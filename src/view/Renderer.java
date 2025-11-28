/**
 * Renderer responsible for drawing the current frame to the screen.
 * Manages the back buffer, background image and the rendering of VObjects
 * using snapshot data provided by the View.
 */
package view;


import _images.Images;
import _images.ImageCache;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Renderer extends Canvas implements Runnable {

    private Dimension viewDimension;
    private View view;
    private int delayInMillis = 20;
    private int currentFrame = 0;
    private Thread thread;

    private BufferedImage background;
    private Images dBodyImage;
    private Images sBodyImage;
    private Images spaceDecoratorImage;

    private ImageCache dBodyCache;
    private ImageCache sBodyCache;
    private ImageCache spaceDecoratorCache;
    private VolatileImage viBackground;

    private final Map<Integer, DBodyRenderable> dBodyRenderables = new HashMap<>();


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
        this.setPreferredSize(this.viewDimension);

        this.thread = new Thread(this);
        this.thread.setName("RENDERER");
        this.thread.setPriority(Thread.NORM_PRIORITY + 2);
        this.thread.start();

        return true;
    }


    public void setImages(
            BufferedImage background, Images dBody,
            Images sBody, Images sDecorator) {

        this.background = background;
        this.viBackground = null;

        this.dBodyImage = dBody;
        this.dBodyCache = new ImageCache(this.getGraphicsConfSafe(), this.dBodyImage);

        this.sBodyImage = sBody;
        this.sBodyCache = new ImageCache(this.getGraphicsConfSafe(), this.sBodyImage);

        this.spaceDecoratorImage = sDecorator;
        this.spaceDecoratorCache = new ImageCache(this.getGraphicsConfSafe(), this.dBodyImage);
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
    private void drawDBodies(Graphics2D g) {
        ArrayList<DBodyRenderInfoDTO> dBodyRenderInfo = this.view.getDBodyRenderInfo();

        this.updateDBodyRenderables(dBodyRenderInfo);

        for (DBodyRenderable renderable : this.dBodyRenderables.values()) {
            renderable.paint(g);
        }
    }


    private void drawScene(BufferStrategy bs) {
        Graphics2D gg;

        gg = (Graphics2D) bs.getDrawGraphics();
        try {
            // Show background
            gg.setComposite(AlphaComposite.Src); // Opaque
            gg.drawImage(this.getVIBackground(), 0, 0, null);

            // Show decorators
            
            // Show static bodies
            
            // Show ynamic bodies
            gg.setComposite(AlphaComposite.SrcOver); // With transparency
            this.drawDBodies(gg);

        } finally {
            gg.dispose();
        }

        bs.show();
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


    private void updateDBodyRenderables(ArrayList<DBodyRenderInfoDTO> renderInfoList) {
        // If no objects are alive this frame, clear the cache entirely
        if (renderInfoList == null || renderInfoList.isEmpty()) {
            this.dBodyRenderables.clear();
            return; // ========= Nothing to render by the moment ... =========>>
        }

        // Update or create a renderable associated with each DBodyRenderInfoDTO
        for (DBodyRenderInfoDTO newRInfo : renderInfoList) {
            int id = newRInfo.entityId;

            DBodyRenderable renderable = this.dBodyRenderables.get(id);
            if (renderable == null) {
                // First time this VObject appears → create a cached renderable
                renderable = new DBodyRenderable(newRInfo, this.dBodyCache, this.currentFrame);
                this.dBodyRenderables.put(id, renderable);
            } else {
                // Existing renderable → update its snapshot and sprite if needed
                renderable.update(newRInfo, this.currentFrame);
            }
        }

        // Remove renderables not updated this frame (i.e., objects no longer alive)
        this.dBodyRenderables.entrySet().removeIf(entry
                -> entry.getValue().getLastFrameSeen() != this.currentFrame
        );
    }
}
