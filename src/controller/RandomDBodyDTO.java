package controller;


class RandomDBodyDTO {

    public final int maxSize;
    public final int minSize;
    public final double maxMass;
    public final double minMass;
    public final double speedMaxModule;
    public final double accMaxModule;
    public final boolean fixedAcc;
    public final double fixedAcc_x;
    public final double fixedAcc_y;
    public final boolean fixedSpeed;
    public final double fixedSpeed_x;
    public final double fixedSpeed_y;


    public RandomDBodyDTO(
            int maxSize, int minSize,
            double maxMass, double minMass,
            double maxSpeedModule, double maxAccModule) {

        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.speedMaxModule = maxSpeedModule;
        this.accMaxModule = maxAccModule;
        this.fixedAcc = false;
        this.fixedSpeed = false;
        this.fixedAcc_x = 0d;
        this.fixedAcc_y = 0d;
        this.fixedSpeed_x = 0d;
        this.fixedSpeed_y = 0d;
    }


    public RandomDBodyDTO(
            int maxSize, int minSize,
            double maxMass, double minMass,
            double fixedSpeed_x, double fixedSpeed_y,
            double fixedAcc_x, double fixedAcc_y) {

        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;
        this.speedMaxModule = 0;
        this.accMaxModule = 0;
        this.fixedAcc = true;
        this.fixedSpeed = true;
        this.fixedAcc_x = fixedAcc_x;
        this.fixedAcc_y = fixedAcc_y;
        this.fixedSpeed_x = fixedSpeed_x;
        this.fixedSpeed_y = fixedSpeed_y;
    }
}
