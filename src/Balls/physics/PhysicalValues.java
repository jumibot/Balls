package Balls.physics;


import Helpers.DoubleVector;
import java.io.Serializable;


public class PhysicalValues implements Serializable {

    public double mass;
    public double maxModuleAcceleration;
    public double maxModuleDeceleration;
    public double maxModuleSpeed;
    public DoubleVector acceleration; // speed per milliseconds
    public DoubleVector speed;        // pixels per milliseconds


    public PhysicalValues() {
        this.mass = 0;
        this.maxModuleAcceleration = 0;
        this.maxModuleDeceleration = 0;
        this.maxModuleSpeed = 0;

        this.acceleration = new DoubleVector(0, 0);
        this.speed = new DoubleVector(0, 0);
    }


    public void cloneAcceleration(DoubleVector dVector) {
        this.acceleration.set(dVector);
    }


    public void cloneSpeed(DoubleVector dVector) {
        this.speed.set(dVector);
    }

}
