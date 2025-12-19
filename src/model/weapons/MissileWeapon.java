package model.weapons;

public class MissileWeapon extends AbstractWeapon {
    private double cooldown = 0.0; // seconds

    public MissileWeapon(String projectileAssetId, double projectileSize,
            double acceleration, double accelerationTime,
            double shootingOffset, double fireRate) {

        super(projectileAssetId, projectileSize, 0,
                acceleration, accelerationTime,
                shootingOffset, 1, fireRate);
    }

    @Override
    public boolean mustFireNow(double dtSeconds) {
        if (this.cooldown > 0) {
            // Cool down weapon. Any pending requests are discarded.
            this.cooldown -= dtSeconds;
            this.markAllRequestsHandled();
            return false; // =================>
        }

        if (!this.hasRequest()) {
            // Nothing to do
            this.cooldown = 0;
            return false; // ==================>
        }

        // Fire
        this.markAllRequestsHandled();
        cooldown = 1.0 / this.getWeaponConfig().fireRate;
        return true;
    }
}
