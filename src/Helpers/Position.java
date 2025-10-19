package Helpers;


import java.io.Serializable;
import static java.lang.System.currentTimeMillis;


public class Position extends DoubleVector implements Serializable {

    private long positionMillis;     // Absolute time in milliseconds


    /**
     * CONSTRUCTORS
     */
    public Position() {
        super();

        this.positionMillis = currentTimeMillis(); // Creation time stamp
    }


    public Position(Position pos) {
        super(pos);

        this.positionMillis = pos.positionMillis;
    }


    public Position(DoubleVector coordinates) {
        super(coordinates);

        this.positionMillis = currentTimeMillis();
    }


    /**
     * PUBLICS
     */
    public DoubleVector calculateOffset(DoubleVector pos) {
        DoubleVector offset
                = new DoubleVector(
                        this.getX() - pos.getX(),
                        this.getY() - pos.getY());

        return offset;
    }


    public void clone(Position pos) {
        this.set(pos);
        this.positionMillis = pos.positionMillis;
    }


    public long getPositionMillis() {
        return this.positionMillis;
    }


    public void setPositionMillis(long timeInMillis) {
        this.positionMillis = timeInMillis;
    }

}
