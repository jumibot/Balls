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


    public void reboundInEast(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = -newPhyVals.speedX;
        double speedY = newPhyVals.speedY;

        // New position: snapped to the east boundary (slightly inside)
        double posX = 0.0001;
        double posY = newPhyVals.posY;

        // Acceleration is preserved
        double accX = newPhyVals.accX;
        double accY = newPhyVals.accY;

        PhysicsValues reboundPhyVals = new PhysicsValues(
                newPhyVals.timeStamp,
                posX, posY,
                speedX, speedY,
                accX, accY,
                oldPhyVals.angle,
                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                oldPhyVals.thrust);

        this.setPhysicsValues(reboundPhyVals);
    }


    public void reboundInWest(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = -newPhyVals.speedX;
        double speedY = newPhyVals.speedY;

        // New position: snapped to the east boundary (slightly inside)
        double posX = worldDim_x - 0.0001;
        double posY = newPhyVals.posY;

        // Acceleration is preserved
        double accX = newPhyVals.accX;
        double accY = newPhyVals.accY;

        PhysicsValues reboundPhyVals = new PhysicsValues(
                newPhyVals.timeStamp,
                posX, posY,
                speedX, speedY,
                accX, accY,
                oldPhyVals.angle,
                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                oldPhyVals.thrust);

        this.setPhysicsValues(reboundPhyVals);
    }


    public void reboundInNorth(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = newPhyVals.speedX;
        double speedY = -newPhyVals.speedY;

        // New position: snapped to the east boundary (slightly inside)
        double posX = newPhyVals.posX;
        double posY = 0.0001;

        // Acceleration is preserved
        double accX = newPhyVals.accX;
        double accY = newPhyVals.accY;

        PhysicsValues reboundPhyVals = new PhysicsValues(
                newPhyVals.timeStamp,
                posX, posY,
                speedX, speedY,
                accX, accY,
                oldPhyVals.angle,
                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                oldPhyVals.thrust);

        this.setPhysicsValues(reboundPhyVals);
    }


    public void reboundInSouth(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = newPhyVals.speedX;
        double speedY = -newPhyVals.speedY;

        // New position: snapped to the east boundary (slightly inside)
        double posX = newPhyVals.posX;
        double posY = worldDim_y - 0.0001;

        // Acceleration is preserved
        double accX = newPhyVals.accX;
        double accY = newPhyVals.accY;

        PhysicsValues reboundPhyVals = new PhysicsValues(
                newPhyVals.timeStamp,
                posX, posY,
                speedX, speedY,
                accX, accY,
                oldPhyVals.angle,
                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                oldPhyVals.thrust);

        this.setPhysicsValues(reboundPhyVals);
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
