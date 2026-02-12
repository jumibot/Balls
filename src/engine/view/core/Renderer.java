package engine.view.core;

// CRITICAL TO-DO: Is pending release renderables when they are
// removed from the model. Thats occurs when bodies die.

// region Imports
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import engine.controller.ports.EngineState;
import engine.utils.images.ImageCache;
import engine.utils.images.Images;
import engine.utils.helpers.DoubleVector;
import engine.view.hud.impl.RenderHUD;
import engine.view.hud.impl.PlayerHUD;
import engine.view.hud.impl.SpatialGridHUD;
import engine.view.hud.impl.SystemHUD;
import engine.view.renderables.impl.DynamicRenderable;
import engine.view.renderables.impl.Renderable;
import engine.view.renderables.ports.DynamicRenderDTO;
import engine.view.renderables.ports.PlayerRenderDTO;
import engine.view.renderables.ports.RenderDTO;
import engine.view.renderables.ports.RenderMetricsDTO;
import engine.view.renderables.ports.SpatialGridStatisticsRenderDTO;
// endregion

/**
 * Renderer
 * --------
 *
 * Active rendering loop responsible for drawing the current frame to the
 * screen. This class owns the rendering thread and performs all drawing using
 * a BufferStrategy-based back buffer.
 *
 * Architectural role
 * ------------------
 * The Renderer is a pull-based consumer of visual snapshots provided by the
 * View.
 * It never queries or mutates the model directly.
 *
 * Rendering is decoupled from simulation through immutable snapshot DTOs
 * (EntityInfoDTO / DBodyInfoDTO), ensuring that rendering remains deterministic
 * and free of model-side race conditions.
 *
 * Threading model
 * ---------------
 * - A dedicated render thread drives the render loop (Runnable).
 * - Rendering is active only while the engine state is ALIVE.
 * - The loop terminates cleanly when the engine reaches STOPPED.
 *
 * Data access patterns
 * --------------------
 * Three different renderable collections are used, each with a consciously
 * chosen
 * concurrency strategy based on update frequency and thread ownership:
 *
 * 1) Dynamic bodies (DBodies)
 * - Stored in a plain HashMap.
 * - Updated and rendered exclusively by the render thread.
 * - No concurrent access → no synchronization required.
 *
 * 2) Static bodies (SBodies)
 * - Rarely updated, potentially from non-render threads
 * (model → controller → view).
 * - Stored using a copy-on-write strategy:
 * * Updates create a new Map instance.
 * * The reference is swapped atomically via a volatile field.
 * - The render thread only reads stable snapshots.
 *
 * 3) Decorators
 * - Same access pattern as static bodies.
 * - Uses the same copy-on-write + atomic swap strategy.
 *
 * This design avoids locks, minimizes contention, and guarantees that the
 * render thread always iterates over a fully consistent snapshot.
 *
 * Frame tracking
 * --------------
 * A monotonically increasing frame counter (currentFrame) is used to:
 * - Track renderable liveness.
 * - Remove obsolete renderables deterministically.
 *
 * Each update method captures a local frame snapshot to ensure internal
 * consistency, even if the global frame counter advances later.
 *
 * Rendering pipeline
 * ------------------
 * Per frame:
 * 1) Background is rendered to a VolatileImage for fast blitting.
 * 2) Decorators are drawn.
 * 3) Static bodies are drawn.
 * 4) Dynamic bodies are updated and drawn.
 * 5) HUD elements (FPS) are rendered last.
 *
 * Alpha compositing is used to separate opaque background rendering from
 * transparent entities.
 *
 * Performance considerations
 * --------------------------
 * - Triple buffering via BufferStrategy.
 * - VolatileImage used for background caching.
 * - Target frame rate ~60 FPS (16 ms delay).
 * - FPS is measured using a rolling one-second window.
 *
 * Design goals
 * ------------
 * - Deterministic rendering.
 * - Zero blocking in the render loop.
 * - Clear ownership of mutable state.
 * - Explicit, documented concurrency decisions.
 *
 * This class is intended to behave as a low-level rendering component suitable
 * for a small game engine rather than a UI-centric Swing renderer.
 */
public class Renderer extends Canvas implements Runnable {

    // region Constants
    private static final int REFRESH_DELAY_IN_MILLIS = 1; //
    private static final long MONITORING_PERIOD_NS = 750_000_000L;

