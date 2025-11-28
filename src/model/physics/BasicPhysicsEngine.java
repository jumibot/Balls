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
        double speedX = -newPhyVals.speed_x;
        double speedY = newPhyVals.speed_y;

        // New position: snapped to the east boundary (slightly inside)
        double posX = 0.0001;
        double posY = newPhyVals.pos_y;

        // Acceleration is preserved
        double accX = newPhyVals.acc_x;
        double accY = newPhyVals.acc_y;

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
    public void reboundInWest(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = -newPhyVals.speed_x;
        double speedY = newPhyVals.speed_y;

        // New position: snapped to the east boundary (slightly inside)
        double posX = worldDim_x - 0.0001;
        double posY = newPhyVals.pos_y;

        // Acceleration is preserved
        double accX = newPhyVals.acc_x;
        double accY = newPhyVals.acc_y;

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
    public void reboundInNorth(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = newPhyVals.speed_x;
        double speedY = -newPhyVals.speed_y;

        // New position: snapped to the east boundary (slightly inside)
        double posX = newPhyVals.pos_x;
        double posY = 0.0001;

        // Acceleration is preserved
        double accX = newPhyVals.acc_x;
        double accY = newPhyVals.acc_y;

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
    public void reboundInSouth(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {

        // New speed: horizontal component flipped, vertical preserved
        double speedX = newPhyVals.speed_x;
        double speedY = -newPhyVals.speed_y;

        // New position: snapped to the east boundary (slightly inside)
        double posX = newPhyVals.pos_x;
        double posY = worldDim_y - 0.0001;

        // Acceleration is preserved
        double accX = newPhyVals.acc_x;
        double accY = newPhyVals.acc_y;

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
    private PhysicsValues integrateMRUA(PhysicsValues phyVals, double dt) {

        // v1 = v0 + a*dt
        double speed0_x = phyVals.speed_x;
        double speed0_y = phyVals.speed_y;
        double acc_x = phyVals.acc_x;
        double acc_y = phyVals.acc_y;

        double speed1_x = speed0_x + acc_x * dt;
        double speed1_y = speed0_y + acc_y * dt;

        // v_avg = (v0 + v1) / 2
        double vavg_x = (speed0_x + speed1_x) * 0.5;
        double vavg_y = (speed0_y + speed1_y) * 0.5;

        // x1 = x0 + v_avg * dt
        double x0 = phyVals.pos_x;
        double y0 = phyVals.pos_y;

        double pos1_x = x0 + vavg_x * dt;
        double pos1_y = y0 + vavg_y * dt;

        long t1 = phyVals.timeStamp + (long) (dt * 1_000_000_000.0d);

        return new PhysicsValues(
                t1, pos1_x, pos1_y, speed1_x, speed1_y,
                phyVals.acc_x, phyVals.acc_y, phyVals.angle
        );
    }
}
