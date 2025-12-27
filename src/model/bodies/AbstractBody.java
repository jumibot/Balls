package model.bodies;

import java.util.UUID;

import model.Model;
import model.physics.PhysicsEngine;
import model.physics.PhysicsValuesDTO;

/**
 *
 * @author juanm
 */
public abstract class AbstractBody {

    /* TO-DO: Replace individual static counters by one static array of counters */
    private static volatile int aliveQuantity = 0;
    private static volatile int createdQuantity = 0;
    private static volatile int deadQuantity = 0;

    private Model model = null;
    private volatile BodyState state;
    private final String entityId;
    private final PhysicsEngine phyEngine;

    /**
     * CONSTRUCTORS
     */
    public AbstractBody(PhysicsEngine phyEngine) {
        this.entityId = UUID.randomUUID().toString();

        this.phyEngine = phyEngine;
        this.state = BodyState.STARTING;
    }

    public synchronized void activate() {
        if (this.model == null) {
            throw new IllegalArgumentException("Model not setted");
        }

        if (!this.model.isAlive()) {
            throw new IllegalArgumentException("Entity activation error due MODEL is not alive!");
        }

        if (this.state != BodyState.STARTING) {
            throw new IllegalArgumentException("Entity activation error due is not starting!");
        }

        AbstractBody.aliveQuantity++;
        this.state = BodyState.ALIVE;
    }

    public synchronized void die() {
        this.state = BodyState.DEAD;
        AbstractBody.deadQuantity++;
        AbstractBody.aliveQuantity--;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public Model getModel() {
        return this.model;
    }

    public PhysicsValuesDTO getPhysicsValues() {
        return this.phyEngine.getPhysicsValues();
    }

    public BodyState getState() {
        return this.state;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setState(BodyState state) {
        this.state = state;
    }

    /**
     * STATICS
     */
    static public int getCreatedQuantity() {
        return AbstractBody.createdQuantity;
    }

    static public int getAliveQuantity() {
        return AbstractBody.aliveQuantity;
    }

    static public int getDeadQuantity() {
        return AbstractBody.deadQuantity;
    }

    static protected int incCreatedQuantity() {
        AbstractBody.createdQuantity++;

        return AbstractBody.createdQuantity;
    }

    static protected int incAliveQuantity() {
        AbstractBody.aliveQuantity++;

        return AbstractBody.aliveQuantity;
    }

    static protected int decAliveQuantity() {
        AbstractBody.aliveQuantity--;

        return AbstractBody.aliveQuantity;
    }

    static protected int incDeadQuantity() {
        AbstractBody.deadQuantity++;

        return AbstractBody.deadQuantity;
    }
}
