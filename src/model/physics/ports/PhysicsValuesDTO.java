package model.physics.ports;

import java.io.Serializable;

/**
 * Immutable value object that encapsulates the physical state of a VObject at a
 * specific moment in time. It stores the timestamp of the state and the
 * kinematic vectors describing its position, speed and acceleration.
 *
 * This object belongs strictly to the physics domain and contains no logic
 * beyond data representation. It is used by the simulation model to track and
 * propagate physical values without exposing mutable state.
 */
public class PhysicsValuesDTO implements Serializable {

    public final long timeStamp;
    public final double posX;
    public final double posY;
    public final double angle;
    public final double size;
    public final double speedX, speedY;
    public final double accX, accY;
    public final double angularSpeed;
    public final double angularAcc;
    public final double thrust;

    public PhysicsValuesDTO(
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

    }

    public PhysicsValuesDTO(double size, double x, double y, double angle) {
        this(
                System.currentTimeMillis(),
                x, y, angle,
                size,
                0.0, 0.0,
                0.0, 0.0,
                0.0, 0.0,
                0.0);
    }

}
