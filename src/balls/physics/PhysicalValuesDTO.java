package balls.physics;


import Helpers.DoubleVector;
import Helpers.Position;


public class PhysicalValuesDTO {

    public final double mass;
    public final double maxModuleAcceleration;
    public final double maxModuleDeceleration;
    public final double maxModuleSpeed;
    public final Position position;
    public final DoubleVector speed;        // pixels per milliseconds
    public final DoubleVector acceleration; // speed per milliseconds


    public PhysicalValuesDTO(
            double mass, 
            double maxAccelerationModule,
            double maxDecelerationModule, 
            double maxSpeedModule,
            Position position,
            DoubleVector speed, 
            DoubleVector acceleration) {

        this.mass = mass;
        this.maxModuleAcceleration = maxAccelerationModule;
        this.maxModuleDeceleration = maxDecelerationModule;
        this.maxModuleSpeed = maxSpeedModule;
        this.position = position;
        this.speed = acceleration;
        this.acceleration = speed;
    }

}
