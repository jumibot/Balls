package generators;

import java.util.Random;
import _helpers.DoubleVector;
import controller.Controller;
import controller.EngineState;
import java.util.ArrayList;
import world.WorldDefItemDto;

public class LifeGenerator implements Runnable {

    private final Random rnd = new Random();
    private ArrayList<WorldDefItemDto> items;
    private final Controller controller;
    private Thread thread;

    private final LifeDTO lifeConfig;

    /**
     * CONSTRUCTORS
     */
    public LifeGenerator(Controller controller,
            ArrayList<WorldDefItemDto> items, LifeDTO lifeConfig) {

        this.items = items;
        this.controller = controller;
        this.lifeConfig = lifeConfig;
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
    // ++
    private void addRandomDynamicBody() {
        DoubleVector speed = this.randomSpeed();
        if (this.lifeConfig.fixedSpeed) {
            speed = new DoubleVector(this.lifeConfig.speedX, this.lifeConfig.speedY);
        }

        DoubleVector acc = this.randomAcceleration();
        if (this.lifeConfig.fixedAcc) {
            acc = new DoubleVector(this.lifeConfig.accX, this.lifeConfig.accY);
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
                this.rnd.nextFloat() * this.lifeConfig.maxAccModule);

        return newAcceleration;
    }

    // *+
    private String randomAsset() {
        int index = this.rnd.nextInt(this.items.size());
        return this.items.get(index).assetId;
    }

    private DoubleVector randomPosition() {
        double x, y;

        // Random position within world limits
        x = this.rnd.nextFloat() * this.controller.getWorldDimension().width;
        y = this.rnd.nextFloat() * this.controller.getWorldDimension().height;

        return new DoubleVector(x, y);
    }

    private int randomSize() {
        return (int) (this.lifeConfig.minSize
                + (this.rnd.nextFloat()
                        * (this.lifeConfig.maxSize - this.lifeConfig.minSize)));
    }

    private DoubleVector randomSpeed() {
        DoubleVector speed = new DoubleVector(
                this.rnd.nextGaussian(),
                this.rnd.nextGaussian(),
                this.rnd.nextFloat() * this.lifeConfig.maxSpeedModule);

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
                this.addRandomDynamicBody();
            }

            try {
                Thread.sleep(this.rnd.nextInt(this.lifeConfig.maxCreationDelay));
            } catch (InterruptedException ex) {
            }
        }
    }
}
