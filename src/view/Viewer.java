package view;


import _helpers.DoubleVector;
import _helpers.Position;
import _images.ImageDTO;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
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
    private final DoubleVector viewDimension;
    private ImageDTO background;


    /**
     * CONSTRUCTORS
     */
    public Viewer(View view, DoubleVector viewDimension, ImageDTO background) {
        this.maxFramesPerSecond = 24;
        this.framesPerSecond = 0;
        this.delayInMillis = 10;
        this.view = view;
        this.viewDimension = viewDimension; //*+
        this.background = background;

        Dimension d = new Dimension((int) this.viewDimension.x, (int) this.viewDimension.y);
        this.setPreferredSize(d);
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
    private void paint() {
        BufferStrategy bs;

        bs = this.getBufferStrategy();
        if (bs == null) {
            System.out.println("kgd");
            return; // =======================================================>>
        }

        // Paint background
        Graphics2D gg = (Graphics2D) bs.getDrawGraphics();
        gg.drawImage(this.background.image, 0, 0, (int) this.viewDimension.x, (int) this.viewDimension.y, null);

        // Paint visual objects
        this.paintRenderables(gg);

        bs.show();
        gg.dispose();
    }


    private void paintRenderables(Graphics2D g) {
        Position position;
        ArrayList<RenderableObject> renderableObjects = this.view.getRenderableObjects();

        if (renderableObjects == null) {
            System.out.println("RenderableObjects ArrayList is null · Viewer");
            return; // =======================================================>>
        }
        if (renderableObjects.isEmpty()) {
            System.out.println("No objects to render · Viewer");
            return; // =======================================================>>
        }

        for (RenderableObject renderableObject : renderableObjects) {
            position = renderableObject.phyValues.position;

            // Prevent to paint objects out of view => clipping
            if (position.x <= this.viewDimension.x && position.y <= this.viewDimension.y
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
        long delayMillis = 10;
        long millisPerFrame;
        int framesCounter;

        this.createBufferStrategy(2);

        if ((this.viewDimension.x <= 0) || (this.viewDimension.y <= 0)) {
            System.out.println(
                    "Canvas size error: (" + this.viewDimension.x + "," + this.viewDimension.y + ")");
            return; // ========================================================>
        }
//         Show frames
        framesCounter = 0;
        millisPerFrame = 1000 / this.maxFramesPerSecond;
        while (true) { // TO-DO End condition
            lastPaintMillisTime = currentTimeMillis();
            if (true) { // TO-DO Pause condition
                this.paint();
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
