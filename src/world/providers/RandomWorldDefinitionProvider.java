package world.providers;

import assets.AssetCatalog;
import assets.AssetInfoDTO;
import assets.AssetType;
import assets.ProjectAssets;
import java.util.ArrayList;
import java.util.Random;

import world.WorldDefBackgroundDto;
import world.WorldDefItemDto;
import world.WorldDefPositionItemDto;
import world.WorldDefWeaponDto;
import world.WorldDefinition;
import world.WorldDefinitionProvider;

public class RandomWorldDefinitionProvider implements WorldDefinitionProvider {

    private final Random rnd = new Random();
    private final int width;
    private final int height;
    private final ProjectAssets projectAssets;
    private final AssetCatalog gameAssets = new AssetCatalog("src/resources/images/");

    private WorldDefBackgroundDto background;

    private ArrayList<WorldDefPositionItemDto> decoratorsDef = new ArrayList<>();
    private ArrayList<WorldDefPositionItemDto> gravityBodiesDef = new ArrayList<>();

    private ArrayList<WorldDefItemDto> asteroidsDef = new ArrayList<>();
    private ArrayList<WorldDefItemDto> spaceshipsDef = new ArrayList<>();

    private ArrayList<WorldDefWeaponDto> primaryWeapon = new ArrayList<>();
    private ArrayList<WorldDefWeaponDto> secondaryWeaponDef = new ArrayList<>();
    private ArrayList<WorldDefWeaponDto> mineLaunchersDef = new ArrayList<>();
    private ArrayList<WorldDefWeaponDto> missilLaunchersDef = new ArrayList<>();

    public RandomWorldDefinitionProvider(int worldWidth, int worldHeight, ProjectAssets assets) {
        this.width = worldWidth;
        this.height = worldHeight;
        this.projectAssets = assets;
    }

    @Override
    public WorldDefinition provide() {

        this.background = randomBackgroundDef();

        this.decorators(this.decoratorsDef, 2, AssetType.STARS, 150, 75);

        this.staticBodies(this.gravityBodiesDef, 2, AssetType.PLANET, 225, 50);

        this.staticBodies(this.gravityBodiesDef, 1, AssetType.MOON, 75, 50);

        this.staticBodies(this.gravityBodiesDef, 1, AssetType.SUN, 45, 35);

        this.staticBodies(this.gravityBodiesDef, 1, AssetType.BLACK_HOLE, 55, 45);

        this.dynamicBodies(this.asteroidsDef, 6, AssetType.ASTEROID, 25, 15);

        this.dynamicBodies(this.spaceshipsDef, 1, AssetType.SPACESHIP, 45, 45);

        this.gunDef(this.primaryWeapon, 1, AssetType.BULLET,
                12, 12, 275d, 10);

        this.machineGuns(this.secondaryWeaponDef, 1, AssetType.BULLET,
                12, 12, 275d, 10, 5);

        this.mineLaunchers(this.mineLaunchersDef, 1, AssetType.MINE, 30, 25, 150);

        this.missilLaunchers(this.missilLaunchersDef, 1, AssetType.MISSILE,
                40, 35, 4000d, 1d, 1);

        WorldDefinition worlDef = new WorldDefinition(this.width, this.height, this.gameAssets,
                background, decoratorsDef, gravityBodiesDef, asteroidsDef, spaceshipsDef,
                primaryWeapon, secondaryWeaponDef, missilLaunchersDef, mineLaunchersDef);

        return worlDef;
    }

    private double randomAngle() {
        return this.rnd.nextFloat() * 360d;
    }

    private void decorators(
            ArrayList<WorldDefPositionItemDto> decos,
            int num, AssetType type,
            int maxSize, int minSize) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            decos.add(new WorldDefPositionItemDto(
                    randomId,
                    this.randomSize(maxSize, minSize),
                    this.randomAngle(),
                    rnd.nextDouble() * this.width, // x
                    rnd.nextDouble() * this.height)); // y
        }
    }

    private void dynamicBodies(ArrayList<WorldDefItemDto> dBodies,
            int num, AssetType type,
            int maxSize, int minSize) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            dBodies.add(new WorldDefItemDto(
                    randomId,
                    this.randomSize(maxSize, minSize),
                    this.randomAngle()));
        }
    }

    private void staticBodies(
            ArrayList<WorldDefPositionItemDto> sBodies,
            int num, AssetType type,
            int maxSize, int minSize) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            sBodies.add(new WorldDefPositionItemDto(
                    randomId,
                    this.randomSize(maxSize, minSize),
                    this.randomAngle(),
                    rnd.nextDouble() * this.width,
                    rnd.nextDouble() * this.height));
        }
    }

    private WorldDefBackgroundDto randomBackgroundDef() {
        String randomId = this.projectAssets.catalog.randomId(AssetType.BACKGROUND);
        AssetInfoDTO assetInfo = this.projectAssets.catalog.get(randomId);
        this.gameAssets.register(assetInfo);

        return new WorldDefBackgroundDto(randomId, 0.0d, 0.0d);
    }

    private double randomSize(int maxSize, int minSize) {
        return (minSize + (this.rnd.nextFloat() * (maxSize - minSize)));
    }

    private void mineLaunchers(ArrayList<WorldDefWeaponDto> weapons, int num, AssetType type,
            int maxSize, int minSize, int fireRate) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            weapons.add(new WorldDefWeaponDto(
                    randomId, this.randomSize(maxSize, minSize),
                    0, 0, 0,
                    1, fireRate));
        }
    }

    private void gunDef(ArrayList<WorldDefWeaponDto> weapons, int num, AssetType type,
            int maxSize, int minSize, double firingSpeed, int fireRate) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            weapons.add(new WorldDefWeaponDto(
                    randomId, this.randomSize(maxSize, minSize),
                    firingSpeed, 0, 0,
                    1, fireRate));
        }
    }

    private void machineGuns(ArrayList<WorldDefWeaponDto> weapons,
            int num, AssetType type,
            int maxSize, int minSize, double firingSpeed,
            int burstSize, int fireRate) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            weapons.add(new WorldDefWeaponDto(
                    randomId, this.randomSize(maxSize, minSize),
                    firingSpeed, 0, 0,
                    1, fireRate));
        }
    }

    private void missilLaunchers(ArrayList<WorldDefWeaponDto> weapons,
            int num, AssetType type,
            int maxSize, int minSize,
            double acc, double accTime, int fireRate) {

        String randomId;
        AssetInfoDTO assetInfo;

        for (int i = 0; i < num; i++) {
            randomId = this.projectAssets.catalog.randomId(type);
            assetInfo = this.projectAssets.catalog.get(randomId);
            this.gameAssets.register(assetInfo);

            weapons.add(new WorldDefWeaponDto(
                    randomId, this.randomSize(maxSize, minSize),
                    0, acc, accTime,
                    1, fireRate));
        }
    }
}
