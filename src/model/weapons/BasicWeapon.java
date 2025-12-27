package model.weapons;

public class BasicWeapon extends AbstractWeapon {

    private double cooldown = 0.0; // seconds

    public BasicWeapon(WeaponDto weaponConfig) {
        super(weaponConfig);
    }

    @Override
    public boolean mustFireNow(double dtSeconds) {
        if (this.cooldown > 0) {
            // Cool down weapon. Any pending requests are discarded.
            this.cooldown -= dtSeconds;
            this.markAllRequestsHandled();
            return false; // ======== Weapon is overheated =========>
        }

        if (this.currentAmmo <= 0) {
            // No ammunition: reload, set time to reload and discard requests
            this.markAllRequestsHandled();
            cooldown = this.getWeaponConfig().reloadTime;
            this.currentAmmo = this.getWeaponConfig().maxAmmo;
            return false;
        }

        if (!this.hasRequest()) {
            // Nothing to do
            this.cooldown = 0;
            return false; // ==================>
        }

        // Fire
        this.markAllRequestsHandled();
        this.currentAmmo--;
        cooldown = 1.0 / this.getWeaponConfig().fireRate;
        return true;
    }
}
