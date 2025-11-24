package controller;


import _helpers.DoubleVector;


public class RandomSBodyDTO {

    public final double maxMass;
    public final double minMass;
    public final int maxSize;
    public final int minSize;


    public RandomSBodyDTO(
            int maxSize, int minSize,
            double maxMass, double minMass) {

        this.maxMass = maxMass;
        this.minMass = minMass;
        this.maxSize = maxSize;
        this.minSize = minSize;
    }
}
