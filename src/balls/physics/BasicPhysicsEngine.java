package balls.physics;


import helpers.DoubleVector;
import helpers.Position;
import static java.lang.System.currentTimeMillis;


/**
 *
 * @author juanm
 *
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 *
 */
public class BasicPhysicsEngine implements PhysicsEngine {

    private static boolean debugMode = false;

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


    public DoubleVector getCoordinates() {
        return new DoubleVector(this.phyValues.position);
    }


    public PhysicsValuesDTO getPhysicalValues() {
        return this.phyValues;
    }


    public void setPhysicalValues(PhysicsValuesDTO newPhyValues) {
        this.phyValues = newPhyValues;
    }
}
