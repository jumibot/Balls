package model.weapons.ports;

public interface Weapon {

    public WeaponDto getWeaponConfig();


    public String getId();


    public void registerFireRequest();


    public boolean mustFireNow(double dtSeconds);
}
