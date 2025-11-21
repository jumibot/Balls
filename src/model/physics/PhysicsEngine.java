package model.physics;


import java.awt.Dimension;
import static java.lang.System.nanoTime;


/**
 *
 * @author juanm
 *
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 *
 */
public class PhysicsEngine extends AbstractPhysicsEngine implements PhysicsEngineInterface {

    /**
     * CONSTRUCTORS
     */
    public PhysicsEngine(PhysicsValues phyVals) {
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
    public void reboundInEast(
            PhysicsValues newPhyVals,
            PhysicsValues oldPhyVals,
            Dimension worldDimn) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = -newPhyVals.speedX;
        double speedY = newPhyVals.speedY;

        // New position: snapped to the east boundary (slightly inside)
        double posX = 0.1;
        double posY = newPhyVals.posY;

        // Acceleration is preserved
        double accX = newPhyVals.accX;
        double accY = newPhyVals.accY;

        PhysicsValues reboundPhyVals = new PhysicsValues(
                newPhyVals.timeStamp,
                posX, posY,
                speedX, speedY,
                accX, accY,
                oldPhyVals.angle
        );

        this.setPhysicsValues(reboundPhyVals);
    }


    @Override
    public void reboundInWest(
            PhysicsValues newPhyVals,
            PhysicsValues oldPhyVals,
            Dimension worldDim) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = -newPhyVals.speedX;
        double speedY = newPhyVals.speedY;

        // New position: snapped to the east boundary (slightly inside)
        double posX = worldDim.width - 0.0001;
        double posY = newPhyVals.posY;

        // Acceleration is preserved
        double accX = newPhyVals.accX;
        double accY = newPhyVals.accY;

        PhysicsValues reboundPhyVals = new PhysicsValues(
                newPhyVals.timeStamp,
                posX, posY,
                speedX, speedY,
                accX, accY,
                oldPhyVals.angle
        );

        this.setPhysicsValues(reboundPhyVals);
    }


    @Override
    public void reboundInNorth(
            PhysicsValues newPhyVals,
            PhysicsValues oldPhyVals,
            Dimension worldDim) {

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
                oldPhyVals.angle
        );

        this.setPhysicsValues(reboundPhyVals);
    }


    @Override
    public void reboundInSouth(
            PhysicsValues newPhyVals,
            PhysicsValues oldPhyVals,
            Dimension worldDim) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = newPhyVals.speedX;
        double speedY = -newPhyVals.speedY;

        // New position: snapped to the east boundary (slightly inside)
        double posX = newPhyVals.posX;
        double posY = worldDim.height - 0.0001;

        // Acceleration is preserved
        double accX = newPhyVals.accX;
        double accY = newPhyVals.accY;

        PhysicsValues reboundPhyVals = new PhysicsValues(
                newPhyVals.timeStamp,
                posX, posY,
                speedX, speedY,
                accX, accY,
                oldPhyVals.angle
        );

        this.setPhysicsValues(reboundPhyVals);
    }


    /**
     * PRIVATES
     */
    private PhysicsValues integrateMRUA(
            PhysicsValues phyVals,
            double dt) {

        // v1 = v0 + a*dt
        double speed0_x = phyVals.speedX;
        double speed0_y = phyVals.speedY;
        double acc_x = phyVals.accX;
        double acc_y = phyVals.accY;

        double speed1_x = speed0_x + acc_x * dt;
        double speed1_y = speed0_y + acc_y * dt;

        // v_avg = (v0 + v1) / 2
        double vavg_x = (speed0_x + speed1_x) * 0.5;
        double vavg_y = (speed0_y + speed1_y) * 0.5;

        // x1 = x0 + v_avg * dt
        double x0 = phyVals.posX;
        double y0 = phyVals.posY;

        double pos1_x = x0 + vavg_x * dt;
        double pos1_y = y0 + vavg_y * dt;

        long t1 = phyVals.timeStamp + (long) (dt * 1_000_000_000.0);

        return new PhysicsValues(
                t1,
                pos1_x, pos1_y,
                speed1_x, speed1_y,
                phyVals.accX, phyVals.accY,
                phyVals.angle
        );
    }
}
