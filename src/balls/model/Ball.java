/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls.model;


import balls.physics.BasicPhysicalEngine;
import balls.physics.PhysicalValuesDTO;
import balls.view.RenderizableObject;
import Helpers.DoubleVector;


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
    public final int maxSizeInPx;

    private final BasicPhysicalEngine phyEngine; // Convert to Atomic Reference


    /**
     * CONSTRUCTORS
     */
    public Ball(int imageId, int maxSizeInPx, BasicPhysicalEngine phyEngine) {
        this.model = null;
        this.thread = new Thread(this);
        this.thread.setName("Ball Thread · " + Ball.createdQuantity);

        this.id = Ball.incCreatedQuantity();
        this.imageId = imageId;
        this.maxSizeInPx = maxSizeInPx;

        this.phyEngine = phyEngine;
    }


    /**
     * PUBLICS
     */
    public void doMovement(PhysicalValuesDTO phyValues) {
        this.phyEngine.setPhysicalValues(phyValues);
    }


    public int getId() {
        return this.id;
    }


    public void setPhysicalChanges(PhysicalValuesDTO newPhyValues) {
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


    protected synchronized RenderizableObject getRenderizableObject() {
        if (this.state != BallState.ALIVE) {
            return null;
        }

        return new RenderizableObject(
                this.id,
                this.imageId,
                this.maxSizeInPx,
                this.phyEngine.getPhysicalValues()
        );
    }


    protected BallState getState() {
        return this.state;
    }


    protected void horizontalRebound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    protected void setModel(Model model) {
        this.model = model;
    }


    protected synchronized void setState(BallState state) {
        this.state = state;
    }


    protected void verticalRebound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void run() {
        PhysicalValuesDTO newPhyValues;

        while (this.getState() != BallState.DEAD) {

            if (this.getState() == BallState.ALIVE) {
                newPhyValues = this.phyEngine.calcNewPhysicalValues();
                this.model.eventDetection(this, newPhyValues);

                //
                // Comprobar si se puede realizar el movimiento
                // Si se puede se setean los nuevos valores físicos
                // STARTING, ALIVE, PAUSED, COLLIDED, DEAD
                //
                //this.nextMove(newPos, newPhyVar); // Try to move
            }

            try {
                /* Descansar */
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
