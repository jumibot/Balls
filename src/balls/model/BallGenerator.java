package balls.model;


import balls.physics.BasicPhysicsEngine;
import balls.physics.PhysicsValuesDTO;
import java.util.Random;
import helpers.DoubleVector;
import helpers.Position;


/**
 *
 * VISUAL DYNAMIC OBJECTS GENERATOR
 *
 */
public class BallGenerator implements Runnable {

    private final Model model;
    private Thread thread;

    private double maxMass;
    private double minMass;
    private int maxCreationDelay;       // In millis
    private double maxAcceleration;     // px/milliseconds^2
    private double maxSpeed;
    private int maxSize;
    private int minSize;

    private static Random rnd = new Random();


    /**
     * CONSTRUCTORS
     *
     * @param theGame
     */
    public BallGenerator(Model model, double maxMass, double minMass,
            int maxCreationDelay, double maxAcceleration, double maxSpeed,
            int maxSize, int minSize) {

        this.model = model;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.maxCreationDelay = maxCreationDelay;
        this.maxAcceleration = maxAcceleration;
        this.maxSpeed = maxSpeed;
        this.maxSize = maxSize;
        this.minSize = minSize;
    }


    /**
     * PROTECTED
     */
    protected void activate() {
        if (this.model.getState() == ModelState.ALIVE) {
            System.out.println("Model is already ALIVE · BallGenerator");
            return;
        }
        if (this.model.getState() == ModelState.PAUSED) {
            System.out.println("Model is already PAUSED · BallGenerator");
            return;
        }

        this.thread = new Thread(this);
        this.thread.setName("Ball Generator Thread · BallGenerator");
        this.thread.start();
        System.out.println("Ball Generator activated! · BallGenerator");
    }


    protected double getMmaxMass() {
        return this.maxMass;
    }


    protected double getMinMass() {
        return this.minMass;
    }


    protected int getMaxCreationDelay() {
        return this.maxCreationDelay;
    }


    protected double getMaxAcceleration() {
        return this.maxAcceleration;
    }


    protected double getMaxSpeed() {
        return this.maxSpeed;
    }


    protected int getMaxSize() {
        return this.maxSize;
    }


    protected int getMinSize() {
        return this.minSize;
    }


    protected void setMmaxMass(double maxMass) {
        this.maxMass = maxMass;
    }


    protected void setMinMass(double minMass) {
        this.minMass = minMass;
    }


    protected void setMaxCreationDelay(int maxCreationDelay) {
        this.maxCreationDelay = maxCreationDelay;
    }


    protected void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }


    protected void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
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
    private Ball newRandomBall() {
        PhysicsValuesDTO phyValues = new PhysicsValuesDTO(
                this.randomMass(),
                this.maxAcceleration,
                this.maxSpeed,
                this.randomPosition(),
                this.randomSpeed(),
                this.randomAcceleration()
        );

        BasicPhysicsEngine phyEngine = new BasicPhysicsEngine(phyValues);

        // Signature => Ball(int imageId, int radius, BasicPhysicsEngine phyEngine)
        Ball newBall = new Ball(1, 20, phyEngine);

        return newBall;
    }


    private DoubleVector randomAcceleration() {
        DoubleVector newAcceleration = new DoubleVector(
                BallGenerator.rnd.nextGaussian() * this.maxAcceleration,
                BallGenerator.rnd.nextGaussian() * this.maxAcceleration);

        return newAcceleration;
    }


    private DoubleVector randomSpeed() {
        DoubleVector newAcceleration = new DoubleVector(
                BallGenerator.rnd.nextGaussian() * this.maxSpeed,
                BallGenerator.rnd.nextGaussian() * this.maxSpeed);

        return newAcceleration;
    }


    private double randomMass() {
        return this.minMass + BallGenerator.rnd.nextFloat() * (this.maxMass - this.minMass);
    }


    private Position randomPosition() {
        double x, y;

        // Recuperar tamaño del mundo establecido en el modelo
        x = BallGenerator.rnd.nextFloat() * this.model.getWorldDimension().x;
        y = BallGenerator.rnd.nextFloat() * this.model.getWorldDimension().y;

        Position position = new Position(x, y);

        return position;
    }


    private double randomSize() {
        return this.minSize + BallGenerator.rnd.nextFloat() * (this.maxSize - this.minSize);
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        // Show frames
        while (this.model.getState() != ModelState.STOPPED) { // TO-DO End condition

            if (this.model.getState() == ModelState.ALIVE) { // TO-DO Pause condition
                if (!this.model.addBall(this.newRandomBall())) {
                    // Max number of live balls reached!
                }
            }

            try {
                Thread.sleep(BallGenerator.rnd.nextInt(this.maxCreationDelay));
            } catch (InterruptedException ex) {
            }
        }
    }
}
