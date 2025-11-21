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
public class LifeGenerator implements Runnable {

    private final Controller controller;
    private Thread thread;
    private LifeParametersDTO parameters;
    private ArrayList<String> asteroid;
    private static final Random rnd = new Random();


    /**
     * CONSTRUCTORS
     */
    public LifeGenerator(Controller controller, LifeParametersDTO parameters, ArrayList<String> asteroid) {
        this.controller = controller;
        this.parameters = parameters;
        this.asteroid = asteroid;
    }


    /**
     * PUBLIC
     */
    public void activate() {
        if (this.parameters == null) {
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
    private void newRandomVObject() {
        if (this.parameters.fixedAcc != null
                && this.parameters.fixedSpeed != null) {

            this.controller.addVObject(
                    this.randomImage(), this.randomSize(), this.randomPosition(),
                    this.parameters.fixedSpeed, this.parameters.fixedAcc, 0);

            return;
        }

        this.controller.addVObject(
                1, this.randomSize(), this.randomPosition(),
                this.randomSpeed(), this.randomAcceleration(), 0);
    }


    private DoubleVector randomAcceleration() {
        DoubleVector newAcceleration = new DoubleVector(
                LifeGenerator.rnd.nextGaussian(),
                LifeGenerator.rnd.nextGaussian(),
                LifeGenerator.rnd.nextFloat() * this.parameters.accMaxModule);

        return newAcceleration;
    }


    private int randomImage() {
        int index = LifeGenerator.rnd.nextInt(this.asteroid.size());
        return this.asteroid.get(index).hashCode();
    }


    private DoubleVector randomPosition() {
        double x, y;

        // Recuperar tamaño del mundo establecido en el modelo
        x = LifeGenerator.rnd.nextFloat() * this.controller.getWorldDimension().width;
        y = LifeGenerator.rnd.nextFloat() * this.controller.getWorldDimension().height;

        return new DoubleVector(x, y);
    }


    private int randomSize() {
        return (int) (this.parameters.minSize
                + (LifeGenerator.rnd.nextFloat()
                * (this.parameters.maxSize - this.parameters.minSize)));
    }


    private DoubleVector randomSpeed() {
        DoubleVector newAcc = new DoubleVector(
                LifeGenerator.rnd.nextGaussian(),
                LifeGenerator.rnd.nextGaussian(),
                LifeGenerator.rnd.nextFloat() * this.parameters.speedMaxModule);

        return newAcc;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        // Show frames
        while (this.controller.getState() != ControllerState.STOPPED) { // TO-DO End condition

            if (this.controller.getState() == ControllerState.ALIVE) { // TO-DO Pause condition
                this.newRandomVObject();
            }

            try {
                Thread.sleep(LifeGenerator.rnd.nextInt(this.parameters.maxCreationDelay));
            } catch (InterruptedException ex) {
            }
        }
    }
}
