package model.weapons;


public interface Weapon {

    public WeaponDto getWeaponConfig();


    public String getId();


    public void registerFireRequest();


    public boolean update(double dtSeconds);
}
