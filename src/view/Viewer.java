package view;


import _helpers.DoubleVector;
import _images.ImageDTO;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class Viewer extends Canvas implements Runnable {

    private final View view;
    private Thread thread;

    private int delayInMillis;
    private int framesPerSecond;
    private int maxFramesPerSecond;
    private final Dimension viewDim;
    private ImageDTO background;
    private VolatileImage viBackground;


    /**
     * CONSTRUCTORS
     */
    public Viewer(View view, Dimension viewDim, ImageDTO background) {
        this.maxFramesPerSecond = 24;
        this.framesPerSecond = 0;
        this.delayInMillis = 25;
        this.view = view;
        this.viewDim = viewDim; //*+
        this.background = background;
        this.viBackground = null;

        this.setPreferredSize(this.viewDim);
    }


    /**
     * PUBLICS
     */
    public void activate() {
        this.thread = new Thread(this);
        this.thread.setName("VIEWER Thread · Create and display frames");
        this.thread.setPriority(Thread.NORM_PRIORITY + 2);
        this.thread.start();
    }


    /**
     * PRIVATES
     */
    private void drawScene(BufferStrategy bs) {
        Graphics2D gg;

        gg = (Graphics2D) bs.getDrawGraphics();
        try {
            gg.setComposite(AlphaComposite.Src);
            gg.drawImage(this.getVIBackground(), 0, 0, null);

            this.drawRenderables(gg);
        } finally {
            gg.dispose();
        }

        bs.show();
    }


    private VolatileImage getVIBackground() {

        this.viBackground = this.getValidVolatileImage(
                this.viBackground, this.background.image, this.viewDim);

        return this.viBackground;

    }


    private VolatileImage getValidVolatileImage(
            VolatileImage vi, Image src, Dimension dim) {

        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (gc == null) {
            gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();
        }
        if (gc == null) {
            return null; // ======= No access to hardware acceleration =======> 
        }

        if (vi == null || vi.getWidth() != dim.width || vi.getHeight() != dim.height
                || vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            vi = gc.createCompatibleVolatileImage(dim.width, dim.height, Transparency.OPAQUE);
        }

        int val;
        Graphics2D g;
        do {
            val = vi.validate(gc);

            if (val != VolatileImage.IMAGE_OK) {
                g = vi.createGraphics();
                g.drawImage(src, 0, 0, dim.width, dim.height, null);
                g.dispose();
            }
        } while (vi.contentsLost());

        return vi;
    }


    private void drawRenderables(Graphics2D g) {
        DoubleVector position;

        ArrayList<RenderableVObject> renderableObjects = this.view.getRenderableObjects();

        if (renderableObjects == null) {
            System.out.println("RenderableObjects ArrayList is null · Viewer");
            return; // =======================================================>>
        }

        for (RenderableVObject renderableObject : renderableObjects) {
            position = renderableObject.phyValues.position;
            renderableObject.paint(g, position);
        }
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

        while (true) { // TO-DO End condition
            if (true) { // TO-DO Pause condition
                this.drawScene(bs);
            }

            try {
                Thread.sleep(this.delayInMillis);
            } catch (InterruptedException ex) {
            }
        }
    }
}
