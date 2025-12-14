package world.providers;


import assets.AssetCatalog;
import assets.AssetInfoDTO;
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
    private final ProjectAssets projectAssets;
    private final AssetCatalog gameAssets = new AssetCatalog("src/resources/images/");

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
        this.projectAssets = assets;
    }


    @Override
    public WorldDefinition provide() {

        this.background = randomBackgroundDef();

        this.randomDecoratorsDef(this.spaceDecoratorsDef, 1, AssetType.STARS, 200, 150);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, AssetType.PLANET, 150, 110);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, AssetType.MOON, 45, 40);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, AssetType.SUN, 25, 15);

        this.randomStaticBodiesDef(this.gravityBodiesDef, 1, AssetType.BLACK_HOLE, 35, 25);

        this.randomStaticBodiesDef(this.bombsDef, 0, AssetType.BOMB, 30, 20);

        this.randomDBodiesDef(this.asteroidsDef, 6, AssetType.ASTEROID, 15, 3);

        this.randomWeaponsDef(this.bulletDef, 1, AssetType.BULLET, 5, 5,
                              275d, 0d, 0d, // firingSpeed, acc, accTime 
                              0, 1, 20); // shootingOffset, burstSize, fireRate

        this.randomWeaponsDef(this.misilsDef, 1, AssetType.MISIL, 30, 30,
                              -100, 4000d, 1d, // firingSpeed, acc, accTime 
                              0, 1, 1); // shootingOffset, burstSize, fireRate

        this.randomDBodiesDef(this.spaceshipsDef, 1, AssetType.SPACESHIP, 25, 25);

        return new WorldDefinition(
                this.width, this.height, this.gameAssets,
                background, spaceDecoratorsDef, gravityBodiesDef,
                asteroidsDef, bulletDef, misilsDef, bombsDef, spaceshipsDef);
    }


    private double randomAngle() {
        return this.rnd.nextFloat() * 360;
    }


    private void randomDecoratorsDef(
            ArrayList<PositionVItemDto> decos,
            int num, AssetType type,
            int maxSize, int minSize) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            decos.add(new PositionVItemDto(
                    randomId,
                    this.randomSize(maxSize, minSize),
                    this.randomAngle(),
                    rnd.nextDouble() * this.width, // x
                    rnd.nextDouble() * this.height)); // y
        }
    }


    private void randomDBodiesDef(ArrayList<VItemDto> dBodies,
            int num, AssetType type,
            int maxSize, int minSize) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            dBodies.add(new VItemDto(
                    randomId,
                    this.randomSize(maxSize, minSize),
                    this.randomAngle()));
        }
    }


    private void randomStaticBodiesDef(
            ArrayList<PositionVItemDto> sBodies,
            int num, AssetType type,
            int maxSize, int minSize) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            sBodies.add(new PositionVItemDto(
                    randomId,
                    this.randomSize(maxSize, minSize),
                    this.randomAngle(),
                    rnd.nextDouble() * this.width,
                    rnd.nextDouble() * this.height));
        }
    }


    private BackgroundDto randomBackgroundDef() {
        String randomId = this.projectAssets.catalog.randomId(AssetType.BACKGROUND);
        AssetInfoDTO assetInfo = this.projectAssets.catalog.get(randomId);
        this.gameAssets.register(assetInfo);

        return new BackgroundDto(randomId, 0.0d, 0.0d);
    }


    private double randomSize(int maxSize, int minSize) {
        return (minSize + (this.rnd.nextFloat() * (maxSize - minSize)));
    }


    private void randomWeaponsDef(ArrayList<WeaponVItemDto> weapons,
            int num, AssetType type,
            int maxSize, int minSize, double firingSpeed,
            double acc, double accTime, int shootingOffset,
            int burstSize, int fireRate) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            weapons.add(
                    new WeaponVItemDto(
                            randomId, this.randomSize(maxSize, minSize),
                            firingSpeed, acc, accTime,
                            burstSize, fireRate));
        }
    }
}