    // Logs
    private static final boolean DIAGNOSTIC_LOGS_ENABLED = true;
    private static final long DIAGNOSTIC_LOG_EVERY_FRAMES = 120L;
    // endregion

    // region Fields
    private DoubleVector viewDimension;
    private View view;
    private int delayInMillis = 5;
    private long currentFrame = 0;
    private Thread thread;

    private BufferedImage background;
    private Images images;
    private ImageCache imagesCache;

    private double cameraX = 0.0d;
    private double cameraY = 0.0d;
    private double maxCameraClampY;
    private double maxCameraClampX;
    private double backgroundScrollSpeedX = 0.4;
    private double backgroundScrollSpeedY = 0.4;

    private final Map<String, DynamicRenderable> dynamicRenderables = new ConcurrentHashMap<>(2500);
    private volatile Map<String, Renderable> staticRenderables = new ConcurrentHashMap<>(100);

    // HUDs
    private final PlayerHUD playerHUD = new PlayerHUD();
    private final SystemHUD systemHUD = new SystemHUD();
    private final SpatialGridHUD spatialGridHUD = new SpatialGridHUD();
    private final RenderHUD renderHUD = new RenderHUD();

    // Buffers for zero-allocation
    private final Set<String> visibleEntityIds = new LinkedHashSet<>(1600);
    private final int[] scratchIdxBuffer = new int[1600];

    private final RendererProfiler rendererProfiler = new RendererProfiler(MONITORING_PERIOD_NS);

    private long lastSpatialFallbackLogFrame = Long.MIN_VALUE;
    private long lastLocalPlayerGapLogFrame = Long.MIN_VALUE;
    // endregion

    // region Constructors
    public Renderer(View view) {
        this.view = view;

        this.setIgnoreRepaint(true);
        this.setCameraClampLimits();
    }
    // endregion

    // *** PUBLICS ***

    public boolean activate() {
        // Be sure all is ready to begin render!
        if (this.viewDimension == null) {
            throw new IllegalArgumentException("View dimensions not setted");
        }

        if ((this.viewDimension.x <= 0) || (this.viewDimension.y <= 0)) {
            throw new IllegalArgumentException("Canvas size error: ("
                    + this.viewDimension.x + "," + this.viewDimension.y + ")");
        }

        // BufferStrategy fails silently when canvas > screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (this.viewDimension.x > screenSize.width || this.viewDimension.y > screenSize.height) {
            throw new IllegalStateException(
                    "Renderer: Canvas size (" + (int) this.viewDimension.x + "x" + (int) this.viewDimension.y + ") "
                            + "exceeds screen size (" + screenSize.width + "x" + screenSize.height + "). "
                            + "Reduce viewDimension in Main.java or disable UI scaling (sun.java2d.uiScale).");
        }

