package model.entities;


import java.util.UUID;
import model.physics.BasicPhysicsEngine;
import model.physics.PhysicsValues;
import model.weapons.Weapon;
import model.weapons.WeaponDto;


public class PlayerBody extends DynamicBody {

    private final String playerId;

    // Calibrate is needed
    private double maxThrustForce = 60;     //
    private double maxAngularAcc = 1800;       // degrees*s^-2
    private double angularSpeed = 180;       // degrees*s^-1

    // === WEAPONS ===
    private final java.util.List<Weapon> weapons = new java.util.ArrayList<>(10);
    private int currentWeaponIndex = -1; // -1 = sin arma


    public PlayerBody(String playerId, String assetId,
            double size, BasicPhysicsEngine physicsEngine) {

        super(assetId, size, physicsEngine);
        this.playerId = playerId;
    }


    public PlayerBody(String assetId, double size,
            BasicPhysicsEngine physicsEngine) {

        this(UUID.randomUUID().toString(), assetId, size, physicsEngine);
    }


    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);

        if (this.currentWeaponIndex < 0) {
            // Signaling existence of weapon in the spaceship
            this.currentWeaponIndex = 0;
        }
    }


    public Weapon getActiveWeapon() {
        if (this.currentWeaponIndex < 0 || this.currentWeaponIndex >= this.weapons.size()) {
            return null;
        }

        return this.weapons.get(this.currentWeaponIndex);
    }


    public WeaponDto getActiveWeaponConfig() {
        Weapon weapon = getActiveWeapon();
        return (weapon != null) ? weapon.getWeaponConfig() : null;
    }


    public String getPlayerId() {
        return this.playerId;
    }


    public void thrustOn() {
        this.setThrust(this.maxThrustForce);
    }


    public void thrustOff() {
        this.resetAcceleration();
        this.setThrust(0.0d);
    }


    public void requestFire() {
        if (this.currentWeaponIndex < 0 || this.currentWeaponIndex >= this.weapons.size()) {
            System.out.println("> No weapon active or no weapons!");
            return;
        }

        Weapon weapon = this.weapons.get(this.currentWeaponIndex);
        if (weapon == null) {
            // Weapon is not created
            return;
        }

        weapon.registerFireRequest();
    }


    public void reverseThrust() {
        this.setThrust(-this.maxThrustForce);
    }


    public void rotateLeftOn() {
        if (this.getAngularSpeed() == 0) {
            this.setAngularSpeed(-this.angularSpeed);
        }

        this.addAngularAcceleration(-this.maxAngularAcc);
    }


    public void rotateRightOn() {
        if (this.getAngularSpeed() == 0) {
            this.setAngularSpeed(this.angularSpeed);
        }

        this.addAngularAcceleration(this.maxAngularAcc);
    }


    public void rotateOff() {
        this.setAngularAcceleration(0.0d);
        this.setAngularSpeed(0.0d);
    }


    public void selectWeapon(int weaponIndex) {
        if (weaponIndex >= 0 && weaponIndex < this.weapons.size()) {
            this.currentWeaponIndex = weaponIndex;
        }
    }


    public void setMaxThrustForce(double maxThrust) {
        this.maxThrustForce = maxThrust;
    }


    public void setMaxAngularAcceleration(double maxAngularAcc) {
        this.setAngularSpeed(this.angularSpeed);
        this.maxAngularAcc = maxAngularAcc;
    }


    public boolean mustFireNow(PhysicsValues newPhyValues) {
        if (this.currentWeaponIndex < 0 || this.currentWeaponIndex >= this.weapons.size()) {
            return false;
        }

        Weapon weapon = this.weapons.get(this.currentWeaponIndex);
        if (weapon == null) {
            return false;
        }

        double dtNanos = newPhyValues.timeStamp - this.getPhysicsValues().timeStamp;
        double dtSeconds = dtNanos / 1_000_000_000;

        return weapon.mustFireNow(dtSeconds);
    }
}
