package model.weapons;


public class BasicWeapon extends AbstractWeapon {

    private double cooldown = 0.0;  // seconds


    public BasicWeapon(String projectileAssetId, double projectileSize,
            double firingSpeed, double acceleration, double accelerationTime,
            double shootingOffset, int burstSize, double fireRate) {
        
        super(projectileAssetId, projectileSize,
            firingSpeed, acceleration, accelerationTime,
            shootingOffset, burstSize, fireRate);
    }


    @Override
    public boolean mustFireNow(double dtSeconds) {
        if (this.cooldown > 0) {
            // Cool down weapon. Any pending requests are discarded.
            this.cooldown -= dtSeconds;
            this.markAllRequestsHandled();
            return false;  // =================>
        }

        if (!this.hasRequest()) {
            // Nothing to do
            return false; // ==================>
        }

        // Fire
        this.markAllRequestsHandled();
        cooldown = 1.0 / this.getWeaponConfig().fireRate;
        return true;
    }
}