        while (!this.isDisplayable()) {
            try {
                Thread.sleep(this.delayInMillis);
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        this.setPreferredSize(
                new Dimension((int) this.viewDimension.x, (int) this.viewDimension.y));

        this.thread = new Thread(this);
        this.thread.setName("Renderer");
        this.thread.setPriority(Thread.NORM_PRIORITY + 2);
        this.thread.start();

        System.out.println("Renderer: Activated");
        return true;
    }

    // region adders (add***)
    public void addStaticRenderable(String entityId, String assetId) {
        Renderable renderable = new Renderable(entityId, assetId, this.imagesCache, this.currentFrame);
        this.staticRenderables.put(entityId, renderable);
    }

    public void addDynamicRenderable(String entityId, String assetId) {
        DynamicRenderable renderable = new DynamicRenderable(entityId, assetId, this.imagesCache, this.currentFrame);
        this.dynamicRenderables.put(entityId, renderable);
    }
    // endregion

    // region getters (get***)
    public Renderable getLocalPlayerRenderable() {
        String localPlayerId = this.view.getLocalPlayerId();

        if (localPlayerId == null || localPlayerId.isEmpty()) {
            return null; // ======= No player to follow =======>>
        }
        Renderable renderableLocalPlayer = this.dynamicRenderables.get(this.view.getLocalPlayerId());
        return renderableLocalPlayer;
    }

    /**
     * Get render metrics for HUD display
     */
    public RenderMetricsDTO getRenderMetrics() {
        return new RenderMetricsDTO(
                this.rendererProfiler.getAvgDrawBackgroundMs(),
                this.rendererProfiler.getAvgTranslateMs(),

                this.rendererProfiler.getAvgDrawStaticMs(),
                this.rendererProfiler.getAvgDrawDynamicMs(),
                this.rendererProfiler.getAvgDrawHudsMs(),
                this.rendererProfiler.getAvgShowMs(),

                this.rendererProfiler.getAvgDrawMs(),
                this.rendererProfiler.getAvgUpdateMs(),
                this.rendererProfiler.getAvgFrameMs());
    }
    // endregion

    // region notifiers (notify***)
    public void notifyDynamicIsDead(String entityId) {
        this.dynamicRenderables.remove(entityId);
    }
    // endregion

    // region setters (set***)
    public void setImages(BufferedImage background, Images images) {
        this.background = background;

        this.images = images;
        this.imagesCache = new ImageCache(this.getGraphicsConfSafe(), this.images);
    }

    public void setViewDimension(DoubleVector viewDim) {
        this.viewDimension = viewDim;
        this.setCameraClampLimits();
        this.setPreferredSize(new Dimension((int) this.viewDimension.x, (int) this.viewDimension.y));
    }

    // endregion

    public void updateStaticRenderables(ArrayList<RenderDTO> renderablesData) {
        if (renderablesData == null) {
            return; // ========= Nothing to render by the moment ... =========>>
        }

        Map<String, Renderable> newRenderables = new java.util.concurrent.ConcurrentHashMap<>(this.staticRenderables);

        if (renderablesData.isEmpty()) {
            newRenderables.clear(); //
            this.staticRenderables = newRenderables;
            return;
        }

        // Update a renderable associated with each DBodyRenderInfoDTO
        long cFrame = this.currentFrame;
        for (RenderDTO renderableData : renderablesData) {
            String entityId = renderableData.entityId;
            if (entityId == null || entityId.isEmpty()) {
                continue;
            }

            Renderable renderable = newRenderables.get(entityId);
            if (renderable == null) {
                throw new IllegalStateException("Renderer: Static renderable not found: " + entityId);
            }
            renderable.update(renderableData, cFrame);
        }

        newRenderables.entrySet().removeIf(e -> e.getValue().getLastFrameSeen() != cFrame);
        this.staticRenderables = newRenderables; // atomic swap
    }

    // *** PRIVATES ***

    // region drawers (draw***)
    private void drawDynamics(Graphics2D g, Set<String> visibleIds) {
        long paintStart = this.rendererProfiler.startInterval();

        for (String entityId : visibleIds) {
            DynamicRenderable renderable = this.dynamicRenderables.get(entityId);
            if (renderable != null) {
                renderable.paint(g, this.currentFrame);
            }
        }

        // Safety net: always paint local player if present, even if a transient
        // SpatialGrid/query race skipped it in the visibleIds list.
        Renderable localPlayerRenderable = this.getLocalPlayerRenderable();
        if (!(localPlayerRenderable instanceof DynamicRenderable)) {
            System.out.print(this.view.getLocalPlayerId());
            System.out.println("KGD");
        }

        this.rendererProfiler.stopInterval(RendererProfiler.METRIC_PAINT_DYNAMIC, paintStart);
    }

    private void drawHUDs(Graphics2D g) {

        long fps = this.rendererProfiler.getLastFps();
        double avgDrawMs = this.rendererProfiler.getAvgDrawMs();

        this.systemHUD.draw(g,
                fps,
                String.format("%.0f", avgDrawMs) + " ms",
                this.imagesCache == null ? 0 : this.imagesCache.size(),
                String.format("%.0f", this.imagesCache == null ? 0 : this.imagesCache.getHitsPercentage()) + "%",
                this.view.getEntityAliveQuantity(),
                this.view.getEntityDeadQuantity(),
                this.currentFrame);

        this.renderHUD.draw(g, this.getRenderMetrics().toObjectArray());

        PlayerRenderDTO playerData = this.view.getLocalPlayerRenderData();
        if (playerData != null) {
            this.playerHUD.draw(g, playerData.toObjectArray());
        }

        SpatialGridStatisticsRenderDTO spatialGridStats = this.view.getSpatialGridStatistics();
        if (spatialGridStats != null) {
            this.spatialGridHUD.draw(g, spatialGridStats.toObjectArray());
        }
    }

    private void drawStatics(Graphics2D g) {
        Map<String, Renderable> renderables = this.staticRenderables;

        for (Renderable renderable : renderables.values()) {
            if (this.isVisible(renderable)) {
                renderable.paint(g, this.currentFrame);
            }
        }
    }

    private void drawScene(BufferStrategy bs, Set<String> visibleIds) {
        Graphics2D gg;

        do {
            gg = (Graphics2D) bs.getDrawGraphics();

            try {
                // 1) BACKGROUND

                // region PROFILER L-3: Start Background
                long bgStart = this.rendererProfiler.startInterval();

                gg.setComposite(AlphaComposite.Src); // Opaque
                this.drawTiledBackground(gg);

                this.rendererProfiler.stopInterval(RendererProfiler.METRIC_DRAW_BACKGROUND, bgStart);
                // endregion PROFILER L-3: Stop Background

                // 2) WORLD TRANSLATE (due camera)

                // region PROFILER L-3: Start Camera tranlate
                long translateStart = this.rendererProfiler.startInterval();

                gg.setComposite(AlphaComposite.SrcOver); // With transparency
                AffineTransform defaultTransform = gg.getTransform();
                gg.translate(-this.cameraX, -this.cameraY);

                this.rendererProfiler.stopInterval(RendererProfiler.METRIC_TRANSLATE, translateStart);
                // endregion PROFILER L-3: Stop Camera translate

                // 3) STATICS

                // region PROFILER L-3: Start Statics
                long staticStart = this.rendererProfiler.startInterval();

                this.drawStatics(gg);

                this.rendererProfiler.stopInterval(RendererProfiler.METRIC_DRAW_STATIC, staticStart);
                // endregion PROFILER L-3: Stop Statics

                // 4) DYNAMICS

                // region PROFILER L-3: Start Dynamics
                long dynamicsStart = this.rendererProfiler.startInterval();

                this.drawDynamics(gg, visibleIds);

                this.rendererProfiler.stopInterval(RendererProfiler.METRIC_DRAW_DYNAMIC, dynamicsStart);
                // endregion PROFILER L-3: Stop Dynamics

                // 5) HUD (on top of everything)

                // region PROFILER L-3: Start draw HUDs
                long hudsStart = this.rendererProfiler.startInterval();

                gg.setTransform(defaultTransform);
                gg.setComposite(AlphaComposite.SrcOver); // With transparency
                this.drawHUDs(gg);

                this.rendererProfiler.stopInterval(RendererProfiler.METRIC_DRAW_HUDS, hudsStart);
                // endregion PROFILER L-3: Stop draw HUDs

            } finally {
                gg.dispose();
            }

            // region PROFILER L-3: Start show
            long showStart = this.rendererProfiler.startInterval();

            bs.show();
            // Toolkit.getDefaultToolkit().sync();

            this.rendererProfiler.stopInterval(RendererProfiler.METRIC_SHOW, showStart);
            // endregion PROFILER L-3: Stop show

        } while (bs.contentsLost());
    }

    private void drawTiledBackground(Graphics2D g) {
        if (this.background == null || this.viewDimension == null)
            return;

        final int viewW = (int) this.viewDimension.x;
        final int viewH = (int) this.viewDimension.y;
        if (viewW <= 0 || viewH <= 0)
            return;

        final int tileW = this.background.getWidth(null);
        final int tileH = this.background.getHeight(null);
        if (tileW <= 0 || tileH <= 0)
            return;

        final double scrollX = this.cameraX * this.backgroundScrollSpeedX;
        final double scrollY = this.cameraY * this.backgroundScrollSpeedY;

        // Tile offset in [-(tile-1)..0], stable with negatives
        final int offX = -Math.floorMod((int) Math.floor(scrollX), tileW);
        final int offY = -Math.floorMod((int) Math.floor(scrollY), tileH);

        // Start 1 tile before to ensure full coverage
        final int startX = offX - tileW;
        final int startY = offY - tileH;
        for (int x = startX; x < viewW + tileW; x += tileW) {
            for (int y = startY; y < viewH + tileH; y += tileH) {
                g.drawImage(this.background, x, y, null);
            }
        }
    }
    // endregion

    // region getters (get***)
    private GraphicsConfiguration getGraphicsConfSafe() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (gc == null) {
            gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
        }

        return gc;
    }

