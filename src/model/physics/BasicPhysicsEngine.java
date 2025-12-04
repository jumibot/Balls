package model.physics;


import static java.lang.System.nanoTime;


/**
 *
 * @author juanm
 *
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 *
 */
public class BasicPhysicsEngine extends AbstractPhysicsEngine implements PhysicsEngine {

    /**
     * CONSTRUCTORS
     */
    public BasicPhysicsEngine(PhysicsValues phyVals) {
        super(phyVals);
    }


    /**
     * PUBLICS
     *
     * @return
     */
    @Override
    public PhysicsValues calcNewPhysicsValues() {
        PhysicsValues phyVals = this.getPhysicsValues();
        long now = nanoTime();
        long elapsedNanos = now - phyVals.timeStamp;

        double dt = elapsedNanos / 1_000_000_000.0; // Nanos to seconds
        return integrateMRUA(phyVals, dt);
    }


    @Override
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


    @Override
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


    @Override
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


    @Override
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


    /**
     * PRIVATES
     */
    private PhysicsValues integrateMRUA(PhysicsValues phyVals, double dt) {
        // Applying thrust according actual angle
        double newAccX = phyVals.accX;
        double newAccY = phyVals.accY;
        double angleRad = Math.toRadians(phyVals.angle);
        if (phyVals.thrust != 0.0d) {
            newAccX += Math.cos(angleRad) * phyVals.thrust;
            newAccY += Math.sin(angleRad) * phyVals.thrust;
        }

        // v1 = v0 + a*dt
        double oldSpeedX = phyVals.speedX;
        double oldSpeedY = phyVals.speedY;
        double newSpeedX = oldSpeedX + newAccX * dt;
        double newSpeedY = oldSpeedY + newAccY * dt;

        // v_avg = (v0 + v1) / 2
        double avgSpeedX = (oldSpeedX + newSpeedX) * 0.5;
        double avgSpeedY = (oldSpeedY + newSpeedY) * 0.5;

        // x1 = x0 + v_avg * dt
        double newPosX = phyVals.posX + avgSpeedX * dt;
        double newPosY = phyVals.posY + avgSpeedY * dt;

        // w1 = w0 + α*dt
        double newAngularSpeed = phyVals.angularSpeed + phyVals.angularAcc * dt;

        // θ1 = θ0 + w0*dt + 0.5*α*dt^2
        double newAngle = (phyVals.angle
                + phyVals.angularSpeed * dt
                + 0.5d * newAngularSpeed * dt * dt) % 360;

        long newTimeStamp = phyVals.timeStamp + (long) (dt * 1_000_000_000.0d);

        return new PhysicsValues(
                newTimeStamp,
                newPosX, newPosY,
                newSpeedX, newSpeedY,
                newAccX, newAccY,
                newAngle,
                newAngularSpeed,
                phyVals.angularAcc, // keep same angular acc
                phyVals.thrust // keep same thrust
        );
    }
}
