package model.entities;


import model.physics.BasicPhysicsEngine;
import model.physics.PhysicsValues;
import view.RenderInfoDTO;
import _helpers.DoubleVector;
import java.awt.Color;
import java.awt.Dimension;
import model.Model;
import model.ModelState;
import model.physics.PhysicsEngine;


/**
 * BodyEntity
 *
 * Represents a single entity in the simulation model. Each BodyEntity maintains: 
 * • A unique identifier, visual attributes (imageId, radius, color) 
 * • Its own PhysicsEngine instance, which stores and updates the immutable 
 * PhysicsValues snapshot (position, speed, acceleration, angle, etc.) 
 * • A dedicated thread responsible for advancing its physics state over time
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
    public DynamicBody(int imageId, int size, BasicPhysicsEngine phyEngine) {
        super(imageId, size);

        this.phyEngine = phyEngine;
    }


    /**
     * PUBLICS
     */
    @Override
    public synchronized void activate() {
        super.activate();

        this.thread = new Thread(this);
        this.thread.setName("Body " + this.getId());
        this.thread.setPriority(Thread.NORM_PRIORITY - 1);
        this.thread.start();
        this.setState(EntityState.ALIVE);
    }


    @Override
    public RenderInfoDTO buildRenderInfo() {
        if (this.getState() == EntityState.DEAD || this.getState() == EntityState.STARTING) {
            return null;
        }

        PhysicsValues phyValues = this.phyEngine.getPhysicsValues();

        return new RenderInfoDTO(
                this.getId(), this.imageId, this.size, this.getColor(),
                phyValues.timeStamp,
                phyValues.pos_x, phyValues.pos_y,
                phyValues.speed_x, phyValues.speed_y,
                phyValues.acc_x, phyValues.acc_y,
                phyValues.angle);
    }


    @Override
    public PhysicsEngine getPhysicsEngine() {
        return this.phyEngine;
    }


    @Override
    public void run() {
        PhysicsValues newPhyValues;

        while ((this.getState() != EntityState.DEAD)
                && (this.getModel().getState() != ModelState.STOPPED)) {

            if ((this.getState() == EntityState.ALIVE)
                    && (this.getModel().getState() == ModelState.ALIVE)) {

                newPhyValues = this.phyEngine.calcNewPhysicsValues();
                this.getModel().processVObjectEvents(this, newPhyValues, this.phyEngine.getPhysicsValues());
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in vObject thread! (VObject) · " + ex.getMessage());
            }
        }
    }


    @Override
    public String toString() {
        return "Body <" + this.getId()
                + "> p (" + this.phyEngine.getPhysicsValues().pos_x + "," + this.phyEngine.getPhysicsValues().pos_x + ") "
                + " s (" + this.phyEngine.getPhysicsValues().speed_x + "," + this.phyEngine.getPhysicsValues().speed_x + ")";
    }

}
