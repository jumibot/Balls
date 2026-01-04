package model.weapons.core;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import model.weapons.ports.Weapon;
import model.weapons.ports.WeaponDto;
import model.weapons.ports.WeaponState;

/**
 * AbstractWeapon
 * --------------
 *
 * Base class for all weapon implementations in the simulation.
 *
 * This class provides:
 * - Immutable identification (weapon id)
 * - A static configuration object (WeaponDto)
 * - A thread-safe monotonic firing-request mechanism
 * - A unified API for discrete-tick weapon updates
 *
 * CONCURRENCY MODEL
 * -----------------
 * - The method registerFireRequest() may be invoked from *any thread*
 * (typically from the Controller or Model in response to player input).
 * It stores a timestamp (System.nanoTime) in an AtomicLong.
 *
 * - The weapon is a *passive component*: it owns no thread and performs no
 * asynchronous work. All firing logic must occur when update(dtSeconds) is
 * executed by the PlayerBody's simulation thread.
 *
 * FIRING REQUEST MODEL
 * --------------------
 * - lastFireRequest holds the timestamp of the *latest* firing request.
 * - lastHandledRequest holds the timestamp of the most recent request already
 * processed (shot produced or consciously ignored).
 *
 * - A new request exists if:
 * lastFireRequest > lastHandledRequest
 *
 * - Calling hasNewRequest() consumes the request by advancing
 * lastHandledRequest to lastFireRequest.
 *
 * - No request buffering or queuing is performed. Only the most recent firing
 * intention matters. Requests that occur during cooldown or ongoing firing
 * sequences are consumed and discarded. This yields simple and predictable
 * behavior aligned with discrete-tick simulation.
 *
 * DESIGN PHILOSOPHY
 * -----------------
 * - Weapons do not run code outside update(): deterministic, thread-safe,
 * and easy to reason about in a heavily multi-threaded simulation.
 *
 * - Firing intent is monotonic and edge-triggered. The system never stores
 * multiple pending shots; weapons decide immediately whether to fire or
 * ignore a request.
 *
 * - AbstractWeapon imposes no specific firing behavior; it standardizes only
 * the request-consumption rules. Concrete weapons (BasicWeapon, BurstWeapon,
 * MissileWeapon, etc.) implement all actual firing logic.
 *
 * GUIDELINES FOR NEW WEAPON IMPLEMENTATIONS
 * ------------------------------------------
 * If you are implementing a new weapon type, follow these principles:
 *
 * 1. **Never block the update() thread.** All logic must be fast and strictly
 * local to one tick.
 *
 * 2. **Use hasNewRequest() only to detect and consume intent.** Do not attempt
 * to re-interpret or store past requests.
 *
 * 3. **Return at most one shot per tick.** Multishot or burst weapons should
 * spread shots across ticks unless the game explicitly supports simultaneous
 * multi-projectile emission.
 *
 * 4. **Manage your own internal timing.** Use dtSeconds to reduce cooldowns,
 * burst timers, or acceleration windows.
 *
 * 5. **Do not create projectiles inside update().** update() should only signal
 * intent (true/false). The Model or caller is responsible for instantiating
 * DynamicBody projectiles.
 *
 * 6. **Be explicit about request-handling policy.** Decide whether a weapon
 * ignores requests during cooldown, collapses multiple requests into one, or
 * only starts bursts when idle.
 *
 * 7. **Keep the weapon stateless with respect to threading.** No background
 * threads, no timers, no sleeps; everything happens during the PlayerBody's
 * tick cycle.
 *
 * These rules ensure that all weapons behave consistently inside the engine,
 * remain deterministic, and do not introduce concurrency hazards.
 */
public abstract class AbstractWeapon implements Weapon {

    private final String id;
    private final WeaponDto weaponConfig;
    private final AtomicLong lastFireRequest = new AtomicLong(0L);
    private WeaponState state;
    private long lastHandledRequest = 0L;
    private int currentAmmo;
    private double cooldown = 0.0; // seconds

    public AbstractWeapon(WeaponDto weaponConfig) {

        if (weaponConfig.fireRate <= 0) {
            throw new IllegalArgumentException(
                    "fireRatePerSec must be > 0. Weapon not created");
        }

        this.id = UUID.randomUUID().toString();
        this.weaponConfig = weaponConfig;
        this.currentAmmo = weaponConfig.maxAmmo;
        this.state = WeaponState.READY;
    }

    public void decCooldown(double dtSeconds) {
        this.cooldown -= dtSeconds;
    }

    public void decCurrentAmmo() {
        this.currentAmmo --;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public WeaponDto getWeaponConfig() {
        return new WeaponDto(this.weaponConfig);
    }

    public double getCooldown() {
        return this.cooldown;
    }

    public int getCurrentAmmo() {
        return this.currentAmmo;
    }

    public int getMaxAmmo() {
        return this.weaponConfig.maxAmmo;
    }

    public WeaponState getState() {
        return this.state;
    }

    public double getReloadTime() {
        return this.weaponConfig.reloadTime;
    }

    protected boolean hasRequest() {
        return this.lastFireRequest.get() > this.lastHandledRequest;
    }

    protected void markAllRequestsHandled() {
        this.lastHandledRequest = this.lastFireRequest.get();
    }

    @Override
    public void registerFireRequest() {
        this.lastFireRequest.set(System.nanoTime());
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    public void setState(WeaponState state) {
        this.state = state;
    }
}
