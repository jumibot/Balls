package world;


import assets.AssetCatalog;
import java.util.ArrayList;


public class WorldDefinition {

    public final int worldWidth;
    public final int worldHeight;

    public final AssetCatalog gameAssets;
    public final BackgroundDto backgroundDef;
    public final ArrayList<PositionItemDto> spaceDecoratorsDef;
    public final ArrayList<PositionItemDto> gravityBodiesDef;
    public final ArrayList<ItemDto> asteroidsDef;
    public final ArrayList<WeaponItemDto> bullets;
    public final ArrayList<WeaponItemDto> misilsDef;
    public ArrayList<PositionItemDto> bombsDef;
    public ArrayList<ItemDto> spaceshipsDef;


    public WorldDefinition(
            int worldWidth,
            int worldHeight,
            AssetCatalog gameAssets,
            BackgroundDto background,
            ArrayList<PositionItemDto> spaceDecorators,
            ArrayList<PositionItemDto> gravityBodies,
            ArrayList<ItemDto> asteroids,
            ArrayList<WeaponItemDto> bullets,
            ArrayList<WeaponItemDto> misils,
            ArrayList<PositionItemDto> bombs,
            ArrayList<ItemDto> spaceships) {

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
