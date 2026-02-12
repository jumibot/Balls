package engine.utils.pooling;

// region Imports
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;
// endregion

/**
 * Generic object pool for reusing objects.
 * 
 * This pool manages poolable object instances to reduce allocation
 * pressure.
 * 
 * @param <T> the type of Object managed by this pool (must implement
 *            PoolableObject)
 */
public class Pool<T extends PoolableObject> {

    // region Fields
    private final Supplier<T> factory;
    private final ConcurrentLinkedDeque<T> pool = new ConcurrentLinkedDeque<>();
    // endregion Fields

    // region Constructors
    public Pool(Supplier<T> factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }

        this.factory = factory;
    }
    // endregion Constructors

    // *** PUBLICS ***

    public T acquire() {
        // Take one object from the pool
        T tObject = this.pool.pollFirst();

        if (tObject == null) {
            // pool is empty => create a new object
            tObject = this.factory.get();
            tObject.setPool(this);
        }
        return tObject;
    }

    public void clear() {
        this.pool.clear();
    }

    public int getPoolSize() {
        return this.pool.size();
    }

    public void preallocate(int count) {
        if (count <= 0)
            throw new IllegalArgumentException("Preallocation count must be positive");

        for (int i = 0; i < count; i++) {
            T tObject = this.factory.get();
            tObject.setPool(this);
            this.pool.addLast(tObject);
        }
    }

    public void release(T tObject) {
        if (tObject != null) {
            tObject.reset();
            this.pool.addLast(tObject);
        }
    }
}
