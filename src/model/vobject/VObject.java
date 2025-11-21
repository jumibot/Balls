package model.vobject;


import model.physics.PhysicsEngine;
import model.physics.PhysicsValues;
import view.RenderInfoDTO;
import _helpers.DoubleVector;
import java.awt.Color;
import java.awt.Dimension;
import model.Model;
import model.ModelState;


/**
 * VObject
 *
 * Represents a single entity in the simulation model. Each VObject maintains: •
 * A unique identifier, visual attributes (imageId, radius, color) • Its own
 * PhysicsEngine instance, which stores and updates the immutable PhysicsValues
 * snapshot (position, speed, acceleration, angle, etc.) • A dedicated thread
 * responsible for advancing its physics state over time
 *
 * VObjects interact exclusively with the Model, reporting physics updates and
 * requesting event processing (collisions, rebounds, etc.). The view layer
 * never reads mutable state directly; instead, VObject produces a RenderInfoDTO
 * snapshot encapsulating all visual and physical data required for rendering.
 *
 * Lifecycle control (STARTING → ALIVE → DEAD) is managed internally, and static
 * counters track global quantities of created, active and dead VObjects.
 *
 * The goal of this class is to isolate per-object behavior and physics
 * evolution while keeping the simulation thread-safe through immutable
 * snapshots and a clearly separated rendering pipeline.
 *
 * Static counters (createdQuantity, aliveQuantity, deadQuantity) track global
 * VObject lifecycle metrics. Although simple, these counters enable
 * instrumentation and debugging of the simulation, providing a quick overview
 * of object churn. They are updated in synchronized methods, ensuring
 * thread-safe increments even under heavy concurrency.
 */
public class VObject implements Runnable {

    /* TO-DO: Replace individual static counters by one static array of counters */
    private static int aliveQuantity = 0;
    private static int createdQuantity = 0;
    private static int deadQuantity = 0;

    private Model model;
    private Thread thread;
    private volatile VObjectState state;

    private final int id;
    public final int imageId;
    public final int radius;
    private Color color;

    private final PhysicsEngine phyEngine; // Convert to Atomic Reference


    /**
     * CONSTRUCTORS
     */
    public VObject(int imageId, int radius, PhysicsEngine phyEngine) {

        this.model = null;
        this.thread = null;

        this.id = VObject.createdQuantity++;
        this.imageId = imageId;
        this.radius = radius;
        this.color = Color.BLUE;

        this.phyEngine = phyEngine;
        this.state = VObjectState.STARTING;
    }


    /**
     * PUBLICS
     */
    public void doMovement(PhysicsValues phyValues) {
        this.phyEngine.setPhysicsValues(phyValues);
    }


    public int getId() {
        return this.id;
    }


    public synchronized void activate() {
        if (this.model == null) {
            throw new IllegalArgumentException("Model not setted");
        }

        if (!this.model.isAlive()) {
            throw new IllegalArgumentException("Visual object activation error due MODEL is not alive!");
        }

        if (this.state != VObjectState.STARTING) {
            throw new IllegalArgumentException("Visual object activation error due is not starting!");
        }

        VObject.aliveQuantity++;
        this.thread = new Thread(this);
        this.thread.setName("VObject s" + this.id);
        this.thread.setPriority(Thread.NORM_PRIORITY - 1);
        this.setState(VObjectState.ALIVE);
        this.thread.start();
    }


    public synchronized void die() {
        if (this.state == VObjectState.ALIVE) {
            this.state = VObjectState.DEAD;
            VObject.deadQuantity++;
            VObject.aliveQuantity--;
        }
    }


    public RenderInfoDTO buildRenderInfo() {
        if (this.state == VObjectState.DEAD || this.state == VObjectState.STARTING) {
            return null;
        }

        PhysicsValues phyValues = this.phyEngine.getPhysicsValues();

        return new RenderInfoDTO(
                this.id, this.imageId, this.radius, this.color,
                phyValues.timeStamp,
                phyValues.posX, phyValues.posY,
                phyValues.speedX, phyValues.speedY,
                phyValues.accX, phyValues.accY,
                phyValues.angle);
    }


    public VObjectState getState() {
        return this.state;
    }


    public void reboundInEast(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDim) {

        this.phyEngine.reboundInEast(newPhyValues, oldPhyValues, worldDim);
    }


    public void reboundInWest(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDim) {

        this.phyEngine.reboundInWest(newPhyValues, oldPhyValues, worldDim);
    }


    public void reboundInNorth(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDim) {

        this.phyEngine.reboundInNorth(newPhyValues, oldPhyValues, worldDim);
    }


    public void reboundInSouth(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDim) {

        this.phyEngine.reboundInSouth(newPhyValues, oldPhyValues, worldDim);
    }


    @Override
    public void run() {
        PhysicsValues newPhyValues;

        while ((this.getState() != VObjectState.DEAD)
                && (this.model.getState() != ModelState.STOPPED)) {

            if ((this.getState() == VObjectState.ALIVE)
                    && (this.model.getState() == ModelState.ALIVE)) {

                newPhyValues = this.phyEngine.calcNewPhysicsValues();
                this.model.processVObjectEvents(this, newPhyValues, this.phyEngine.getPhysicsValues());
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in vObject thread! (VObject) · " + ex.getMessage());
            }
        }
    }


    public void setModel(Model model) {
        this.model = model;
    }


    public void setState(VObjectState state) {
        this.state = state;
    }


    /**
     * PROTECTED
     */
    protected PhysicsValues getPhysicsValues() {
        return this.phyEngine.getPhysicsValues();
    }


    @Override
    public String toString() {
        return "VObject<" + this.id
                + "> p (" + this.phyEngine.getPhysicsValues().posX + "," + this.phyEngine.getPhysicsValues().posX + ") "
                + " s (" + this.phyEngine.getPhysicsValues().speedX + "," + this.phyEngine.getPhysicsValues().speedX + ")";
    }


    /**
     * PRIVATE
     */
    private DoubleVector adjustBounds(
            DoubleVector worldDimension, PhysicsValues phyValues, double delta) {

        double x = Math.max(phyValues.posX, delta);
        x = Math.min(x, worldDimension.x - delta);

        double y = Math.max(phyValues.posY, delta);
        y = Math.min(y, worldDimension.y - delta);

        return new DoubleVector(x, y);
    }


    /**
     * STATICS
     */
    static public int getCreatedQuantity() {
        return VObject.createdQuantity;
    }


    static public int getAliveQuantity() {
        return VObject.aliveQuantity;
    }


    static public int getDeadQuantity() {
        return VObject.deadQuantity;
    }


    static synchronized protected int incCreatedQuantity() {
        VObject.createdQuantity++;

        return VObject.createdQuantity;
    }


    static synchronized protected int incAliveQuantity() {
        VObject.aliveQuantity++;

        return VObject.aliveQuantity;
    }
}
