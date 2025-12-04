package model.physics;


import java.util.concurrent.atomic.AtomicReference;


public abstract class AbstractPhysicsEngine {

    private final AtomicReference<PhysicsValues> phyValues; // *+


    /**
     * CONSTRUCTORS
     */
    public AbstractPhysicsEngine(PhysicsValues phyValues) {
        this.phyValues = new AtomicReference(phyValues);
    }


    /**
     * PUBLIC
     */
    public void addAngularAcceleration(double angularAcc) {
        PhysicsValues old = this.getPhysicsValues();
        this.setPhysicsValues(new PhysicsValues(
                old.timeStamp,
                old.posX, old.posY,
                old.speedX, old.speedY,
                old.accX, old.accY,
                old.angle,
                old.angularSpeed,
                old.angularAcc + angularAcc,
                old.thrust
        ));
    }


    public double getAngularSpeed() {
        PhysicsValues phyValues = this.phyValues.get();

        return phyValues.angularSpeed;
    }


    public PhysicsValues getPhysicsValues() {
        return this.phyValues.get();
    }


    public void resetAcceleration() {
        PhysicsValues old = this.getPhysicsValues();
        this.setPhysicsValues(new PhysicsValues(
                old.timeStamp,
                old.posX, old.posY,
                old.speedX, old.speedY,
                0, 0,
                old.angle,
                old.angularSpeed,
                old.angularAcc,
                old.thrust
        ));

    }


    public void setAngularAcceleration(double angularAcc) {
        PhysicsValues old = this.getPhysicsValues();
        this.setPhysicsValues(new PhysicsValues(
                old.timeStamp,
                old.posX, old.posY,
                old.speedX, old.speedY,
                old.accX, old.accY,
                old.angle,
                old.angularSpeed,
                angularAcc,
                old.thrust
        ));
    }


    public void setAngularSpeed(double angularSpeed) {
        PhysicsValues old = this.getPhysicsValues();
        this.setPhysicsValues(new PhysicsValues(
                old.timeStamp,
                old.posX, old.posY,
                old.speedX, old.speedY,
                old.accX, old.accY,
                old.angle,
                angularSpeed,
                old.angularAcc,
                old.thrust
        ));
    }


    public void setPhysicsValues(PhysicsValues phyValues) {
        this.phyValues.set(phyValues);
    }


    public void setThrust(double thrust) {
        PhysicsValues old = this.getPhysicsValues();
        this.setPhysicsValues(new PhysicsValues(
                old.timeStamp,
                old.posX, old.posY,
                old.speedX, old.speedY,
                old.accX, old.accY,
                old.angle,
                old.angularSpeed,
                old.angularAcc,
                thrust
        ));
    }
}
