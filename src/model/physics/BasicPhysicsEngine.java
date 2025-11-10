package model.physics;


import _helpers.DoubleVector;
import _helpers.Position;
import static java.lang.System.currentTimeMillis;


/**
 *
 * @author juanm
 *
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 *
 */
public class BasicPhysicsEngine implements PhysicsEngine {

    private PhysicsValuesDTO phyValues; // Convertir en referencia atómica


    /**
     * CONSTRUCTORS
     */
    public BasicPhysicsEngine(PhysicsValuesDTO phyValues) {
        this.phyValues = phyValues;
    }


    /**
     * PUBLICS
     */
    @Override
    public PhysicsValuesDTO calcNewPhysicsValues() {
        // Calculate thee time elapsed since the last displacement calculation
        long timeStamp = currentTimeMillis();
        long elapsedMillis = timeStamp - this.phyValues.position.timeStampInMillis;

        // Calculate the displacement due to object speed -> e = v*t
        DoubleVector offset = this.phyValues.speed.scale(elapsedMillis);

        // Apply the offset to calculate the new position of the object
        Position newPosition = this.phyValues.position.add(offset, timeStamp);

        // Calculate the velocity by appling the acceleration of the object -> v = v + a*t
        DoubleVector newSpeed
                = this.phyValues.speed.add(
                        this.phyValues.acceleration.scale(elapsedMillis));

        // Creating a new DTO and return it
        return new PhysicsValuesDTO(
                this.phyValues.mass,
                this.phyValues.maxModuleAcceleration,
                this.phyValues.maxModuleSpeed,
                newPosition,
                newSpeed,
                this.phyValues.acceleration);
    }


    public void doMovement(PhysicsValuesDTO phyValues) {
        this.setPhysicalValues(phyValues);
    }


    public PhysicsValuesDTO getPhysicalValues() {
        return this.phyValues;
    }


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


    public void reboundInNorth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues, DoubleVector worldDimension) {

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


    public void reboundInSouth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues, DoubleVector worldDimension) {

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


    /*
        PRIVATE
     */
    private void setPhysicalValues(PhysicsValuesDTO newPhyValues) {
        this.phyValues = newPhyValues;
    }

}
