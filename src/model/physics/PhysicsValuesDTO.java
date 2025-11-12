package model.physics;


import _helpers.DoubleVector;
import java.io.Serializable;


public class PhysicsValuesDTO implements Serializable {

    public final double mass;
    public final double maxModuleAcceleration;
    public final double maxModuleSpeed;
    public final long timeStamp;
    public final DoubleVector position;
    public final DoubleVector speed;        // pixels per milliseconds
    public final DoubleVector acceleration; // pixels per milliseconds^2


    public PhysicsValuesDTO(
            double mass,
            double maxAccelerationModule,
            double maxSpeedModule,
            long timeStamp,
            DoubleVector position,
            DoubleVector speed,
            DoubleVector acceleration) {

        this.mass = mass;
        this.maxModuleAcceleration = maxAccelerationModule;
        this.maxModuleSpeed = maxSpeedModule;
        this.timeStamp = timeStamp;
        this.position = position;
        this.speed = speed;
        this.acceleration = acceleration;
    }

}
