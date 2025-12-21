package model.mappers;

import world.WorldDefWeaponDto;
import model.weapons.WeaponDto;
import model.weapons.WeaponType;

public class WeaponMapper {

    public static WeaponDto fromWorldDef(WorldDefWeaponDto weaponDef, int shootingOffset) {
        if (weaponDef == null) {
            return null;
        }
        return new WeaponDto(
                WeaponType.valueOf(weaponDef.type.name()),
                weaponDef.assetId,
                weaponDef.size,
                weaponDef.firingSpeed,
                weaponDef.acceleration,
                weaponDef.accelerationDuration,
                weaponDef.burstSize,
                weaponDef.fireRate,
                weaponDef.maxAmmo,
                weaponDef.reloadTime,
                weaponDef.projectileMass,
                weaponDef.maxlifeTime,
                shootingOffset
        );   
    }
}