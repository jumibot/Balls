package model.physics;


import _helpers.DoubleVector;
import java.io.Serializable;


public class PhysicsValuesDTO implements Serializable {

    public final long timeStamp;
    public final DoubleVector position;
    public final DoubleVector speed;        // pixels per milliseconds
    public final DoubleVector acceleration; // pixels per milliseconds^2


    public PhysicsValuesDTO(
            long timeStamp,
            DoubleVector position,
            DoubleVector speed,
            DoubleVector acceleration) {

        this.timeStamp = timeStamp;
        this.position = position;
        this.speed = speed;
        this.acceleration = acceleration;
    }

}
