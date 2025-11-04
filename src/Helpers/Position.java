package helpers;


import java.io.Serializable;
import static java.lang.System.currentTimeMillis;


public class Position extends DoubleVector implements Serializable {

    public final long timeStampInMillis;     // Absolute time in milliseconds


    /**
     * CONSTRUCTORS
     */
    public Position(double x, double y) {
        super(x, y);
        this.timeStampInMillis = currentTimeMillis();
    }


    public Position(double x, double y, long timeStamp) {
        super(x, y);
        this.timeStampInMillis = timeStamp;
    }


    public Position(DoubleVector coordinates) {
        super(coordinates);

        this.timeStampInMillis = currentTimeMillis();
    }


    public Position(DoubleVector coordinates, long timeStamp) {
        super(coordinates);

        this.timeStampInMillis = timeStamp;
    }


    /**
     * PUBLICS
     */
    public Position add(DoubleVector dVector) {
        return new Position(
                this.x + dVector.x,
                this.y + dVector.y);
    }


    public Position add(DoubleVector dVector, long timeStamp) {
        return new Position(
                this.x + dVector.x,
                this.y + dVector.y,
                timeStamp);
    }


    public DoubleVector calculateOffset(DoubleVector pos) {
        DoubleVector offset
                = new DoubleVector(
                        this.x - pos.x,
                        this.y - pos.y);

        return offset;
    }


    public Position newWithTimeStamp(long ts) {
        return new Position(this.x, this.y, ts);
    }
}
