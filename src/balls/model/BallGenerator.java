package balls.model;


import balls.physics.BasicPhysicsEngine;
import balls.physics.PhysicsEngine;
import balls.physics.PhysicsValuesDTO;
import java.util.Random;
import helpers.DoubleVector;
import helpers.Position;
import static java.lang.Integer.max;


/**
 *
 * VISUAL DYNAMIC OBJECTS GENERATOR
 *
 */
public class BallGenerator implements Runnable {

    private Model model;
    private Thread thread;

    private final int maxBalls;
    private final double maxMass;
    private final double minMass;
    private final int maxCreationDelay;       // In millis
    private final double maxAcceleration;     // px/milliseconds^2
    private final double maxSpeed;
    private final int maxSize;
    private final int minSize;

    private static Random rnd = new Random();


    /**
     * CONSTRUCTORS
     *
     * @param theGame
     */
    public BallGenerator(Model model, int maxBalls, double maxMass, double minMass,
            int maxCreationDelay, double maxAcceleration, double maxSpeed,
            int maxSize, int minSize) {

        this.model = model;

        this.maxBalls = maxBalls;
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
        this.thread = new Thread(this);
        this.thread.setName("Ball Generator Thread");
        this.thread.start();
    }


    protected void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
        this.maxDeceleration = maxDeceleration;
    }


    protected void setMaxBalls(int maxBalls) {
        // No puede sobrepasar el máximo establecido en el modelo
        this.maxBalls = max(maxBalls, 25);
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
        float x, y;

        // Recuperar tamaño del mundo establecido en el modelo
        x = BallGenerator.rnd.nextFloat() * 1300f;
        y = BallGenerator.rnd.nextFloat() * 800f;

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
        while (true) { // TO-DO End condition

            if (true) { // TO-DO Pause condition
                if (!this.model.addBall(this.newRandomBall())) {
                    System.out.println("Max number of live balls reached!");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            try {
                Thread.sleep(BallGenerator.rnd.nextInt(this.maxCreationDelay - this.minMillisCreationDelay) + this.minMillisCreationDelay);
            } catch (InterruptedException ex) {
            }
        }
    }
}
