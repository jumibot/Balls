package generators;


import java.util.Random;
import _helpers.DoubleVector;
import controller.Controller;
import controller.EngineState;
import java.util.ArrayList;
import world.VItemDto;


public class LifeGenerator implements Runnable {

    private final Random rnd = new Random();

    private ArrayList<VItemDto> asteroid;
    private final Controller controller;
    private final int maxCreationDelay;
    private Thread thread;

    public final int maxSize, minSize;
    public final double maxMass, minMass;
    public final double maxSpeedModule;
    public final double accMaxModule;
    public final boolean fixedAcc;
    public final double acc_x, acc_y;
    public final boolean fixedSpeed;
    public final double speed_x, speed_y;


    /**
     * CONSTRUCTORS
     */
    public LifeGenerator(Controller controller,
            ArrayList<VItemDto> asteroid, int maxCreationDelay,
            int maxSize, int minSize, double maxMass, double minMass,
            double speed_x, double speed_y, double acc_x, double acc_y) {

        this.maxCreationDelay = maxCreationDelay;
        this.asteroid = asteroid;
        this.controller = controller;

        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.maxSpeedModule = 0;
        this.accMaxModule = 0;
        this.fixedSpeed = true;
        this.fixedAcc = true;
        this.speed_x = speed_x;
        this.speed_y = speed_y;
        this.acc_x = acc_x;
        this.acc_y = acc_y;
    }


    public LifeGenerator(Controller controller,
            ArrayList<VItemDto> asteroid, int maxCreationDelay,
            int maxSize, int minSize, double maxMass, double minMass,
            double maxSpeedModule, double maxAccModule) {

        this.maxCreationDelay = maxCreationDelay;
        this.asteroid = asteroid;
        this.controller = controller;

        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.maxSpeedModule = maxSpeedModule;
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
    private void addRandomDBody() {
        DoubleVector acc, speed;

        if (this.fixedSpeed) {
            speed = new DoubleVector(this.speed_x, this.speed_y);
        } else {
            speed = this.randomSpeed();
        }

        if (this.fixedAcc) {
            acc = new DoubleVector(this.acc_x, this.acc_y);
        } else {
            acc = this.randomAcceleration();
        }

        DoubleVector pos = this.randomPosition();
        this.controller.addDBody(
                this.randomAsset(), this.randomSize(),
                pos.x, pos.y, speed.x, speed.y, acc.x, acc.y,
                0d, this.randomAngularSpeed(460d), 0d, 0d);
    }


    private DoubleVector randomAcceleration() {

        DoubleVector newAcceleration = new DoubleVector(
                this.rnd.nextGaussian(),
                this.rnd.nextGaussian(),
                this.rnd.nextFloat() * this.accMaxModule);

        return newAcceleration;
    }


    //*+
    private String randomAsset() {
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
        DoubleVector speed = new DoubleVector(
                this.rnd.nextGaussian(),
                this.rnd.nextGaussian(),
                this.rnd.nextFloat() * this.maxSpeedModule);

        return speed;
    }


    private double randomAngularSpeed(double maxAngularSpeed) {
        return this.rnd.nextFloat() * maxAngularSpeed - maxAngularSpeed / 2;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        while (this.controller.getEngineState() != EngineState.STOPPED) { // TO-DO End condition

            if (this.controller.getEngineState() == EngineState.ALIVE) { // TO-DO Pause condition
//                this.addRandomDBody();
            }

            try {
                Thread.sleep(this.rnd.nextInt(this.maxCreationDelay));
            } catch (InterruptedException ex) {
            }
        }
    }
}
