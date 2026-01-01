package model.bodies;

import java.util.UUID;

import model.Model;
import model.physics.PhysicsValuesDTO;
import model.physics.ports.PhysicsEngine;

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
    private final long bornTime = System.nanoTime();
    private final double maxLifeInSeconds; // Infinite life by default

    /**
     * CONSTRUCTORS
     */
    public AbstractBody(PhysicsEngine phyEngine) {
        this(phyEngine, -1D);
    }

    public AbstractBody(PhysicsEngine phyEngine, double maxLifeInSeconds) {
        this.entityId = UUID.randomUUID().toString();

        this.phyEngine = phyEngine;
        this.state = BodyState.STARTING;
        this.maxLifeInSeconds = maxLifeInSeconds;
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

    public long getBornTime() {
        return this.bornTime;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public double getLifeInSeconds() {
        return (System.nanoTime() - this.bornTime) / 1_000_000_000.0D;
    }

    public boolean isLifeOver() {
        if (this.maxLifeInSeconds <= 0) {
            return false;
        }

        return this.getLifeInSeconds() >= this.maxLifeInSeconds;
    }

    public double getMaxLife() {
        return this.maxLifeInSeconds;
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
