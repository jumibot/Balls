package model.physics;


import _helpers.DoubleVector;
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
    public final double posX;
    public final double posY;
    public final double speedX;
    public final double speedY;
    public final double accX;
    public final double accY;
    public final double angle;


    public PhysicsValues(
            long timeStamp,
            double pos_x, double pos_y,
            double speed_x, double speed_y, 
            double acc_x, double acc_y,
            double angle
    ) {
        this.timeStamp = timeStamp;

        this.posX = pos_x;
        this.posY = pos_y;

        this.speedX = speed_x;
        this.speedY = speed_y;

        this.accX = acc_x;
        this.accY = acc_y;

        this.angle = angle;
    }

}
