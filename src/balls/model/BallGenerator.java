package balls.model;


import java.util.Random;
import Helpers.DoubleVector;


/**
 *
 * VISUAL DYNAMIC OBJECTS GENERATOR
 *
 */
public class BallGenerator implements Runnable {

    private Model model;
    private Thread thread;
    private int maxBallsQuantity;
    private int maxMillis;
    private int minMillis;
    private DoubleVector maxAcceleration;
    private DoubleVector minAcceleration;
    private double maxScale;
    private double minScale;


    /**
     * CONSTRUCTORS
     *
     * @param theGame
     */
    public BallGenerator(Model model) {
        this.maxBallsQuantity = 20;

        this.minMillis = 10;
        this.maxMillis = 40;

        this.minScale = 0.1;
        this.maxScale = 0.5;

        this.minAcceleration = new DoubleVector(-0.0001, -0.0001);
        this.maxAcceleration = new DoubleVector(0.0001, 0.0001);

        this.thread = new Thread(this);
        this.thread.setName("Ball Generator Thread");

        this.model = model;
    }


    /**
     * PROTECTED
     */
    protected void activate() {
        this.thread.start();
    }


    protected void setAccelerationRange(float minX, float minY, float maxX, float maxY) {
        this.minAcceleration.setXY(minX, minY);
        this.maxAcceleration.setXY(maxX, maxY);
    }


    protected void setMaxBallsQuantity(int maxBallsQuantity) {
        this.maxBallsQuantity = maxBallsQuantity;
    }


    protected void setMillis(int minMillis, int maxMillis) {
        this.minMillis = minMillis;
        this.maxMillis = maxMillis;
    }


    /**
     * PRIVATE
     */
    private Ball createBall() {
        Ball ball;

        ball = null;

        return ball;
    }


    private DoubleVector randomAcceleration() {
        Random rnd = new Random();
        DoubleVector newAcceleration = new DoubleVector(
                this.minAcceleration.getX() + rnd.nextGaussian() * (this.maxAcceleration.getX() - this.minAcceleration.getX()),
                this.minAcceleration.getY() + rnd.nextGaussian() * (this.maxAcceleration.getY() - this.minAcceleration.getY()));

        return newAcceleration;
    }


    private DoubleVector randomPosition() {
        float x, y;

        Random rnd = new Random();
        x = rnd.nextFloat() * 1300f;
        y = rnd.nextFloat() * 800f;

        DoubleVector position = new DoubleVector(x, y);

        return position;
    }


    private double randomScale() {
        Random rnd = new Random();

        return this.minScale + rnd.nextFloat() * (this.maxScale - this.minScale);
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        Random rnd = new Random();

        // Show frames
        while (true) { // TO-DO End condition

            if (true) { // TO-DO Pause condition
                if (!this.model.addBall(this.createBall())) {
                    System.out.println("Max number of live balls reached!");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            try {
                Thread.sleep(rnd.nextInt(this.maxMillis - this.minMillis) + this.minMillis);
            } catch (InterruptedException ex) {
            }
        }
    }
}
