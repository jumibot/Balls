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
    }


    /**
     * PUBLICS
     */
    public void doMovement(PhysicsValuesDTO phyValues) {
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
        if (!this.model.isAlive()) {
            System.err.println("ERROR Model is not alive! · (Ball)");
            return false;
        }

        if (this.state != BallState.STARTING) {
            System.err.println("ERROR Ball is not starting! · (Ball)");
            return false;
        }

        this.thread = new Thread(this);
        this.thread.setName("Ball Thread · " + this.id);

        this.setState(BallState.ALIVE);
        this.thread.start();
        Ball.aliveQuantity++;
        return true;
    }


    protected synchronized void die() {
        if (this.state == BallState.ALIVE) {
            this.state = BallState.DEAD;
            Ball.deadQuantity++;
            Ball.aliveQuantity--;
        }
    }


    protected synchronized RenderableObject getRenderableObject() {
        if (this.state != BallState.ALIVE) {
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


    protected void horizontalRebound(PhysicsValuesDTO phyValues) {
        DoubleVector newSpeed
                = new DoubleVector(
                        -phyValues.speed.getX(),
                        phyValues.speed.getY());

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        phyValues.mass,
                        phyValues.maxModuleAcceleration,
                        phyValues.maxModuleDeceleration,
                        phyValues.maxModuleSpeed,
                        phyValues.position,
                        newSpeed,
                        phyValues.acceleration
                );

        this.setPhysicalChanges(reboundPhyValues);
    }


    protected void setModel(Model model) {
        this.model = model;
    }


    protected synchronized void setState(BallState state) {
        this.state = state;
    }


    protected void verticalRebound(PhysicsValuesDTO phyValues) {
        DoubleVector newSpeed
                = new DoubleVector(
                        phyValues.speed.getX(),
                        -phyValues.speed.getY());

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        phyValues.mass,
                        phyValues.maxModuleAcceleration,
                        phyValues.maxModuleDeceleration,
                        phyValues.maxModuleSpeed,
                        phyValues.position,
                        newSpeed,
                        phyValues.acceleration
                );

        this.setPhysicalChanges(reboundPhyValues);
    }


    @Override
    public void run() {
        PhysicsValuesDTO newPhyValues;

        while ((this.getState() != BallState.DEAD)
                && (this.model.getState() != ModelState.STOPED)) {

            if ((this.getState() == BallState.ALIVE)
                && (this.model.getState() == ModelState.ALIVE)) {
                
                newPhyValues = this.phyEngine.calcNewPhysicsValues();
                this.model.detectEvents(this, newPhyValues);
            }

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in ball thread! (Ball) · " + ex.getMessage());
            }
        }
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
}
