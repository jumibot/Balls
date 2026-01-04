package model.weapons.ports;

/**
 * WeaponDto
 *
 * Immutable data-transfer object describing the projectile configuration and
 * firing mechanics of a weapon. Each Weapon instance refers to one of these
 * DTOs
 * to determine how its projectiles are spawned and how often they may be fired.
 *
 * This structure deliberately contains no logic. All fields are public and
 * final
 * to keep the DTO lightweight, transparent, and ideal for:
 * • external configuration (game rules, scenario files, JSON assets)
 * • weapon upgrades and unlock systems
 * • creation of new weapon types without modifying engine code
 *
 * SEMANTICS (PROJECTILE BEHAVIOR):
 *
 * • projectileAssetId
 * Identifier of the projectile sprite/asset used for rendering.
 *
 * • projectileSize
 * Visual and collision radius/size of the spawned projectile.
 *
 * • firingSpeed
 * Initial muzzle speed relative to the firing body's orientation.
 * The projectile’s initial world-space velocity is:
 *
 * V_initial = V_body + firingSpeed * dir(angle)
 *
 * • acceleration
 * Constant forward acceleration applied after spawn (for missiles or
 * self-propelled projectiles). Zero indicates a non-propelled projectile.
 *
 * • accelerationTime
 * Time interval (in seconds) during which the projectile acceleration
 * is applied. After this period, the projectile transitions to simple
 * MRU physics as handled by the physics engine.
 *
 * • shootingOffset
 * Distance from the center of the firing body to the muzzle origin in
 * the direction of its angle. Prevents projectiles from appearing inside
 * the firing entity.
 *
 *
 * SEMANTICS (WEAPON FIRING MECHANICS):
 *
 * • burstSize
 * Number of projectiles fired in a single burst. For single-shot weapons,
 * this value is typically 1. Burst weapons may define larger values.
 *
 * • fireRatePerSec
 * Maximum number of shots per second the weapon may produce. Applies to both
 * single-shot and burst weapons as their cadence limiter. The exact usage
 * depends on the Weapon implementation (BasicWeapon, BurstWeapon, etc.).
 *
 *
 * DESIGN NOTES:
 *
 * - Weapons use this DTO strictly as immutable configuration. Firing behavior
 * (timing, cooldowns, burst sequencing) is implemented in the Weapon classes.
 *
 * - The Model or a ProjectileFactory consumes this DTO to create DynamicBody
 * instances representing projectiles, ensuring separation of concerns between:
 *
 * weapon logic → when/how many
 * model physics → how the projectile behaves in the world
 *
 * - This DTO supports fully data-driven weapon definitions, enabling:
 * • scenario-dependent weapon loadouts
 * • player progression systems
 * • modding or asset-based weapon packs
 */
public class WeaponDto {

    public final WeaponType type;
    public final String projectileAssetId;
    public final double projectileSize;
    public final double firingSpeed;
    public final double acceleration;
    public final double accelerationTime;
    public final double shootingOffset;
    public final int burstSize;
    public final int burstFireRate;
    public final double fireRate; // shoots per seconds
    public final int maxAmmo;
    public final double reloadTime;
    public final double projectileMass;
    public final double maxLifeTime;

    public WeaponDto(
            WeaponType type,
            String projectileAssetId,
            double projectileSize,
            double firingSpeed,
            double acceleration,
            double accelerationDuration,
            int burstSize,
            int burstFireRate,
            double fireRatePerSec,
            int maxAmmo,
            double reloadTime,
            double projectileMass,
            double maxlifeTime,
            double shootingOffset) {

        this.type = type;
        this.projectileAssetId = projectileAssetId;
        this.projectileSize = projectileSize;
        this.firingSpeed = firingSpeed;
        this.acceleration = acceleration;
        this.accelerationTime = accelerationDuration;
        this.shootingOffset = shootingOffset;
        this.burstSize = burstSize;
        this.burstFireRate = burstFireRate;
        this.fireRate = fireRatePerSec;
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;
        this.projectileMass = projectileMass;
        this.maxLifeTime = maxlifeTime;
    }

    // Clone constructor
    public WeaponDto(
            WeaponDto weaponConfig) {

        this.type = weaponConfig.type;
        this.projectileAssetId = weaponConfig.projectileAssetId;
        this.projectileSize = weaponConfig.projectileSize;
        this.firingSpeed = weaponConfig.firingSpeed;
        this.acceleration = weaponConfig.acceleration;
        this.accelerationTime = weaponConfig.accelerationTime;
        this.shootingOffset = weaponConfig.shootingOffset;
        this.burstSize = weaponConfig.burstSize;
        this.burstFireRate = weaponConfig.burstFireRate;
        this.fireRate = weaponConfig.fireRate;
        this.maxAmmo = weaponConfig.maxAmmo;
        this.reloadTime = weaponConfig.reloadTime;
        this.projectileMass = weaponConfig.projectileMass;
        this.maxLifeTime = weaponConfig.maxLifeTime;
    }
}
