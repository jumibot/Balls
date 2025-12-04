package model.physics;


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
public class PhysicsValues implements Serializable {

    public final long timeStamp;
    public final double posX, posY;
    public final double speedX, speedY;
    public final double accX, accY;

    public final double angle;
    public final double angularSpeed;
    public final double angularAcc;

    public final double thrust;


    public PhysicsValues(long timeStamp,
            double pos_x, double pos_y,
            double speed_x, double speed_y,
            double acc_x, double acc_y,
            double angle, double angularSpeed, double angularAcc,
            double thrust) {
        
        this.timeStamp = timeStamp;
        this.posX = pos_x;
        this.posY = pos_y;
        this.speedX = speed_x;
        this.speedY = speed_y;
        this.accX = acc_x;
        this.accY = acc_y;
        this.angle = angle;
        this.angularSpeed = angularSpeed;
        this.angularAcc = angularAcc;
        this.thrust = thrust;
    }
}
