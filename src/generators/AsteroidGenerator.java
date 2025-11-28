package generators;


import java.util.Random;
import _helpers.DoubleVector;
import controller.Controller;
import controller.ControllerState;
import java.util.ArrayList;
import world.DynamicBodyDef;


/**
 *
 * VISUAL DYNAMIC OBJECTS GENERATOR
 *
 */
public class AsteroidGenerator implements Runnable {

    private final Random rnd = new Random();

    private ArrayList<DynamicBodyDef> asteroid;
    private final Controller controller;
    private int maxCreationDelay;
    private Thread thread;

    public final int maxSize, minSize;
    public final double maxMass, minMass;
    public final double speedMaxModule;
    public final double accMaxModule;
    public final boolean fixedAcc;
    public final double acc_x, acc_y;
    public final boolean fixedSpeed;
    public final double speed_x, speed_y;


    /**
     * CONSTRUCTORS
     */
    public AsteroidGenerator(Controller controller,
            ArrayList<DynamicBodyDef> asteroid, int maxCreationDelay,
            int maxSize, int minSize, double maxMass, double minMass,
            double speed_x, double speed_y, double acc_x, double acc_y) {

        this.maxCreationDelay = maxCreationDelay;
        this.asteroid = asteroid;
        this.controller = controller;

        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.speedMaxModule = 0;
        this.accMaxModule = 0;
        this.fixedSpeed = true;
        this.fixedAcc = true;
        this.speed_x = speed_x;
        this.speed_y = speed_y;
        this.acc_x = acc_x;
        this.acc_y = acc_y;
    }


    public AsteroidGenerator(Controller controller,
            ArrayList<DynamicBodyDef> asteroid, int maxCreationDelay,
            int maxSize, int minSize, double maxMass, double minMass,
            double maxSpeedModule, double maxAccModule) {

        this.maxCreationDelay = maxCreationDelay;
        this.asteroid = asteroid;
        this.controller = controller;

        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.speedMaxModule = maxSpeedModule;
        this.accMaxModule = maxAccModule;
        this.fixedAcc = false;
        this.fixedSpeed = false;
        this.acc_x = 0d;
        this.acc_y = 0d;
        this.speed_x = 0d;
        this.speed_y = 0d;
    }

    

    /**
     * PUBLIC
     */
    public void activate() {
        this.thread = new Thread(this);
        this.thread.setName("Life generator");
        this.thread.setPriority(Thread.NORM_PRIORITY - 3);
        this.thread.start();
        System.out.println("Life generator activated! · RandomWorld");
    }


    /**
     * PRIVATE
     */
    //++
    private void newRandomDynamicBody() {
        DoubleVector acc, speed;

        if (this.fixedSpeed) {
            speed = new DoubleVector(this.speed_x, this.speed_y);
        } else {
            speed = this.randomSpeed();
        }

        if (this.fixedAcc) {
            acc = new DoubleVector(this.speed_x, this.speed_y);
        } else {
            acc = this.randomAcceleration();
        }

        this.controller.addDynamicBody(
                this.randomImage(), this.randomSize(), this.randomPosition(), speed, acc, 0);
    }


    private DoubleVector randomAcceleration() {

        DoubleVector newAcceleration = new DoubleVector(
                this.rnd.nextGaussian(),
                this.rnd.nextGaussian(),
                this.rnd.nextFloat() * this.accMaxModule);

        return newAcceleration;
    }


    //*+
    private String randomImage() {
        int index = this.rnd.nextInt(this.asteroid.size());
        return this.asteroid.get(index).assetId;
    }


    private DoubleVector randomPosition() {
        double x, y;

        // Recuperar tamaño del mundo establecido en el modelo
        x = this.rnd.nextFloat() * this.controller.getWorldDimension().width;
        y = this.rnd.nextFloat() * this.controller.getWorldDimension().height;

        return new DoubleVector(x, y);
    }


    private int randomSize() {
        return (int) (this.minSize
                + (this.rnd.nextFloat()
                * (this.maxSize - this.minSize)));
    }


    private DoubleVector randomSpeed() {
        DoubleVector newAcc = new DoubleVector(
                this.rnd.nextGaussian(),
                this.rnd.nextGaussian(),
                this.rnd.nextFloat() * this.speedMaxModule);

        return newAcc;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        while (this.controller.getState() != ControllerState.STOPPED) { // TO-DO End condition

            if (this.controller.getState() == ControllerState.ALIVE) { // TO-DO Pause condition
                this.newRandomDynamicBody();
            }

            try {
                Thread.sleep(this.rnd.nextInt(this.maxCreationDelay));
            } catch (InterruptedException ex) {
            }
        }
    }
}