    private VolatileImage getVolatileImage(
            VolatileImage vi, BufferedImage src, Dimension dim) {

        GraphicsConfiguration gc = this.getGraphicsConfSafe();

        if (vi == null || vi.getWidth() != dim.width || vi.getHeight() != dim.height
                || vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            // New volatile image
            vi = gc.createCompatibleVolatileImage(dim.width, dim.height, Transparency.OPAQUE);
        }

        int val;
        do {
            val = vi.validate(gc);
            if (val != VolatileImage.IMAGE_OK || vi.contentsLost()) {
                Graphics2D g = vi.createGraphics();
                g.drawImage(src, 0, 0, dim.width, dim.height, null);
                g.dispose();
            }
        } while (vi.contentsLost());

        return vi;
    }
    // endregion

    private boolean isVisible(Renderable renderable) {
        RenderDTO renderData = renderable.getRenderData();
        if (renderData == null) {
            return false;
        }

        double viewW = this.viewDimension.x;
        double viewH = this.viewDimension.y;

        double camLeft = this.cameraX;
        double camTop = this.cameraY;
        double camRight = camLeft + viewW;
        double camBottom = camTop + viewH;

        double half = renderData.size * 0.5d;
        if (renderable.getImage() != null) {
            double halfW = renderable.getImage().getWidth(null) * 0.5d;
            double halfH = renderable.getImage().getHeight(null) * 0.5d;
            half = Math.max(halfW, halfH);
        }

        double minX = renderData.posX - half;
        double maxX = renderData.posX + half;
        double minY = renderData.posY - half;
        double maxY = renderData.posY + half;

        if (maxX < camLeft || minX > camRight) {
            return false; // ==== Out of horizontal bounds ======>>
        }

        if (maxY < camTop || minY > camBottom) {
            return false; // ==== Out of vertical bounds ======>>
        }

        return true;
    }

