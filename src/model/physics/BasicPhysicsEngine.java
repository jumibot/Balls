package model.physics;


import _helpers.DoubleVector;
import java.awt.Dimension;
import static java.lang.System.currentTimeMillis;


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
    public BasicPhysicsEngine(PhysicsValuesDTO phyValues) {
        super(phyValues);
    }


    /**
     * PUBLICS
     *
     * @return
     */
    @Override
    public PhysicsValuesDTO calcNewPhysicsValues() {
        // Calculate thee time elapsed since the last displacement calculation
        long now = currentTimeMillis();
        PhysicsValuesDTO phyValues = this.getPhysicalValues();
        long elapsedMillis = now - phyValues.timeStamp;

        // v(t+dt) = v + a*dt
        DoubleVector newSpeed = phyValues.speed.addScaled(phyValues.acceleration, elapsedMillis);

        // x(t+dt) = x + v(t+dt)*dt
        DoubleVector newPosition = phyValues.position.addScaled(phyValues.speed, elapsedMillis);

        // Creating a new DTO and return it
        return new PhysicsValuesDTO(
                phyValues.mass,
                phyValues.maxModuleAcceleration,
                phyValues.maxModuleSpeed,
                now,
                newPosition,
                newSpeed,
                phyValues.acceleration);
    }


    @Override
    public void reboundInEast(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension) {

        DoubleVector newSpeed
                = new DoubleVector(-newPhyValues.speed.x, newPhyValues.speed.y);

        DoubleVector newPosition = new DoubleVector(1, newPhyValues.position.y);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
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

        DoubleVector newPosition = new DoubleVector(worldDim.width - 1, newPhyValues.position.y);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
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

        DoubleVector newPosition = new DoubleVector(newPhyValues.position.x, 1);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
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

        DoubleVector newPosition = new DoubleVector(newPhyValues.position.x, worldDim.height - 1);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
                        newPhyValues.timeStamp,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicsValues(reboundPhyValues);
    }

}
