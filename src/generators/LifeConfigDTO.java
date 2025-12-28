package generators;

public class LifeConfigDTO {

    public final int maxCreationDelay;
    public final int maxSize;
    public final int minSize;
    public final double maxMass;
    public final double minMass;
    public final int maxSpeedModule;
    public final int maxAccModule;
    public final boolean fixedSpeed;
    public final boolean fixedAcc;
    public final double speedX;
    public final double speedY;
    public final double accX;
    public final double accY;

    public LifeConfigDTO(int maxCreationDelay,
            int maxSize, int minSize,
            double maxMass, double minMass,
            int maxSpeedModule, int maxAccModule) {

        this.maxCreationDelay = maxCreationDelay;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;

        this.maxSpeedModule = maxSpeedModule;
        this.maxAccModule = maxAccModule;

        this.fixedAcc = false;
        this.fixedSpeed = false;
        this.accX = 0d;
        this.accY = 0d;
        this.speedX = 0d;
        this.speedY = 0d;
    }

    public LifeConfigDTO(int maxCreationDelay,
            int maxSize, int minSize,
            double maxMass, double minMass,
            double accX, double accY,
            double speedX, double speedY) {

        this.maxCreationDelay = maxCreationDelay;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxMass = maxMass;
        this.minMass = minMass;

        this.maxSpeedModule = 0;
        this.maxAccModule = 0;

        this.fixedSpeed = true;
        this.fixedAcc = true;
        this.speedX = speedX;
        this.speedY = speedY;
        this.accX = accX;
        this.accY = accY;
    }

}
