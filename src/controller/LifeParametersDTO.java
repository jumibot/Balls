package controller;


import _helpers.DoubleVector;


public class LifeParametersDTO {

    public final int maxCreationDelay;
    public final double maxMass;
    public final double minMass;
    public final double speedMaxModule;
    public final double accMaxModule;
    public final int maxSize;
    public final int minSize;
    public final DoubleVector fixedAcc;
    public final DoubleVector fixedSpeed;


    public LifeParametersDTO(
            int maxCreationDelay, double maxMass, double minMass,
           double speed, double acceleration, 
            int maxSize, int minSize) {

        this.maxCreationDelay = maxCreationDelay;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.speedMaxModule = speed;
        this.accMaxModule = acceleration;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.fixedAcc = null;
        this.fixedSpeed = null;
    }


    public LifeParametersDTO(
            int maxCreationDelay, double maxMass, double minMass,
             DoubleVector fixedSpeed, DoubleVector fixedAcc,
            int maxSize, int minSize) {

        this.maxCreationDelay = maxCreationDelay;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.speedMaxModule = 0;
        this.accMaxModule = 0;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.fixedAcc = fixedAcc;
        this.fixedSpeed = fixedSpeed;
    }
}
