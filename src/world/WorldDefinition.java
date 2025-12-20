package world;

import assets.AssetCatalog;
import java.util.ArrayList;

public class WorldDefinition {

    public final int worldWidth;
    public final int worldHeight;

    public final AssetCatalog gameAssets;
    public final WorldDefBackgroundDto backgroundDef;
    public final ArrayList<WorldDefPositionItemDto> spaceDecoratorsDef;
    public final ArrayList<WorldDefPositionItemDto> gravityBodiesDef;
    public final ArrayList<WorldDefItemDto> asteroidsDef;

    public final ArrayList<WorldDefWeaponDto> gunsDef;
    public final ArrayList<WorldDefWeaponDto> machineGunsDef;
    public final ArrayList<WorldDefWeaponDto> mineLaunchersDef;
    public final ArrayList<WorldDefWeaponDto> missilLaunchersDef;

    public ArrayList<WorldDefItemDto> spaceshipsDef;

    public WorldDefinition(
            int worldWidth,
            int worldHeight,
            AssetCatalog gameAssets,
            WorldDefBackgroundDto background,
            ArrayList<WorldDefPositionItemDto> spaceDecorators,
            ArrayList<WorldDefPositionItemDto> gravityBodies,

            ArrayList<WorldDefItemDto> asteroids,
            ArrayList<WorldDefItemDto> spaceships,

            ArrayList<WorldDefWeaponDto> gunsDef,
            ArrayList<WorldDefWeaponDto> machineGunsDef,
            ArrayList<WorldDefWeaponDto> mineLaunchersDef,
            ArrayList<WorldDefWeaponDto> missilLaunchersDef) {

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.gameAssets = gameAssets;
        this.backgroundDef = background;
        this.spaceDecoratorsDef = spaceDecorators;
        this.gravityBodiesDef = gravityBodies;
        this.asteroidsDef = asteroids;
        this.gunsDef = gunsDef;
        this.machineGunsDef = machineGunsDef;
        this.mineLaunchersDef = mineLaunchersDef;
        this.missilLaunchersDef = missilLaunchersDef;
        this.spaceshipsDef = spaceships;
    }
}
