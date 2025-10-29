package helpers;


import java.io.Serializable;


public class DoubleVector implements Serializable {

    private double x;
    private double y;
    private double module;


    /**
     * CONSTRUCTORS
     */
    public DoubleVector(double x, double y) {
        this.x = x;
        this.y = y;
        this.calcModule();
    }


    public DoubleVector(DoubleVector dVector) {
        this.x = dVector.x;
        this.y = dVector.y;
        this.calcModule();
    }


    /**
     * PUBLICS
     */
    public DoubleVector add(DoubleVector dVector) {
        return new DoubleVector(
                this.x + dVector.x,
                this.y += dVector.y);
    }


    public double getModule() {
        return this.module;
    }


    public double getX() {
        return this.x;
    }


    public double getY() {
        return this.y;
    }


    public DoubleVector rotated(double angle) {
        double angleInRadians = Math.toRadians(angle);
        double x = this.x * Math.cos(angleInRadians) - this.y * Math.sin(angleInRadians);
        double y = this.x * Math.sin(angleInRadians) + this.y * Math.cos(angleInRadians);

        return new DoubleVector(x, y);
    }


    public DoubleVector scale(double scaleFactor) {
        return new DoubleVector(
                this.x * scaleFactor,
                this.y * scaleFactor);
    }


    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
        this.calcModule();
    }


    /**
     * PRIVATES
     */
    public void calcModule() {
        double module;

        module = Math.pow(Math.abs(this.getX()), 2) + Math.pow(Math.abs(this.getY()), 2);
        module = Math.pow(module, 0.5);

        this.module = module;
    }


    @Override
    public String toString() {
        return "(" + this.x + " : " + this.y + ")";
    }
}
