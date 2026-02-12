package engine.view.renderables.ports;

import engine.utils.pooling.PoolableObject;

import engine.utils.pooling.Pool;

public class RenderDTO implements PoolableObject {

    // region Fields
    public String entityId;
    public double posX;
    public double posY;
    public double angle;
    public double size;
    public long timestamp;

    // Referencia al pool para autoliberación
    protected transient Pool<? extends PoolableObject> pool;
    // endregion

    // region Constructors
    public RenderDTO(
            String entityId, double posX, double posY, double angle, double size, long timestamp) {

        this.entityId = entityId;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.size = size;
        this.timestamp = timestamp;
    }
    // endregion

    // *** PUBLIC ***

    public void setPool(Pool<? extends PoolableObject> pool) {
        this.pool = pool;
    }

    public void updateBase(
            String entityId, double posX, double posY, double angle, double size, long timestamp) {
        this.entityId = entityId;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.size = size;
        this.timestamp = timestamp;
    }

    // *** INTERFACE IMPLEMENTATIONS **

    @Override
    public void release() {
        if (this.pool == null)
            throw new IllegalStateException("RenderDTO: No pool set for release()");

        ((Pool<PoolableObject>) this.pool).release(this);
    }

    @Override
    public void reset() {
        this.entityId = null;
        this.posX = 0.0;
        this.posY = 0.0;
        this.angle = 0.0;
        this.size = 0.0;
        this.timestamp = 0L;
    }

}
