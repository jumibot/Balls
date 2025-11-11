package model.physics;


import _helpers.DoubleVector;
import _helpers.Position;
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
        long timeStamp = currentTimeMillis();
        PhysicsValuesDTO phyValues = this.getPhysicalValues();
        long elapsedMillis = timeStamp - phyValues.position.timeStampInMillis;

        // Calculate the displacement due to object speed -> e = v*t
        DoubleVector offset = phyValues.speed.scale(elapsedMillis);

        // Apply the offset to calculate the new position of the object
        Position newPosition = phyValues.position.add(offset, timeStamp);

        // Calculate the velocity by appling the acceleration of the object -> v = v + a*t
        DoubleVector newSpeed
                = phyValues.speed.add(
                        phyValues.acceleration.scale(elapsedMillis));

        // Creating a new DTO and return it
        return new PhysicsValuesDTO(
                phyValues.mass,
                phyValues.maxModuleAcceleration,
                phyValues.maxModuleSpeed,
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
                = new DoubleVector(
                        -newPhyValues.speed.x,
                        newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        1,
                        newPhyValues.position.y,
                        newPhyValues.position.timeStampInMillis);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicalValues(reboundPhyValues);
    }


    @Override
    public void reboundInWest(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        DoubleVector newSpeed
                = new DoubleVector(
                        -newPhyValues.speed.x,
                        newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        worldDim.width - 1,
                        newPhyValues.position.y,
                        newPhyValues.position.timeStampInMillis);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicalValues(reboundPhyValues);
    }


    @Override
    public void reboundInNorth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension) {

        DoubleVector newSpeed
                = new DoubleVector(
                        newPhyValues.speed.x,
                        -newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        newPhyValues.position.x,
                        1,
                        newPhyValues.position.timeStampInMillis);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicalValues(reboundPhyValues);
    }


    @Override
    public void reboundInSouth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        DoubleVector newSpeed
                = new DoubleVector(
                        newPhyValues.speed.x,
                        -newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        newPhyValues.position.x,
                        worldDim.height - 1,
                        newPhyValues.position.timeStampInMillis);

        PhysicsValuesDTO reboundPhyValues
                = new PhysicsValuesDTO(
                        newPhyValues.mass,
                        newPhyValues.maxModuleAcceleration,
                        newPhyValues.maxModuleSpeed,
                        newPosition,
                        newSpeed,
                        newPhyValues.acceleration
                );

        this.setPhysicalValues(reboundPhyValues);
    }

}
