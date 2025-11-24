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
    public final double pos_x;
    public final double pos_y;
    public final double speed_x;
    public final double speed_y;
    public final double acc_x;
    public final double acc_y;
    public final double angle;


    public PhysicsValues(long timeStamp,
            double pos_x, double pos_y,
            double speed_x, double speed_y,
            double acc_x, double acc_y,
            double angle
    ) {
        this.timeStamp = timeStamp;

        this.pos_x = pos_x;
        this.pos_y = pos_y;

        this.speed_x = speed_x;
        this.speed_y = speed_y;

        this.acc_x = acc_x;
        this.acc_y = acc_y;

        this.angle = angle;
    }
}
