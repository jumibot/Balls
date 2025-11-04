/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls.model;


import balls.physics.BasicPhysicsEngine;
import balls.physics.PhysicsValuesDTO;
import balls.view.RenderableObject;
import helpers.DoubleVector;
import helpers.Position;


/**
 *
 * @author juanm
 */
public class Ball implements Runnable {

    /* TO-DO: Replace individual static counters by one static array of counters */
    private static int aliveQuantity = 0;
    private static int createdQuantity = 0;
    private static int deadQuantity = 0;

    private Model model;
    private Thread thread;
    private BallState state;

    private final int id;
    public final int imageId;
    public final int radius;

    private final BasicPhysicsEngine phyEngine; // Convert to Atomic Reference


    /**
     * CONSTRUCTORS
     */
    public Ball(int imageId, int radius, BasicPhysicsEngine phyEngine) {
        this.model = null;
        this.thread = null;

        this.id = Ball.incCreatedQuantity();
        this.imageId = imageId;
        this.radius = radius;

        this.phyEngine = phyEngine;
        this.state = BallState.STARTING;
    }


    /**
     * PUBLICS
     */
    public void doMovement(PhysicsValuesDTO phyValues) {

//        System.out.println("Do movement · Ball <" + this.id + "> " + phyValues.position);
        this.phyEngine.setPhysicalValues(phyValues);
    }


    public int getId() {
        return this.id;
    }


    public void setPhysicalChanges(PhysicsValuesDTO newPhyValues) {
        this.phyEngine.setPhysicalValues(newPhyValues);
    }


    /**
     * PROTECTED
     */
    protected synchronized boolean activate() {
        if (this.model == null) {
            System.err.println("Activation error due ball model is null! · (Ball)");
            return false;
        }

        if (!this.model.isAlive()) {
            System.err.println("Activation error due model is not alive! · (Ball)");
            return false;
        }

        if (this.state != BallState.STARTING) {
            System.err.println("Activation error due ball is not starting! · (Ball)");
            return false;
        }

        Ball.incAliveQuantity();
        this.thread = new Thread(this);
        this.thread.setName("Ball Thread · " + this.id);

        this.setState(BallState.ALIVE);
        this.thread.start();
        return true;
    }


    protected synchronized void die() {
        if (this.state == BallState.ALIVE) {
            this.state = BallState.DEAD;
            Ball.deadQuantity++;
            Ball.aliveQuantity--;
        }
    }


    protected PhysicsValuesDTO getPhysicsValues() {
        return this.phyEngine.getPhysicalValues();
    }


    protected synchronized RenderableObject getRenderableObject() {
        if (this.state == BallState.DEAD || this.state == BallState.STARTING) {
            return null;
        }

        return new RenderableObject(
                this.id,
                this.imageId,
                this.radius,
                this.phyEngine.getPhysicalValues()
        );
    }


    protected BallState getState() {
        return this.state;
    }


    protected void doHorizontalRebound(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues) {

        DoubleVector newSpeed
                = new DoubleVector(
                        -newPhyValues.speed.getX(),
                        newPhyValues.speed.getY());

        Position newPosition
                = new Position(
                        0.5,
                        newPhyValues.position.getY(),
                        newPhyValues.position.getTimeStamp());

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicalChanges(reboundPhyValues);
        System.err.println("Horizontal rebound " + this); //*+
    }


    protected void setModel(Model model) {
        this.model = model;
    }


    protected synchronized void setState(BallState state) {
        this.state = state;
    }


    protected void doVerticalRebound(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues) {

        DoubleVector newSpeed
                = new DoubleVector(
                        newPhyValues.speed.getX(),
                        -newPhyValues.speed.getY());

        Position newPosition
                = new Position(
                        newPhyValues.position.getX(),
                        0.5,
                        newPhyValues.position.getTimeStamp());

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicalChanges(reboundPhyValues);
        System.err.println("Vertical rebound " + this); //*+
    }


    @Override
    public void run() {
        PhysicsValuesDTO newPhyValues;

        while ((this.getState() != BallState.DEAD)
                && (this.model.getState() != ModelState.STOPPED)) {

            if ((this.getState() == BallState.ALIVE)
                    && (this.model.getState() == ModelState.ALIVE)) {

                newPhyValues = this.phyEngine.calcNewPhysicsValues();
                this.model.processBallEvents(this, newPhyValues, this.phyEngine.getPhysicalValues());
            }

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in ball thread! (Ball) · " + ex.getMessage());
            }
        }
    }


    public String toString() {
        return "Ball<" + this.id
                + "> p" + this.phyEngine.getPhysicalValues().position
                + " s" + this.phyEngine.getPhysicalValues().speed;
    }


    /**
     * STATICS
     */
    static protected int getCreatedQuantity() {
        return Ball.createdQuantity;
    }


    static protected int getAliveQuantity() {
        return Ball.aliveQuantity;
    }


    static protected int getDeadQuantity() {
        return Ball.deadQuantity;
    }


    static synchronized protected int incCreatedQuantity() {
        Ball.createdQuantity++;

        return Ball.createdQuantity;
    }


    static synchronized protected int incAliveQuantity() {
        Ball.aliveQuantity++;

        return Ball.aliveQuantity;
    }
}
