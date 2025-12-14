package world.providers;


import assets.AssetCatalog;
import assets.AssetType;
import assets.ProjectAssets;
import java.util.ArrayList;
import java.util.Random;

import world.BackgroundDto;
import world.VItemDto;
import world.PositionVItemDto;
import world.WeaponVItemDto;
import world.WorldDefinition;
import world.WorldDefinitionProvider;


public class RandomWorldDefinitionProvider implements WorldDefinitionProvider {

    private final Random rnd = new Random();
    private final int width;
    private final int height;
    private final ProjectAssets assets;

    private BackgroundDto background;

    private ArrayList<PositionVItemDto> spaceDecoratorsDef = new ArrayList<>();
    private ArrayList<PositionVItemDto> gravityBodiesDef = new ArrayList<>();
    private ArrayList<PositionVItemDto> bombsDef = new ArrayList<>();
    private ArrayList<VItemDto> asteroidsDef = new ArrayList<>();
    private ArrayList<WeaponVItemDto> bulletDef = new ArrayList<>();
    private ArrayList<WeaponVItemDto> misilsDef = new ArrayList<>();
    private ArrayList<VItemDto> spaceshipsDef = new ArrayList<>();


    public RandomWorldDefinitionProvider(int worldWidth, int worldHeight, ProjectAssets assets) {
        this.width = worldWidth;
        this.height = worldHeight;
        this.assets = assets;
    }


    @Override
    public WorldDefinition provide() {

        this.background = randomBackgroundDef();

        this.randomDecoratorsDef(this.spaceDecoratorsDef, 1, assets.catalog, AssetType.STARS, 200, 150);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, assets.catalog, AssetType.PLANET, 150, 110);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, assets.catalog, AssetType.MOON, 45, 40);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, assets.catalog, AssetType.SUN, 25, 15);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, assets.catalog, AssetType.BLACK_HOLE, 35, 25);

        this.randomStaticBodiesDef(this.bombsDef, 0, assets.catalog, AssetType.BOMB, 30, 20);

        this.randomDBodiesDef(this.asteroidsDef, 6, assets.catalog, AssetType.ASTEROID, 15, 3);

        this.randomWeaponsDef(this.bulletDef, 1, this.assets.catalog, AssetType.BULLET, 6, 6,
                150d, 0d, 0d, // firingSpeed, acc, accTime 
                0, 1, 12); // shootingOffset, burstSize, fireRate

        this.randomWeaponsDef(this.misilsDef, 1, this.assets.catalog, AssetType.MISIL, 20, 20,
                0, 2000d, 1d, // firingSpeed, acc, accTime 
                0, 1, 2); // shootingOffset, burstSize, fireRate

        this.randomDBodiesDef(this.spaceshipsDef, 1, assets.catalog, AssetType.SPACESHIP, 25, 25);

        return new WorldDefinition(
                this.width, this.height,
                background, spaceDecoratorsDef, gravityBodiesDef,
                asteroidsDef, bulletDef, misilsDef, bombsDef, spaceshipsDef);
    }


    private double randomAngle() {
        return this.rnd.nextFloat() * 360;
    }


    private void randomDecoratorsDef(
            ArrayList<PositionVItemDto> decos,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        for (int i = 0; i < num; i++) {
            decos.add(new PositionVItemDto(
                    catalog.randomId(type),
                    this.randomSize(maxSize, minSize),
                    this.randomAngle(),
                    rnd.nextDouble() * this.width, // x
                    rnd.nextDouble() * this.height)); // y
        }
        
        
    }


    private void randomDBodiesDef(ArrayList<VItemDto> dBodies,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        for (int i = 0; i < num; i++) {
            dBodies.add(new VItemDto(
                    catalog.randomId(type),
                    this.randomSize(maxSize, minSize),
                    this.randomAngle()));
        }
    }


    private void randomStaticBodiesDef(
            ArrayList<PositionVItemDto> sBodies,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        for (int i = 0; i < num; i++) {
            sBodies.add(new PositionVItemDto(
                    catalog.randomId(type),
                    this.randomSize(maxSize, minSize),
                    this.randomAngle(),
                    rnd.nextDouble() * this.width,
                    rnd.nextDouble() * this.height)); 
        }
    }


    private BackgroundDto randomBackgroundDef() {
        String randomId = this.assets.catalog.randomId(AssetType.BACKGROUND);

        return new BackgroundDto(randomId, 0.0d, 0.0d);
    }


    private double randomSize(int maxSize, int minSize) {
        return (minSize + (this.rnd.nextFloat() * (maxSize - minSize)));
    }


    private void randomWeaponsDef(ArrayList<WeaponVItemDto> weapons,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize, double firingSpeed,
            double acc, double accTime, int shootingOffset,
            int bustSize, int fireRate) {

        for (int i = 0; i < num; i++) {
            weapons.add(new WeaponVItemDto(
                            catalog.randomId(type),
                            this.randomSize(maxSize, minSize),
                            0, //firingSpeed
                            1200d, // acc
                            1d, // accTime 
                            1, // burstSize
                            12 // fireRate
                    )
            );
        }
    }

}
