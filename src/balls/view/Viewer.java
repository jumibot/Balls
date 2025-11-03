package balls.view;


import helpers.DoubleVector;
import Images.ImageDTO;
import java.awt.Canvas;
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
    private int viewHeigh;
    private int viewWidth;

    private ImageDTO background;


    /**
     * CONSTRUCTORS
     */
    public Viewer(View view, int viewHeigh, int viewWidth, ImageDTO background) {
        this.maxFramesPerSecond = 24;
        this.framesPerSecond = 0;
        this.delayInMillis = 30;
        this.view = view;
        this.viewHeigh = viewHeigh;
        this.viewWidth = viewWidth;
        this.background = background;

        Dimension d = new Dimension(viewWidth, viewHeigh);
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
        gg.drawImage(this.background.image, 0, 0, this.viewWidth, this.viewHeigh, null);

        // Paint visual objects
        this.paintVisualBalls(gg);

        bs.show();
        gg.dispose();
    }


    private void paintVisualBalls(Graphics g) {
        DoubleVector coordinates;
        ArrayList<RenderableObject> renderableObjects = this.view.getRenderableObjects();

        if (renderableObjects == null) {
            return;
        }

        for (RenderableObject renderableObject : renderableObjects) {
            coordinates = renderableObject.getCoordinates();

            if (coordinates.getX() <= this.viewWidth && coordinates.getY() <= this.viewHeigh
                    && coordinates.getX() >= 0 && coordinates.getY() >= 0) {

                System.out.println("Paint " + renderableObject + coordinates);

                renderableObject.paint(g);

            } else {
                System.out.println("NO Paint" + renderableObject + coordinates);
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

        if ((this.viewHeigh <= 0) || (this.viewWidth <= 0)) {
            System.out.println(
                    "Canvas size error: (" + this.viewWidth + "," + this.viewHeigh + ")");
            return; // ========================================================>
        }

        // Show frames
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
