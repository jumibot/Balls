package controller.mappers;

import world.WorldDefWeaponDto;
import model.weapons.WeaponDto;

public class WeaponMapper {

    public static WeaponDto fromWorldDef(WorldDefWeaponDto weaponDef, int shootingOffset) {
        if (weaponDef == null) {
            return null;
        }
        return new WeaponDto(
            WeaponTypeMapper.fromWorldDef(weaponDef.type),
                weaponDef.assetId,
                weaponDef.size,
                weaponDef.projectileSpeed,
                weaponDef.acceleration,
                weaponDef.accelerationDuration,
                weaponDef.burstSize,
                weaponDef.burstFireRate,
                weaponDef.fireRate,
                weaponDef.maxAmmo,
                weaponDef.reloadTime,
                weaponDef.projectileMass,
                weaponDef.maxLifetimeInSeconds,
                shootingOffset
        );   
    }
} 