    private void clearDynamicRenderables() {
        System.out.println("Renderer: Clearing dynamic renderables (" + this.dynamicRenderables.size() + ")");
        for (DynamicRenderable renderable : this.dynamicRenderables.values()) {
            renderable.releaseRenderData();
        }
        this.dynamicRenderables.clear();
    }

    // region setters (set***)
    private void setCameraClampLimits() {
        DoubleVector woldDim = this.view.getWorldDimension();

        if (woldDim == null || this.viewDimension == null) {
            this.maxCameraClampX = 0.0;
            this.maxCameraClampY = 0.0;
            return; // ======= No world or view dimensions info ======= >>
        }

        this.maxCameraClampX = Math.max(0.0, woldDim.x - this.viewDimension.x);
        this.maxCameraClampY = Math.max(0.0, woldDim.y - this.viewDimension.y);
    }
    // endregion

    // region updaters (update***)
    private void updateCamera() {
        Renderable localPlayerRenderable = this.getLocalPlayerRenderable();
        DoubleVector worldDim = this.view.getWorldDimension();

        if (localPlayerRenderable == null || this.viewDimension == null || worldDim == null) {
            return; // ======== No player or data to follow =======>>
        }

        RenderDTO playerData = localPlayerRenderable.getRenderData();

        double playerX = playerData.posX - this.cameraX;
        double playerY = playerData.posY - this.cameraY;

        double desiredX;
        double desiredY;

        double minX = this.viewDimension.x * 0.3;
        double maxX = this.viewDimension.x * 0.7;
        double minY = this.viewDimension.y * 0.3;
        double maxY = this.viewDimension.y * 0.7;

        if (playerX < minX) {
            desiredX = playerData.posX - minX;
        } else if (playerX > maxX) {
            desiredX = playerData.posX - maxX;
        } else {
            desiredX = playerData.posX - (playerX);
        }

        if (playerY < minY) {
            desiredY = playerData.posY - minY;
        } else if (playerY > maxY) {
            desiredY = playerData.posY - maxY;
        } else {
            desiredY = playerData.posY - (playerY);
        }

        // double desiredX = playerData.posX - (this.viewDimension.x / 2.0d);
        // double desiredY = playerData.posY - (this.viewDimension.y / 2.0d);

        this.cameraX += (desiredX - this.cameraX);
        this.cameraY += (desiredY - this.cameraY);

        // // Clamp when camera goes out of world limits
        this.cameraX = clamp(cameraX, 0.0, this.maxCameraClampX);
        this.cameraY = clamp(cameraY, 0.0, this.maxCameraClampY);
    }

