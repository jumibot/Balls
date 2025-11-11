package view;


import _helpers.DoubleVector;
import _helpers.Position;
import _images.ImageDTO;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import static java.lang.Long.max;
import static java.lang.System.currentTimeMillis;
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
    private VolatileImage vIBackground;


    /**
     * CONSTRUCTORS
     */
    public Viewer(View view, Dimension viewDim, ImageDTO background) {
        this.maxFramesPerSecond = 24;
        this.framesPerSecond = 0;
        this.delayInMillis = 20;
        this.view = view;
        this.viewDim = viewDim; //*+
        this.background = background;
        this.vIBackground = null;

        this.setPreferredSize(this.viewDim);
    }


    /**
     * PUBLICS
     */
    public void activate() {
        this.thread = new Thread(this);
        this.thread.setName("VIEWER Thread · Create and display frames");
        this.thread.start();
    }


    /**
     * PRIVATES
     */
    private void drawScene(BufferStrategy bs) {
        Graphics2D gg;

        do {
            do {
                gg = (Graphics2D) bs.getDrawGraphics();
                try {
                    gg.setComposite(AlphaComposite.Src);
                    gg.drawImage(
                            this.getVIBackground(), 0, 0,
                            this.viewDim.width, this.viewDim.height, null);

                    this.paintRenderables(gg);
                } finally {
                    gg.dispose();
                }
            } while (bs.contentsRestored());

            bs.show();
            Toolkit.getDefaultToolkit().sync();
        } while (bs.contentsLost());
    }


    private VolatileImage getVIBackground() {

        this.vIBackground = this.getVolatileImage(
                this.vIBackground, this.background.image, this.viewDim);

        return this.vIBackground;

    }


    private VolatileImage getVolatileImage(
            VolatileImage vImage,
            Image image,
            Dimension dim) {

        GraphicsConfiguration gc = this.getGraphicsConfiguration();
        if (gc == null) {
            vImage = null;
            return vImage; // === Sin acceso a hardware acceleración gráficos
        }

        if (vImage == null) {
            // Imagen volatil todavia no creada
            vImage = gc.createCompatibleVolatileImage(
                    dim.width, dim.height, Transparency.OPAQUE);
        }

        if (vImage.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            // Imagen volatil estaba creada pero ya no es valida
            vImage = gc.createCompatibleVolatileImage(
                    dim.width, dim.height, Transparency.OPAQUE);
        }

        Graphics2D g = vImage.createGraphics();
        g.setColor(Color.BLACK);
        g.drawImage(image, 0, 0, dim.width, dim.height, null);
        g.dispose();

        return vImage;
    }


    private void paintRenderables(Graphics2D g) {
        Position position;
        ArrayList<RenderableVObject> renderableObjects = this.view.getRenderableObjects();

        if (renderableObjects == null) {
            System.out.println("RenderableObjects ArrayList is null · Viewer");
            return; // =======================================================>>
        }
        if (renderableObjects.isEmpty()) {
            System.out.println("No objects to render · Viewer");
            return; // =======================================================>>
        }

        for (RenderableVObject renderableObject : renderableObjects) {
            position = renderableObject.phyValues.position;

            // Prevent to paint objects out of view => clipping
            if (position.x <= this.viewDim.width && position.y <= this.viewDim.height
                    && position.x >= 0 && position.y >= 0) {

                renderableObject.paint(g, position);

            } else {
//                System.out.println("NO Paint" + renderableObject);
            }
        }
    }


    @Override
    public void run() {
        long lastPaintMillisTime;
        long lastPaintMillis;
        long delayMillis = 20;
        long millisPerFrame;
        int framesCounter;
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

        framesCounter = 0;
        millisPerFrame = 1000 / this.maxFramesPerSecond;
        while (true) { // TO-DO End condition
            lastPaintMillisTime = currentTimeMillis();
            if (true) { // TO-DO Pause condition
                this.drawScene(bs);
            }

            lastPaintMillis = currentTimeMillis() - lastPaintMillisTime;
            delayMillis = max(0, millisPerFrame - lastPaintMillis);
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException ex) {
            }
        }
    }
}
