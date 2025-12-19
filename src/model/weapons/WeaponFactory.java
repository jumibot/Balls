package model.weapons;

public class WeaponFactory {

    /**
     * Crete a weapon using type and configuration
     */
    public static Weapon create(WeaponType type, WeaponDto config) {
        switch (type) {
            case BASIC:
                return new BasicWeapon(
                        config.projectileAssetId,
                        config.projectileSize,
                        config.firingSpeed,
                        config.shootingOffeset,
                        config.fireRate);

            case BURST:
                return new BurstWeapon(
                        config.projectileAssetId,
                        config.projectileSize,
                        config.firingSpeed,
                        config.shootingOffeset,
                        config.burstSize,
                        config.fireRate);

            case MISSILE:
                return new MissileWeapon(
                        config.projectileAssetId,
                        config.projectileSize,
                        config.acceleration,
                        config.accelerationTime,
                        config.shootingOffeset,
                        config.fireRate);

            default:
                throw new IllegalArgumentException(
                        "Tipo de arma desconocido: " + type);
        }
    }

}
