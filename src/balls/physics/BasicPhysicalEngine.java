package balls.physics;


import Helpers.DoubleVector;
import Helpers.Position;
import static java.lang.System.currentTimeMillis;


/**
 *
 * @author juanm
 *
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 *
 */
public class BasicPhysicalEngine {

    private static boolean debugMode = false;

    private PhysicalValuesDTO phyValues; // Convertir en referencia atómica


    /**
     * CONSTRUCTORS
     */
    public BasicPhysicalEngine(PhysicalValuesDTO phyValues) {
        this.phyValues = phyValues;
    }


    /**
     * PUBLICS
     */
    public PhysicalValuesDTO calcNewPhysicalValues() {
        // Calculate thee time elapsed since the last displacement calculation
        long timeStamp = currentTimeMillis();
        long elapsedMillis = timeStamp - this.phyValues.position.getTimeStamp();

        // Calculate the displacement due to object speed -> e = v*t
        DoubleVector offset = this.phyValues.speed.scale(elapsedMillis);

        // Apply the offset to calculate the new position of the object
        Position newPosition = this.phyValues.position.add(offset, timeStamp);

        // Calculate the velocity by appling the acceleration of the object -> v = v + a*t
        DoubleVector newSpeed
                = this.phyValues.speed.add(
                        this.phyValues.acceleration.scale(elapsedMillis));

        // Creating a new DTO and return it
        return new PhysicalValuesDTO(
                this.phyValues.mass,
                this.phyValues.maxModuleAcceleration,
                this.phyValues.maxModuleDeceleration,
                this.phyValues.maxModuleSpeed,
                newPosition,
                newSpeed,
                this.phyValues.acceleration);
    }


    public DoubleVector getCoordinates() {
        return new DoubleVector(this.phyValues.position);
    }


    public PhysicalValuesDTO getPhysicalValues() {
        return this.phyValues;
    }


    public void setPhysicalValues(PhysicalValuesDTO newPhyValues) {
        this.phyValues = newPhyValues;
    }
}
