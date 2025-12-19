package model.weapons;

public class BurstWeapon extends AbstractWeapon {

    private double cooldown = 0.0; // seconds
    private int shotsRemaining = 0;

    public BurstWeapon(String projectileAssetId, double projectileSize,
            double firingSpeed, double shootingOffset, int burstSize, double fireRate) {

        super(projectileAssetId, projectileSize, firingSpeed,
                0, 0,
                shootingOffset, burstSize, fireRate);
    }

    @Override
    public boolean mustFireNow(double dtSeconds) {
        if (this.cooldown > 0) {
            // Cool down weapon. Any pending requests or shot are discarded.
            this.cooldown -= dtSeconds;
            this.markAllRequestsHandled();
            return false; // =================>
        }

        if (this.shotsRemaining > 0) {
            // Burst mode ongoing...
            // Discard any pending requests while in burst mode
            this.markAllRequestsHandled();

            this.shotsRemaining--;
            this.cooldown = 1.0 / this.getWeaponConfig().fireRate;
            // Requesting shot
            return true;
        }

        if (!this.hasRequest()) {
            // No burst to start
            this.cooldown = 0;
            return false; // ==================>
        }

        // Start new burst
        this.markAllRequestsHandled();

        int burstSize = this.getWeaponConfig().burstSize;
        this.shotsRemaining = Math.max(1, burstSize)-1; // One shot now
        this.cooldown = 1.0 / this.getWeaponConfig().fireRate;
        return true;
    }
}
