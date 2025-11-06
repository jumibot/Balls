package view;


import _helpers.DoubleVector;
import _images.ImageDTO;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import static java.lang.Long.max;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class Viewer extends Canvas implements Runnable {

    private View view;
    private Thread thread;

    private int delayInMillis;
    private int framesPerSecond;
    private int maxFramesPerSecond;
    private final DoubleVector worldDimension;
    private ImageDTO background;


    /**
     * CONSTRUCTORS
     */
    public Viewer(View view, DoubleVector worldDimension, ImageDTO background) {
        this.maxFramesPerSecond = 24;
        this.framesPerSecond = 0;
        this.delayInMillis = 30;
        this.view = view;
        this.worldDimension = worldDimension; //*+
        this.background = background;

        Dimension d = new Dimension((int) this.worldDimension.x, (int) this.worldDimension.y);
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
        Graphics gg = bs.getDrawGraphics();
        gg.drawImage(this.background.image, 0, 0, (int) this.worldDimension.x, (int) this.worldDimension.y, null);

        // Paint visual objects
        this.paintRenderables(gg);

        bs.show();
        gg.dispose();
    }


    private void paintRenderables(Graphics g) {
        DoubleVector coordinates;
        ArrayList<RenderableObject> renderableObjects = this.view.getRenderableObjects();

        if (renderableObjects == null) {
            System.out.println("RenderableObjects ArrayList is null · Viewer");
            return;
        }

        for (RenderableObject renderableObject : renderableObjects) {
            coordinates = renderableObject.phyValues.position;

            if (coordinates.x <= this.worldDimension.x && coordinates.y <= this.worldDimension.y
                    && coordinates.x >= 0 && coordinates.y >= 0) {

                renderableObject.paint(g);

            } else {
                System.out.println("NO Paint" + renderableObject);
            }
        }
    }


    @Override
    public void run() {
        long lastPaintMillisTime;
        long lastPaintMillis;
        long delayMillis = 1000;
        long millisPerFrame;
        int framesCounter;

        this.createBufferStrategy(2);

        if ((this.worldDimension.x <= 0) || (this.worldDimension.y <= 0)) {
            System.out.println(
                    "Canvas size error: (" + this.worldDimension.x + "," + this.worldDimension.y + ")");
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
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }

        }
    }
}
