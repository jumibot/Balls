package engine.model.physics.ports;

import java.io.Serializable;

import engine.utils.pooling.Pool;
import engine.utils.pooling.PoolableObject;

/**
 * Mutable value object that encapsulates the physical state of a body at a
 * specific moment in time. It stores the timestamp of the state and the
 * kinematic vectors describing its position, speed and acceleration.
 * 
 * Its reusable from a pool.
 * 
 * It is mutable to support efficient updates.
 */
public class PhysicsValuesMDTO implements Serializable, PoolableObject {

    // region Fields

    // Referencia al pool que gestiona este DTO
    private Pool<PhysicsValuesMDTO> pool;

    public long timeStamp;
    public double posX;
    public double posY;
    public double angle;
    public double size;
    public double speedX, speedY;
    public double accX, accY; // Derived values. Every tick recalculateds
    public double angularSpeed;
    public double angularAcc;
    public double thrust;
    // endregion

    public PhysicsValuesMDTO(
            long timeStamp,
            double posX, double posY, double angle,
            double size,
            double speed_x, double speed_y,
            double acc_x, double acc_y,
            double angularSpeed, double angularAcc,
            double thrust) {

        this.timeStamp = timeStamp;

        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.size = size;
        this.speedX = speed_x;
        this.speedY = speed_y;
        this.accX = acc_x;
        this.accY = acc_y;
        this.angularSpeed = angularSpeed;
        this.angularAcc = angularAcc;
        this.thrust = thrust;

        // pool se asigna por PoolMDTO.acquire()
    }

    public PhysicsValuesMDTO(long timeStamp, double size, double x, double y, double angle) {
        this(
                timeStamp,
                x, y, angle,
                size,
                0.0, 0.0,
                0.0, 0.0,
                0.0, 0.0,
                0.0);
    }

    public void update(
            long timeStamp,
            double posX, double posY, double angle,
            double size,
            double speedX, double speedY,
            double accX, double accY,
            double angularSpeed, double angularAcc,
            double thrust) {

        this.timeStamp = timeStamp;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.size = size;
        this.speedX = speedX;
        this.speedY = speedY;
        this.accX = accX;
        this.accY = accY;
        this.angularSpeed = angularSpeed;
        this.angularAcc = angularAcc;
        this.thrust = thrust;
    }

    @Override
    public void reset() {
        this.timeStamp = 0L;
        this.posX = 0;
        this.posY = 0;
        this.angle = 0;
        this.size = 0;
        this.speedX = 0;
        this.speedY = 0;
        this.accX = 0;
        this.accY = 0;
        this.angularSpeed = 0;
        this.angularAcc = 0;
        this.thrust = 0;
    }

    @Override
    public void setPool(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void release() {
        if (this.pool != null) {
            this.pool.release(this);
        }
    }
}
