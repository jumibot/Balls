package Helpers;


import java.io.Serializable;


public class DoubleVector implements Serializable {

    private double x;
    private double y;
    private double module;


    /**
     * CONSTRUCTORS
     */
    public DoubleVector() {
        this.x = 0;
        this.y = 0;
        this.module = 0;
    }


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
    public void add(DoubleVector dVector) {
        this.x += dVector.x;
        this.y += dVector.y;
        this.calcModule();
    }


    public double getModule() {
        return this.module;
    }


    public void calcModule() {
        double module;

        module = Math.pow(Math.abs(this.getX()), 2) + Math.pow(Math.abs(this.getY()), 2);
        module = Math.pow(module, 0.5);

        this.module = module;
    }


    public DoubleVector getRotated(double angle) {
        double angleInRadians = Math.toRadians(angle);
        double x = this.x * Math.cos(angleInRadians) - this.y * Math.sin(angleInRadians);
        double y = this.x * Math.sin(angleInRadians) + this.y * Math.cos(angleInRadians);

        return new DoubleVector(x, y);
    }


    public void rotate(double angle) {
        double angleInRadians = Math.toRadians(angle);
        double x = this.x * Math.cos(angleInRadians) - this.y * Math.sin(angleInRadians);
        double y = this.x * Math.sin(angleInRadians) + this.y * Math.cos(angleInRadians);

        this.x = x;
        this.y = y;
    }


    public double getX() {
        return this.x;
    }


    public double getY() {
        return this.y;
    }


    public void set(DoubleVector dVector) {
        this.x = dVector.x;
        this.y = dVector.y;
        this.calcModule();
    }


    public void scale(double scaleFactor) {
        this.x *= scaleFactor;
        this.y *= scaleFactor;
        this.calcModule();
    }


    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
        this.calcModule();
    }


    public void setX(double x) {
        this.x = x;
        this.calcModule();
    }


    public void setY(double y) {
        this.y = y;
        this.calcModule();
    }


    @Override
    public String toString() {
        return "(" + this.x + " : " + this.y + ")";
    }
}
