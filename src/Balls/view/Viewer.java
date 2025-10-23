package Balls.view;


import Balls.dto.VisualBallCatalogDto;
import Balls.dto.VisualBallDto;
import Balls.model.Ball;
import Helpers.Position;
import Images.dto.ImageDto;
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
    private int pixHeigh;
    private int pixWidth;

    private ImageDto background;


    /**
     * CONSTRUCTORS
     */
    public Viewer(View view, int pixHeigh, int pixWidth, ImageDto background) {
        this.maxFramesPerSecond = 24;
        this.framesPerSecond = 0;
        this.delayInMillis = 30;
        this.view = view;
        this.pixHeigh = pixHeigh;
        this.pixWidth = pixWidth;
        this.background = background;

        Dimension d = new Dimension(pixWidth, pixHeigh);
        this.setPreferredSize(d);

    }


    /**
     * PUBLICS
     */
    public void activate() {
        this.thread = new Thread(this);
        this.thread.setName("VIEWER Thread · Create and display frames");
        //this.thread.setPriority(Thread.MAX_PRIORITY-1);        this.thread.start();
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
        gg.drawImage(this.background.image, 0, 0, this.pixWidth, this.pixHeigh, null);

        // Paint visual objects
        this.paintVO(gg);

        bs.show();
        gg.dispose();
    }


    private void paintVO(Graphics g) {
        Position pos;
        VisualBallCatalogDto visualBallCatalog;
        ArrayList<Ball> balls;
        //= new ArrayList<>();

        // Aconseguir el catalog d'objectes visuals
        // Demanar les coordinades de cadascun dels 
        visualBallCatalog = this.view.getVisualBallCatalog();
        for (VisualBallDto visualBall: visualBallCatalog.visualBalls) {
            pos = this.view.getBallPosition(visualBall.id);

            if (pos.getX() <= this.pixWidth && pos.getY() <= this.pixHeigh
                    && pos.getX() >= 0 && pos.getY() >= 0) {
                ball.paint(g);
            }
        }
    }


    @Override
    public void run() {
        long lastPaintMillisTime;
        long lastPaintMillis;
        long delayMillis;
        long millisPerFrame;
        int framesCounter;

        this.createBufferStrategy(2);

        if ((this.pixHeigh <= 0) || (this.pixWidth <= 0)) {
            System.out.println("Canvas size error: (" + this.pixWidth + "," + this.pixHeigh + ")");
            return; // =======================================================>>
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
