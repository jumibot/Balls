/**
 * Renderer responsible for drawing the current frame to the screen.
 * Manages the back buffer, background image and the rendering of VObjects
 * using snapshot data provided by the View.
 */
package view;


import _images.ImageDTO;
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

    private final Images backgrounds;
    private final Images voImages;
    private final SpriteCache spriteCache;
    private final Map<Integer, RenderableSprite> renderables = new HashMap<>();

    private final View view;
    private Thread thread;

    private final int delayInMillis;
    private int currentFrame;
    private final Dimension viewDim;

    private final ImageDTO background;
    private VolatileImage viBackground;


    /**
     * CONSTRUCTORS
     */
    public Renderer(View view, Dimension viewDim) {
        this.delayInMillis = 0;
        this.currentFrame = 0;
        this.view = view;
        this.viewDim = viewDim; //*+

        this.backgrounds = this.loadBackgrounds();
        this.background = this.backgrounds.getRamdomImage();
        this.viBackground = null;

        this.voImages = this.loadVOImages();
        this.spriteCache = new SpriteCache(this.getGraphicsConfigurationSafe());

        this.setPreferredSize(this.viewDim);
    }


    /**
     * PUBLICS
     */
    public void activate() {
        this.thread = new Thread(this);
        this.thread.setName("RENDERER Thread · Create and display frames");
        this.thread.setPriority(Thread.NORM_PRIORITY + 2);
        this.thread.start();
    }


    @Override
    public void run() {
        final BufferStrategy bs;

        this.setIgnoreRepaint(true);
        this.createBufferStrategy(3);
        bs = getBufferStrategy();
        if (bs == null) {
            System.out.println("kgd");
            return; // =======================================================>>
        }

        if ((this.viewDim.width <= 0) || (this.viewDim.height <= 0)) {
            System.out.println("Canvas size error: ("
                    + this.viewDim.width + "," + this.viewDim.height + ")");
            return; // ========================================================>
        }

        double t0, t1, t2, t3, draw = 0, iter = 0, last_draw, last_iter;
        while (true) { // TO-DO End condition
            last_draw = draw;
            last_iter = iter;
            t0 = nanoTime();
            if (true) { // TO-DO Pause condition
                this.currentFrame++;
                t1 = nanoTime();
                this.drawScene(bs);
                t2 = nanoTime();
            }

            try {
                Thread.sleep(this.delayInMillis);
            } catch (InterruptedException ex) {
            }
            t3 = nanoTime();
            draw = (t2 - t1) / 1_000_000;
            iter = (t3 - t0) / 1_000_000;
            t0 = nanoTime();

        }
    }


    /**
     * PRIVATES
     */
    private void drawRenderables(Graphics2D g) {
        long t0, t1, t2, t3, full_draw = 0, full_snapshot = 0, getInfo = 0, draw = 0, updateInfo = 0;

        t0 = System.nanoTime(); // Profiling

        ArrayList<RenderInfoDTO> renderInfoList = this.view.getRenderInfo();
        if (renderInfoList == null || renderInfoList.isEmpty()) {
            System.out.println("RenderInfoDTO list is null · Viewer");
            return; // ===================== Do nothing ======================>>
        }

        t1 = System.nanoTime(); // Profiling

        this.updateRenderables(renderInfoList);

        t2 = System.nanoTime(); // Profiling

        for (RenderableSprite renderable : this.renderables.values()) {
            renderable.paint(g);
        }

        // Used to profile performance only
        t3 = System.nanoTime();

        getInfo = (t1 - t0) / 1_000_000;
        updateInfo = (t2 - t1) / 1_000_000;
        full_snapshot = (t2 - t0) / 1_000_000;
        draw = (t3 - t2) / 1_000_000;
        full_draw = (t3 - t0) / 1_000_000;

        return;
    }


    private void drawScene(BufferStrategy bs) {
        long t0, t1, t2, full_scene, background, renderables;

        t0 = nanoTime();

        Graphics2D gg;

        gg = (Graphics2D) bs.getDrawGraphics();
        try {
            // Show background
            gg.setComposite(AlphaComposite.Src); // Sin transparencia
            gg.drawImage(this.getVIBackground(), 0, 0, null);

            t1 = nanoTime();
            // Show VObjects
            gg.setComposite(AlphaComposite.SrcOver); // Usar transparencia
            this.drawRenderables(gg);
        } finally {
            gg.dispose();
        }

        bs.show();
        t2 = nanoTime();
        background = (t1 - t0) / 1_000_000;
        renderables = (t2 - t1) / 1_000_000;
        full_scene = (t2 - t0) / 1_000_000;

        t0 = nanoTime();
    }


    private GraphicsConfiguration getGraphicsConfigurationSafe() {
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
                this.viBackground, this.background.image, this.viewDim);

        return this.viBackground;

    }


    private VolatileImage getVolatileImage(
            VolatileImage vi, BufferedImage src, Dimension dim) {

        GraphicsConfiguration gc = this.getGraphicsConfigurationSafe();

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


    private Images loadBackgrounds() {
        Images images = new Images("src/_images/assets/");

        images.addImageToManifest("background-1.png");
        images.addImageToManifest("background-2.jpeg");
        images.addImageToManifest("background-3.jpeg");
        images.addImageToManifest("background-4.jpeg");
        images.addImageToManifest("background-5.jpg");

        return images;
    }


    private Images loadVOImages() {
        Images images = new Images("src/_images/assets/");

        images.addImageToManifest("asteroid-1-mini.png");
        images.addImageToManifest("asteroid-2-mini.png");
        images.addImageToManifest("spaceship-1.png");
        images.addImageToManifest("spaceship-2.png");

        return images;
    }


    private void updateRenderables(ArrayList<RenderInfoDTO> renderInfoList) {
        // If no objects are alive this frame, clear the cache entirely
        if (renderInfoList == null || renderInfoList.isEmpty()) {
            renderables.clear();
            return;
        }

        // Update or create the RenderableSprite associated with each RenderInfoDTO
        for (RenderInfoDTO newRInfo : renderInfoList) {
            int id = newRInfo.idVObject;

            RenderableSprite renderable = this.renderables.get(id);
            if (renderable == null) {
                // First time this VObject appears → create a cached renderable
                renderable = new RenderableSprite(newRInfo, this.spriteCache, this.currentFrame);
                this.renderables.put(id, renderable);
            } else {
                // Existing renderable → update its snapshot and sprite if needed
                renderable.update(newRInfo, this.spriteCache, this.currentFrame);
            }
        }

        // Remove renderables not updated this frame (i.e., objects no longer alive)
        renderables.entrySet().removeIf(entry
                -> entry.getValue().getLastFrameSeen() != this.currentFrame
        );
    }
}