    private void updateDynamicRenderables(ArrayList<DynamicRenderDTO> renderDataList) {
        if (renderDataList == null || renderDataList.isEmpty()) {
            System.out.println("Renderer: No dynamic render data, clearing dynamic renderables.");
            this.clearDynamicRenderables();
            return; // ========= Nothing to render by the moment ... =========>>
        }

        // Update or create a renderable associated with each DBodyRenderInfoDTO
        long cFrame = this.currentFrame;

        for (DynamicRenderDTO newRenderData : renderDataList) {
            String entityId = newRenderData.entityId;
            if (entityId == null || entityId.isEmpty()) {
                newRenderData.release();
                continue; // ======= No entityId, cannot be rendered =======>>
            }

            DynamicRenderable renderable = this.dynamicRenderables.get(entityId);
            if (renderable == null) {
                newRenderData.release();
                continue; // ======= No renderable, cannot update render data =======>>
            }

            renderable.releaseRenderData();
            renderable.update(newRenderData, cFrame);
        }
    }
    // endregion

    private static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    // *** INTERFACE IMPLEMENTATIONS ***

    // region Runnable
    @Override
    public void run() {
        this.createBufferStrategy(3);
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            throw new IllegalStateException(
                    "Renderer: BufferStrategy creation failed (canvas too large): "
                            + (int) this.viewDimension.x + "x" + (int) this.viewDimension.y);
        }

        while (true) {
            EngineState engineState = this.view.getEngineState();
            if (engineState == EngineState.STOPPED) {
                break; // ======= Engine stopped, exit render loop =======>>
            }

            // region PROFILER L-1: Start Total Frame
            long totalFrameStart = this.rendererProfiler.startInterval();

            if (engineState == EngineState.ALIVE) { // TO-DO Pause condition

                this.currentFrame++;
                this.rendererProfiler.addFrame();

                // 1) Calculate Visible Entities (at frame -1)
                String localPlayerId = this.view.getLocalPlayerId();
                double minX, maxX, minY, maxY;

                if (localPlayerId == null || localPlayerId.isEmpty()) {
                    minX = 0;
                    minY = 0;
                    maxX = this.viewDimension.x * 2;
                    maxY = this.viewDimension.y * 2;
                } else {
                    RenderDTO renderLocalPlayerData = this.view.getRenderData(localPlayerId);

                    minX = renderLocalPlayerData.posX - (this.viewDimension.x);
                    minY = renderLocalPlayerData.posY - (this.viewDimension.y);
                    maxX = renderLocalPlayerData.posX + (this.viewDimension.x);
                    maxY = renderLocalPlayerData.posY + (this.viewDimension.y);
                }

                Set<String> visibleIds = this.view.queryEntitiesInRegion(
                        minX, maxX,
                        minY, maxY,
                        this.scratchIdxBuffer,
                        this.visibleEntityIds);

                // 2) Snapshot of dynamic render data

                // region PROFILER L-2: Start Update Phase
                long updatePhaseStart = this.rendererProfiler.startInterval();

                ArrayList<DynamicRenderDTO> renderData = this.view.snapshotRenderData(visibleIds);

                this.updateDynamicRenderables(renderData);

                this.rendererProfiler.stopInterval(RendererProfiler.METRIC_UPDATE_PHASE, updatePhaseStart);
                this.updateCamera();

                // endregion PROFILER L-2: Stop Update Phase

                // 3) Draw the scene with the current snapshot

                // region PROFILER L-2: Start Draw Phase
                long drawPhaseStart = this.rendererProfiler.startInterval();

                this.drawScene(bs, visibleIds);

                this.rendererProfiler.stopInterval(RendererProfiler.METRIC_DRAW_PHASE, drawPhaseStart);
                // endregion PROFILER L-2: Stop Draw Phase

                this.view.syncInputState(); // To prevent staus keys inconsistencies
            }

            try {
                Thread.sleep(REFRESH_DELAY_IN_MILLIS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            this.rendererProfiler.stopInterval(RendererProfiler.METRIC_TOTAL_FRAME, totalFrameStart);
            // endregion PROFILER L-1: Stop Total Frame
        }
    }
    // endregion
}
