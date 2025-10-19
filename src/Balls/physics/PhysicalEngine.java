package Balls.physics;


import Helpers.DoubleVector;
import Helpers.Position;
import Balls.physics.PhysicalValues;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import static java.lang.System.currentTimeMillis;


/**
 *
 * @author juanm
 *
 * A SIMPLE PHYSICAL MODEL APPLIED TO DYNAMIC OBJECTS BY DEFAULT
 *
 */
public class PhysicalEngine implements Serializable {

    public static boolean debugMode = false;

    public PhysicalValues phyValues;


    /**
     * CONSTRUCTORS
     */
    public PhysicalEngine() {
        this.phyValues = new PhysicalValues();

        this.phyValues.acceleration = new DoubleVector(0, 0);
        this.phyValues.speed = new DoubleVector(0, 0);
    }


    public void calcNewLocation(Position pos, PhysicalValues phyValues) {
        long nowMillis, elapsedMillis;
        DoubleVector offset;
        DoubleVector speed;
        Position newPos;

        this.phyValues.cloneSpeed(this.phyValues.speed);
        this.phyValues.cloneAcceleration(this.phyValues.acceleration);

        // Tiempo transcurrido desde el último cambio de posición
        nowMillis = currentTimeMillis();
        elapsedMillis = nowMillis - pos.getPositionMillis();

        // Desplazamiento durante el lapso de tiempo -> e = v*t
        offset = this.calcOffset(phyValues.speed, elapsedMillis);

        // Nueva posición
        newPos = new Position(pos);
        newPos.add(offset);
        newPos.setPositionMillis(nowMillis);

        pos.clone(newPos);

        // Incremento de velocidad debido a la aceleración -> v = v + a*t
        speed = this.calcSpeed(phyValues.acceleration, phyValues.speed, elapsedMillis);
        phyValues.cloneSpeed(speed);

    }


    public DoubleVector calcOffset(DoubleVector speed, float elapsedMillis) {
        DoubleVector offset = new DoubleVector();

        offset.set(speed);
        offset.scale(elapsedMillis);  // e = v.t

        return offset;
    }


    public DoubleVector calcSpeed(DoubleVector acceleration, DoubleVector speed, float elapsedMillis) {
        DoubleVector newSpeed;
        DoubleVector speedIncrement;

        speedIncrement = new DoubleVector(acceleration); // a
        speedIncrement.scale(elapsedMillis);    // a*t

        newSpeed = new DoubleVector(speed);
        newSpeed.add(speedIncrement);         // v = v + a*t

        return newSpeed;
    }


    public void cloneAcceleration(DoubleVector fVector) {
        this.phyValues.acceleration.set(fVector);
    }


    public void cloneSpeed(DoubleVector fVector) {
        this.phyValues.speed.set(fVector);
    }


    public void paint(Graphics gr, Position pos) {
        if (!PhysicalEngine.debugMode) {
            return;
        }

        int x = (int) pos.getX();
        int y = (int) pos.getY();

        // Aceleración
        gr.setColor(Color.RED);
        gr.drawLine(
                x, y,
                x + (int) (this.phyValues.acceleration.getX() * 200000),
                y + (int) (this.phyValues.acceleration.getY() * 200000));

        // Velocidad
        gr.setColor(Color.YELLOW);
        gr.drawLine(
                x, y,
                x + (int) (this.phyValues.speed.getX() * 50),
                y + (int) (this.phyValues.speed.getY() * 50));
    }


    public void resetAcceleration() {
        this.phyValues.acceleration = new DoubleVector(0, 0);
    }


    public void resetSpeed() {
        this.phyValues.speed = new DoubleVector(0, 0);
    }

}
