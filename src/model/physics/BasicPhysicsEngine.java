package model.physics;


import _helpers.DoubleVector;
import _helpers.Position;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.atomic.AtomicReference;


/**
 *
 * @author juanm
 *
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 *
 */
public class BasicPhysicsEngine extends AbstractPhysicsEngine implements PhysicsEngine {

    private AtomicReference<PhysicsValuesDTO> phyValues; // *+


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
        long elapsedMillis = timeStamp - this.phyValues.get().position.timeStampInMillis;

        // Calculate the displacement due to object speed -> e = v*t
        DoubleVector offset = this.phyValues.get().speed.scale(elapsedMillis);

        // Apply the offset to calculate the new position of the object
        Position newPosition = this.phyValues.get().position.add(offset, timeStamp);

        // Calculate the velocity by appling the acceleration of the object -> v = v + a*t
        DoubleVector newSpeed
                = this.phyValues.get().speed.add(
                        this.phyValues.get().acceleration.scale(elapsedMillis));

        // Creating a new DTO and return it
        return new PhysicsValuesDTO(
                this.phyValues.get().mass,
                this.phyValues.get().maxModuleAcceleration,
                this.phyValues.get().maxModuleSpeed,
                newPosition,
                newSpeed,
                this.phyValues.get().acceleration);
    }


    @Override
    public void reboundInEast(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            DoubleVector worldDimension) {

        DoubleVector newSpeed
                = new DoubleVector(
                        -newPhyValues.speed.x,
                        newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        0.5,
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
            DoubleVector worldDimension) {

        DoubleVector newSpeed
                = new DoubleVector(
                        -newPhyValues.speed.x,
                        newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        worldDimension.x - 0.5,
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
            DoubleVector worldDimension) {

        DoubleVector newSpeed
                = new DoubleVector(
                        newPhyValues.speed.x,
                        -newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        newPhyValues.position.x,
                        0.5,
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
            DoubleVector worldDimension) {

        DoubleVector newSpeed
                = new DoubleVector(
                        newPhyValues.speed.x,
                        -newPhyValues.speed.y);

        Position newPosition
                = new Position(
                        newPhyValues.position.x,
                        worldDimension.y - 0.5,
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
