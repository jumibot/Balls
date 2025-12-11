package model.entities;


import model.physics.BasicPhysicsEngine;
import model.physics.PhysicsValues;
import view.renderables.DBodyInfoDTO;
import model.ModelState;
import model.physics.PhysicsEngine;


/**
 * BodyEntity
 *
 * Represents a single entity in the simulation model. Each BodyEntity
 * maintains: • A unique identifier, visual attributes (imageId, radius, color)
 * • Its own PhysicsEngine instance, which stores and updates the immutable
 * PhysicsValues snapshot (position, speed, acceleration, angle, etc.) • A
 * dedicated thread responsible for advancing its physics state over time
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
public class DynamicBody extends AbstractEntity implements PhysicsBody, Runnable {

    private Thread thread;

    private final BasicPhysicsEngine phyEngine; // Convert to Atomic Reference


    /**
     * CONSTRUCTORS
     */
    public DynamicBody(String assetId, double size, BasicPhysicsEngine phyEngine) {
        super(assetId, size);

        this.phyEngine = phyEngine;
    }


    /**
     * PUBLICS
     */
    @Override
    public synchronized void activate() {
        super.activate();

        this.thread = new Thread(this);
        this.thread.setName("Body " + this.getEntityId());
        this.thread.setPriority(Thread.NORM_PRIORITY - 1);
        this.thread.start();
        this.setState(EntityState.ALIVE);
    }


    public void addAngularAcceleration(double angularSpeed) {
        this.phyEngine.addAngularAcceleration(angularSpeed);
    }


    @Override
    public DBodyInfoDTO buildEntityInfo() {
        if (this.getState() == EntityState.DEAD || this.getState() == EntityState.STARTING) {
            return null;
        }

        PhysicsValues phyValues = this.phyEngine.getPhysicsValues();

        return new DBodyInfoDTO(
                this.getEntityId(), this.assetId, this.size,
                phyValues.timeStamp,
                phyValues.posX, phyValues.posY,
                phyValues.speedX, phyValues.speedY,
                phyValues.accX, phyValues.accY,
                phyValues.angle);
    }


    public double getAngularSpeed() {
        return this.phyEngine.getAngularSpeed();
    }


    public PhysicsEngine getPhysicsEngine() {
        return this.phyEngine;
    }


    public void resetAcceleration() {
        this.phyEngine.resetAcceleration();
    }


    @Override
    public void run() {
        PhysicsValues newPhyValues;

        while ((this.getState() != EntityState.DEAD)
                && (this.getModel().getState() != ModelState.STOPPED)) {

            if ((this.getState() == EntityState.ALIVE)
                    && (this.getModel().getState() == ModelState.ALIVE)) {

                newPhyValues = this.phyEngine.calcNewPhysicsValues();
                this.getModel().processDBodyEvents(this, newPhyValues, this.phyEngine.getPhysicsValues());
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in vObject thread! (VObject) · " + ex.getMessage());
            }
        }
    }


    public void setAngularAcceleration(double angularAcc) {
        this.phyEngine.setAngularAcceleration(angularAcc);
    }


    public void setAngularSpeed(double angularSpeed) {
        this.phyEngine.setAngularSpeed(angularSpeed);
    }


    public void setThrust(double thrust) {
        this.phyEngine.setThrust(thrust);
    }


    @Override
    public String toString() {
        return "Body <" + this.getEntityId()
                + "> p (" + this.phyEngine.getPhysicsValues().posX + "," + this.phyEngine.getPhysicsValues().posX + ") "
                + " s (" + this.phyEngine.getPhysicsValues().speedX + "," + this.phyEngine.getPhysicsValues().speedX + ")";
    }
}
