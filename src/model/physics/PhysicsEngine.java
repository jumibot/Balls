package model.physics;


import _helpers.DoubleVector;
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
    public PhysicsEngine(PhysicsValuesDTO phyValues) {
        super(phyValues);
    }


    /**
     * PUBLICS
     *
     * @return
     */
    @Override
    public PhysicsValuesDTO calcNewPhysicsValues() {
        PhysicsValuesDTO phyValues = this.getPhysicalValues();
        long now = nanoTime();
        long elapsedNanos = now - phyValues.timeStamp;

        if (elapsedNanos <= 0) {
            return phyValues;
        }

        // Converting nanos to seconds
        double dt = elapsedNanos / 1_000_000_000.0;
        return integrateMRUA(phyValues, dt, true);
    }


    @Override
    public void reboundInEast(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension) {

        DoubleVector newSpeed
                = new DoubleVector(-newPhyValues.speed.x, newPhyValues.speed.y);

        DoubleVector newPosition = new DoubleVector(0.1, newPhyValues.position.y);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.timeStamp,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicsValues(reboundPhyValues);
    }


    @Override
    public void reboundInWest(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        DoubleVector newSpeed = new DoubleVector(-newPhyValues.speed.x, newPhyValues.speed.y);

        DoubleVector newPosition = new DoubleVector(worldDim.width - 0.1, newPhyValues.position.y);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.timeStamp,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicsValues(reboundPhyValues);
    }


    @Override
    public void reboundInNorth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension) {

        DoubleVector newSpeed = new DoubleVector(newPhyValues.speed.x, -newPhyValues.speed.y);

        DoubleVector newPosition = new DoubleVector(newPhyValues.position.x, 0.1);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.timeStamp,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicsValues(reboundPhyValues);
    }


    @Override
    public void reboundInSouth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        DoubleVector newSpeed = new DoubleVector(newPhyValues.speed.x, -newPhyValues.speed.y);

        DoubleVector newPosition = new DoubleVector(newPhyValues.position.x, worldDim.height - 0.1);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.timeStamp,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicsValues(reboundPhyValues);
    }


    /**
     * PRIVATES
     */
    private PhysicsValuesDTO integrateMRUA(
            PhysicsValuesDTO state,
            double dt,
            boolean clampSpeed) {

        DoubleVector v0 = state.speed;
        DoubleVector a = state.acceleration;

        // Final speed: v1 = v0 + a*dt
        DoubleVector v1 = v0.addScaled(a, dt);

        // Medium speed: v_avg = (v0 + v1) / 2
        DoubleVector v_avg = v0.add(v1).scale(0.5);

        // Final position: x1 = x0 + v_avg * dt
        DoubleVector x1 = state.position.addScaled(v_avg, dt);

        // Final time stamp
        long t1 = state.timeStamp + (long) (dt * 1_000_000_000.0);

        return new PhysicsValuesDTO(
                t1, x1, v1,
                state.acceleration
        );
    }
}
