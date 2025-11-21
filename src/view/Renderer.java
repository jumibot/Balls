/**
 * Renderer responsible for drawing the current frame to the screen.
 * Manages the back buffer, background image and the rendering of VObjects
 * using snapshot data provided by the View.
 */
package view;


import _images.Images;
import _images.SpriteCache;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import static java.lang.System.nanoTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Renderer extends Canvas implements Runnable {

    private Dimension viewDimension;
    private View view;
    private int delayInMillis = 1;
    private int currentFrame = 0;
    private Thread thread;

    private Images asteroidImages;
    private Images playerImages;
    private SpriteCache asteroidCache;
    private SpriteCache playerCache;
    private BufferedImage background;
    private VolatileImage viBackground;

    private final Map<Integer, RenderableSprite> renderables = new HashMap<>();


    /**
     * CONSTRUCTORS
     */
    public Renderer(View view) {
        this.view = view;

        this.setIgnoreRepaint(true);
    }


    /**
     * PUBLICS
     */
    public boolean activate() {
        if (this.viewDimension == null) {
            throw new IllegalArgumentException("Null view dimension");
        }

        if (this.background == null) {
            System.out.println("Warning: No background image · Renderer");
        }
        if (this.asteroidImages == null) {
            System.out.println("Warning: No asteroids images · Renderer");
        }
        if (this.playerImages == null) {
            System.out.println("Warning: No players image · Renderer");
        }

        this.setPreferredSize(this.viewDimension);

        this.thread = new Thread(this);
        this.thread.setName("RENDERER");
        this.thread.setPriority(Thread.NORM_PRIORITY + 2);
        this.thread.start();

        return true;
    }


    public void setAssets(BufferedImage background, Images asteroidImages, Images playerImages) {
        this.background = background;
        this.viBackground = null;

        this.asteroidImages = asteroidImages;
        this.asteroidCache = new SpriteCache(this.getGraphicsConfSafe(), this.asteroidImages);
        this.playerImages = playerImages;
        this.playerCache = new SpriteCache(this.getGraphicsConfSafe(), this.playerImages);
    }


    public void SetViewDimension(Dimension viewDim) {
        this.viewDimension = viewDim;
        this.setPreferredSize(this.viewDimension);
    }


    @Override
    public void run() {
        this.createBufferStrategy(3);
        BufferStrategy bs = getBufferStrategy();

        if (this.viewDimension == null) {
            throw new IllegalArgumentException("BufferStrategy cannot be created");
        }

        if ((this.viewDimension.width <= 0) || (this.viewDimension.height <= 0)) {
            System.out.println("Canvas size error: ("
                    + this.viewDimension.width + "," + this.viewDimension.height + ")");
            return; // ========================================================>
        }

        while (true) { // TO-DO End condition
            if (true) { // TO-DO Pause condition
                this.currentFrame++;
                this.drawScene(bs);
            }

            try {
                Thread.sleep(this.delayInMillis);
            } catch (InterruptedException ex) {
            }
        }
    }


    /**
     * PRIVATES
     */
    private void drawRenderables(Graphics2D g) {
//        t0 = System.nanoTime(); // Profiling

        ArrayList<RenderInfoDTO> renderInfoList = this.view.getRenderInfo();

//        t1 = System.nanoTime(); // Profiling
        this.updateRenderables(renderInfoList);

//        t2 = System.nanoTime(); // Profiling
        for (RenderableSprite renderable : this.renderables.values()) {
            renderable.paint(g);
        }

        // Used to profile performance only
//        t3 = System.nanoTime();
//
//        getInfo = (t1 - t0) / 1_000_000;
//        updateInfo = (t2 - t1) / 1_000_000;
//        full_snapshot = (t2 - t0) / 1_000_000;
//        draw = (t3 - t2) / 1_000_000;
//        full_draw = (t3 - t0) / 1_000_000;
//
//        return;
    }


    private void drawScene(BufferStrategy bs) {
//        long t0, t1, t2, full_scene, background, renderables;
//
//        t0 = nanoTime();

        Graphics2D gg;

        gg = (Graphics2D) bs.getDrawGraphics();
        try {
            // Show background
            gg.setComposite(AlphaComposite.Src); // Opaque
            gg.drawImage(this.getVIBackground(), 0, 0, null);

//            t1 = nanoTime();
            // Show VObjects
            gg.setComposite(AlphaComposite.SrcOver); // With transparency
            this.drawRenderables(gg);

        } finally {
            gg.dispose();
        }

        bs.show();
//        t2 = nanoTime();
//        background = (t1 - t0) / 1_000_000;
//        renderables = (t2 - t1) / 1_000_000;
//        full_scene = (t2 - t0) / 1_000_000;
//
//        t0 = nanoTime();
    }


    private GraphicsConfiguration getGraphicsConfSafe() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (gc == null) {
            gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
        }

        return gc;
    }


    private VolatileImage getVIBackground() {
        this.viBackground = this.getVolatileImage(
                this.viBackground, this.background, this.viewDimension);

        return this.viBackground;

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
            if (val != VolatileImage.IMAGE_OK) {
                Graphics2D g = vi.createGraphics();
                g.drawImage(src, 0, 0, dim.width, dim.height, null);
                g.dispose();
            }
        } while (vi.contentsLost());

        return vi;
    }


    private void updateRenderables(ArrayList<RenderInfoDTO> renderInfoList) {
        // If no objects are alive this frame, clear the cache entirely
        if (renderInfoList == null || renderInfoList.isEmpty()) {
            renderables.clear();
            return; // ========= Nothing to render by the moment ... =========>>
        }

        // Update or create the RenderableSprite associated with each RenderInfoDTO
        for (RenderInfoDTO newRInfo : renderInfoList) {
            int id = newRInfo.idVObject;

            RenderableSprite renderable = this.renderables.get(id);
            if (renderable == null) {
                // First time this VObject appears → create a cached renderable
                renderable = new RenderableSprite(newRInfo, this.asteroidCache, this.currentFrame);
                this.renderables.put(id, renderable);
            } else {
                // Existing renderable → update its snapshot and sprite if needed
                renderable.update(newRInfo, this.asteroidCache, this.currentFrame);
            }
        }

        // Remove renderables not updated this frame (i.e., objects no longer alive)
        renderables.entrySet().removeIf(entry
                -> entry.getValue().getLastFrameSeen() != this.currentFrame
        );
    }
}
