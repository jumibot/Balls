package world.providers;


import assets.AssetCatalog;
import assets.AssetType;
import assets.Assets;
import java.util.ArrayList;
import java.util.Random;

import world.BackgroundDef;
import world.DecoratorDef;
import world.DynamicBodyDef;
import world.StaticBodyDef;
import world.WorldDefinition;
import world.WorldDefinitionProvider;


public class RandomWorldDefinitionProvider implements WorldDefinitionProvider {

    private final Random rnd = new Random();
    private final int width;
    private final int height;
    private final Assets assets;

    private BackgroundDef background;

    private ArrayList<DecoratorDef> spaceDecorators = new ArrayList<>(20);
    private ArrayList<StaticBodyDef> gravityBodies = new ArrayList<>(20);
    private ArrayList<StaticBodyDef> bombs = new ArrayList<>(20);
    private ArrayList<DynamicBodyDef> asteroids = new ArrayList<>(20);
    private ArrayList<DynamicBodyDef> misils = new ArrayList<>(20);
    private ArrayList<DynamicBodyDef> spaceships = new ArrayList<>(20);


    public RandomWorldDefinitionProvider(int worldWidth, int worldHeight, Assets assets) {
        this.width = worldWidth;
        this.height = worldHeight;
        this.assets = assets;
    }


    @Override
    public WorldDefinition provide() {

        this.background = randomBackground();

        this.randomDecorators(
                this.spaceDecorators, 1, assets.spaceDecors, AssetType.STARS, 250, 50);

        this.randomStaticBodies(
                this.gravityBodies, 1, assets.gravityBodies, AssetType.PLANET, 100, 85);

        this.randomStaticBodies(
                this.gravityBodies, 1, assets.gravityBodies, AssetType.MOON, 50, 30);

        this.randomStaticBodies(
                this.gravityBodies, 1, assets.gravityBodies, AssetType.SUN, 30, 20);

        this.randomStaticBodies(
                this.gravityBodies, 1, assets.gravityBodies, AssetType.BLACK_HOLE, 30, 20);

        this.randomStaticBodies(
                this.bombs, 0, assets.weapons, AssetType.BOMB, 30, 20);

        this.randomDynamicBodies(
                this.asteroids, 6, assets.solidBodies, AssetType.ASTEROID, 15, 3);

        this.randomDynamicBodies(
                this.misils, 1, assets.weapons, AssetType.MISIL, 30, 20);

        this.randomDynamicBodies(
                this.spaceships, 1, assets.spaceship, AssetType.SPACESHIP, 35, 25);

        this.randomDynamicBodies(
                this.spaceships, 1, assets.spaceship, AssetType.LAB, 35, 20);

        // WorldDefinition
        return new WorldDefinition(
                this.width, this.height,
                background, spaceDecorators, gravityBodies,
                asteroids, misils, bombs, spaceships);
    }


    private void randomDecorators(
            ArrayList<DecoratorDef> decos,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        for (int i = 0; i < num; i++) {
            decos.add(new DecoratorDef(
                    catalog.randomId(type),
                    1, // Layer
                    rnd.nextDouble() * this.width, // x
                    rnd.nextDouble() * this.height, // y
                    this.randomSize(maxSize, minSize),
                    this.randomAngle()));
        }
    }


    private void randomDynamicBodies(ArrayList<DynamicBodyDef> dBodies,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        for (int i = 0; i < num; i++) {
            dBodies.add(new DynamicBodyDef(
                    catalog.randomId(type),
                    this.randomSize(maxSize, minSize),
                    this.randomAngle()));
        }
    }


    private void randomStaticBodies(
            ArrayList<StaticBodyDef> sBodies,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        for (int i = 0; i < num; i++) {
            sBodies.add(new StaticBodyDef(
                    catalog.randomId(type),
                    rnd.nextDouble() * this.width, // x
                    rnd.nextDouble() * this.height, // y
                    this.randomSize(maxSize, minSize),
                    this.randomAngle()));
        }
    }


    private BackgroundDef randomBackground() {
        String randomId = this.assets.backgrounds.randomId(null);

        return new BackgroundDef(randomId, 0.0d, 0.0d);
    }


    private double randomSize(int maxSize, int minSize) {
        return (minSize + (this.rnd.nextFloat() * (maxSize - minSize)));
    }


    private double randomAngle() {
        return this.rnd.nextFloat() * 360;
    }
}
