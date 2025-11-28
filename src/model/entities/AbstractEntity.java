package model.entities;


import java.awt.Color;
import model.Model;
import model.physics.PhysicsValues;
import view.DBodyRenderInfoDTO;


/**
 *
 * @author juanm
 */
public abstract class AbstractEntity {

    /* TO-DO: Replace individual static counters by one static array of counters */
    private static volatile int aliveQuantity = 0;
    private static volatile int createdQuantity = 0;
    private static volatile int deadQuantity = 0;

    private Model model = null;
    private volatile EntityState state;

    private final int id;
    public final String assetId;
    public final int size;
    private Color color;


    public AbstractEntity(String assetId, int size) {
        this.id = AbstractEntity.createdQuantity++;
        this.assetId = assetId;
        this.size = size;
        this.color = Color.BLUE;

        this.state = EntityState.STARTING;
    }


    public synchronized void activate() {
        if (this.model == null) {
            throw new IllegalArgumentException("Model not setted");
        }

        if (!this.model.isAlive()) {
            throw new IllegalArgumentException("Entity activation error due MODEL is not alive!");
        }

        if (this.state != EntityState.STARTING) {
            throw new IllegalArgumentException("Entity activation error due is not starting!");
        }

        AbstractEntity.aliveQuantity++;
        this.state = EntityState.ALIVE;
    }


    public abstract DBodyRenderInfoDTO buildRenderInfo();


    public synchronized void die() {
        if (this.state == EntityState.ALIVE) {
            this.state = EntityState.DEAD;
            AbstractEntity.deadQuantity++;
            AbstractEntity.aliveQuantity--;
        }
    }


    public int getId() {
        return this.id;
    }


    public Color getColor() {
        return this.color;
    }


    public Model getModel() {
        return this.model;
    }


    public EntityState getState() {
        return this.state;
    }


    public void setModel(Model model) {
        this.model = model;
    }


    public void setState(EntityState state) {
        this.state = state;
    }


    /**
     * STATICS
     */
    static public int getCreatedQuantity() {
        return AbstractEntity.createdQuantity;
    }


    static public int getAliveQuantity() {
        return AbstractEntity.aliveQuantity;
    }


    static public int getDeadQuantity() {
        return AbstractEntity.deadQuantity;
    }


    static protected int incCreatedQuantity() {
        AbstractEntity.createdQuantity++;

        return AbstractEntity.createdQuantity;
    }


    static protected int incAliveQuantity() {
        AbstractEntity.aliveQuantity++;

        return AbstractEntity.aliveQuantity;
    }


    static protected int decAliveQuantity() {
        AbstractEntity.aliveQuantity--;

        return AbstractEntity.aliveQuantity;
    }


    static protected int incDeadQuantity() {
        AbstractEntity.deadQuantity++;

        return AbstractEntity.deadQuantity;
    }
}
