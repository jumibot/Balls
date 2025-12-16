package world;


public class WeaponItemDto extends ItemDto {

    public final double firingSpeed;
    public final double acc;
    public final double accTime;
    public final int burstSize;
    public final int fireRate;


    public WeaponItemDto(String assetId, double size, double firingSpeed,
            double acc, double accTime, int bustSize, int fireRate) {

        super(assetId, size, 0);

        this.firingSpeed = firingSpeed;
        this.acc = acc;
        this.accTime = accTime;
        this.burstSize = bustSize;
        this.fireRate = fireRate;
    }
}
