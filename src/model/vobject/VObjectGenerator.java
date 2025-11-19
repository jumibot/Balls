package model.vobject;


import model.physics.PhysicsEngine;
import model.physics.PhysicsValues;
import java.util.Random;
import _helpers.DoubleVector;
import static java.lang.System.nanoTime;
import model.Model;
import model.ModelState;


/**
 *
 * VISUAL DYNAMIC OBJECTS GENERATOR
 *
 */
public class VObjectGenerator implements Runnable {

    private final Model model;
    private Thread thread;

    private int maxCreationDelay;       // In millis
    private double acceleration;     // px X milliseconds^-2
    private double speed;
    private int maxSize;
    private int minSize;

    private static final Random rnd = new Random();


    /**
     * CONSTRUCTORS
     *
     * @param theGame
     */
    public VObjectGenerator(
            Model model,
            double maxMass,
            double minMass,
            int maxCreationDelay,
            double acceleration,
            double speed,
            int maxSize,
            int minSize) {

        this.model = model;
        this.maxCreationDelay = maxCreationDelay;
        this.acceleration = acceleration;
        this.speed = speed;
        this.maxSize = maxSize;
        this.minSize = minSize;
    }


    /**
     * PUBLIC
     */
    public void activate() {
        if (this.model.getState() == ModelState.ALIVE) {
            System.out.println("Model is already ALIVE · VObjectGenerator");
            return;
        }
        if (this.model.getState() == ModelState.PAUSED) {
            System.out.println("Model is already PAUSED · VObjectGenerator");
            return;
        }

        this.thread = new Thread(this);
        this.thread.setName("VObject Generator Thread · VObjectGenerator");
        this.thread.setPriority(Thread.NORM_PRIORITY - 3);
        this.thread.start();
        System.out.println("VObject Generator activated! · VObjectGenerator");
    }


    /**
     * PROTECTED
     */
    protected int getMaxCreationDelay() {
        return this.maxCreationDelay;
    }


    protected double getAcceleration() {
        return this.acceleration;
    }


    protected double getSpeed() {
        return this.speed;
    }


    protected int getMaxSize() {
        return this.maxSize;
    }


    protected int getMinSize() {
        return this.minSize;
    }


    protected void setMaxCreationDelay(int maxCreationDelay) {
        this.maxCreationDelay = maxCreationDelay;
    }


    protected void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }


    protected void setSpeed(double speed) {
        this.speed = speed;
    }


    protected void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }


    protected void setMinSize(int minSize) {
        this.minSize = minSize;
    }


    /**
     * PRIVATE
     */
    private VObject newRandomVObject() {
        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(),
                this.randomPosition().x, this.randomPosition().y,
                this.randomSpeed().x, this.randomSpeed().y,
                this.randomAcceleration().x, this.randomAcceleration().y,
                0 // angle
        );

        PhysicsEngine phyEngine = new PhysicsEngine(phyVals);

        // Signature => VObject(int imageId, int radius, BasicPhysicsEngine phyEngine)
        VObject newVObject = new VObject(1, this.randomSize(), phyEngine);

        return newVObject;
    }


    private DoubleVector randomAcceleration() {
        DoubleVector newAcceleration = new DoubleVector(
                VObjectGenerator.rnd.nextGaussian(),
                VObjectGenerator.rnd.nextGaussian(),
                VObjectGenerator.rnd.nextFloat() * this.acceleration);

        return newAcceleration;
    }


    private DoubleVector randomPosition() {
        double x, y;

        // Recuperar tamaño del mundo establecido en el modelo
        x = VObjectGenerator.rnd.nextFloat() * this.model.getWorldDim().width;
        y = VObjectGenerator.rnd.nextFloat() * this.model.getWorldDim().height;

        DoubleVector pos = new DoubleVector(x, y);

        return pos;
    }


    private int randomSize() {
        return (int) (this.minSize + (VObjectGenerator.rnd.nextFloat() * (this.maxSize - this.minSize)));
    }


    private DoubleVector randomSpeed() {
        DoubleVector newAcc = new DoubleVector(
                VObjectGenerator.rnd.nextGaussian(),
                VObjectGenerator.rnd.nextGaussian(),
                VObjectGenerator.rnd.nextFloat() * this.speed);

        return newAcc;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        // Show frames
        while (this.model.getState() != ModelState.STOPPED) { // TO-DO End condition

            if (this.model.getState() == ModelState.ALIVE) { // TO-DO Pause condition
                if (!this.model.addVObject(this.newRandomVObject())) {
                    // Max number of live vObject reached!
                }
            }

            try {
                Thread.sleep(VObjectGenerator.rnd.nextInt(this.maxCreationDelay));
            } catch (InterruptedException ex) {
            }
        }
    }
}
