package world;


import assets.AssetCatalog;
import java.util.ArrayList;


public class WorldDefinition {

    public final int worldWidth;
    public final int worldHeight;

    public final AssetCatalog gameAssets;
    public final BackgroundDto backgroundDef;
    public final ArrayList<PositionVItemDto> spaceDecoratorsDef;
    public final ArrayList<PositionVItemDto> gravityBodiesDef;
    public final ArrayList<VItemDto> asteroidsDef;
    public final ArrayList<WeaponVItemDto> bullets;
    public final ArrayList<WeaponVItemDto> misilsDef;
    public ArrayList<PositionVItemDto> bombsDef;
    public ArrayList<VItemDto> spaceshipsDef;


    public WorldDefinition(
            int worldWidth,
            int worldHeight,
            AssetCatalog gameAssets,
            BackgroundDto background,
            ArrayList<PositionVItemDto> spaceDecorators,
            ArrayList<PositionVItemDto> gravityBodies,
            ArrayList<VItemDto> asteroids,
            ArrayList<WeaponVItemDto> bullets,
            ArrayList<WeaponVItemDto> misils,
            ArrayList<PositionVItemDto> bombs,
            ArrayList<VItemDto> spaceships) {

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.gameAssets = gameAssets;
        this.backgroundDef = background;
        this.spaceDecoratorsDef = spaceDecorators;
        this.gravityBodiesDef = gravityBodies;
        this.asteroidsDef = asteroids;
        this.bullets = bullets;
        this.misilsDef = misils;
        this.bombsDef = bombs;
        this.spaceshipsDef = spaceships;
    }
}
