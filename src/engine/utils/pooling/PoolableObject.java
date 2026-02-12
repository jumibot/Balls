package engine.utils.pooling;

/**
 * Marker interface for MDTOs that can be pooled and reused.
 * Poolable Mutable MDTOs must implement reset() to clear their
 * state before returning to the pool.
 */
public interface PoolableObject {
    public void reset();

    public void release();

    public void setPool(Pool<? extends PoolableObject>  pool);
}
