package controller;


import java.util.Random;
import _helpers.DoubleVector;
import static java.lang.System.nanoTime;
import java.util.ArrayList;


/**
 *
 * VISUAL DYNAMIC OBJECTS GENERATOR
 *
 */
public class RandomWorld implements Runnable {

    private final Controller controller;
    private Thread thread;
    private RandomWorldDTO wParams;
    private ArrayList<String> asteroid;
    private final Random rnd = new Random();


    /**
     * CONSTRUCTORS
     */
    public RandomWorld(Controller controller, RandomWorldDTO wParams, ArrayList<String> asteroid) {
        this.controller = controller;
        this.wParams = wParams;
        this.asteroid = asteroid;
    }


    /**
     * PUBLIC
     */
    public void activate() {
        if (this.wParams == null) {
            throw new IllegalArgumentException("Parameters are not setted!");
        }

        this.thread = new Thread(this);
        this.thread.setName("VObject GENERATOR");
        this.thread.setPriority(Thread.NORM_PRIORITY - 3);
        this.thread.start();
        System.out.println("VObject Generator activated! · VObjectGenerator");
    }


    /**
     * PRIVATE
     */
    private void newRandomDynamicBody() {
        DoubleVector acc, speed;
        RandomDBodyDTO params = this.wParams.dBodyParams;

        if (this.wParams.dBodyParams.fixedSpeed) {
            speed = new DoubleVector(params.fixedSpeed_x, params.fixedSpeed_y);
        } else {
            speed = this.randomSpeed();
        }

        if (params.fixedAcc) {
            acc = new DoubleVector(params.fixedAcc_x, params.fixedAcc_y);
        } else {
            acc = this.randomAcceleration();
        }

        this.controller.addDynamicBody(
                this.randomImage(), this.randomSize(), this.randomPosition(), speed, acc, 0);
    }


    private DoubleVector randomAcceleration() {
        RandomDBodyDTO params = this.wParams.dBodyParams;

        DoubleVector newAcceleration = new DoubleVector(
                this.rnd.nextGaussian(),
                this.rnd.nextGaussian(),
                this.rnd.nextFloat() * params.accMaxModule);

        return newAcceleration;
    }


    private int randomImage() {
        int index = this.rnd.nextInt(this.asteroid.size());
        return this.asteroid.get(index).hashCode();
    }


    private DoubleVector randomPosition() {
        double x, y;

        // Recuperar tamaño del mundo establecido en el modelo
        x = this.rnd.nextFloat() * this.controller.getWorldDimension().width;
        y = this.rnd.nextFloat() * this.controller.getWorldDimension().height;

        return new DoubleVector(x, y);
    }


    private int randomSize() {
        RandomDBodyDTO params = this.wParams.dBodyParams;

        return (int) (params.minSize
                + (this.rnd.nextFloat()
                * (params.maxSize - params.minSize)));
    }


    private DoubleVector randomSpeed() {
        RandomDBodyDTO params = this.wParams.dBodyParams;

        DoubleVector newAcc = new DoubleVector(
                this.rnd.nextGaussian(),
                this.rnd.nextGaussian(),
                this.rnd.nextFloat() * params.speedMaxModule);

        return newAcc;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        RandomDBodyDTO params = this.wParams.dBodyParams;

        // Show frames
        while (this.controller.getState() != ControllerState.STOPPED) { // TO-DO End condition

            if (this.controller.getState() == ControllerState.ALIVE) { // TO-DO Pause condition
                this.newRandomDynamicBody();
            }

            try {
                Thread.sleep(this.rnd.nextInt(this.wParams.maxCreationDelay));
            } catch (InterruptedException ex) {
            }
        }
    }
}
