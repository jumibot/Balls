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

    private ArrayList<DecoratorDef> spaceDecoratorsDef = new ArrayList<>(20);
    private ArrayList<StaticBodyDef> gravityBodiesDef = new ArrayList<>(20);
    private ArrayList<StaticBodyDef> bombsDef = new ArrayList<>(20);
    private ArrayList<DynamicBodyDef> asteroidsDef = new ArrayList<>(20);
    private ArrayList<DynamicBodyDef> misilsDef = new ArrayList<>(20);
    private ArrayList<DynamicBodyDef> spaceshipsDef = new ArrayList<>(20);


    public RandomWorldDefinitionProvider(int worldWidth, int worldHeight, Assets assets) {
        this.width = worldWidth;
        this.height = worldHeight;
        this.assets = assets;
    }


    @Override
    public WorldDefinition provide() {

        this.background = randomBackgroundDef();

        this.randomDecoratorsDef(
                this.spaceDecoratorsDef, 1, assets.spaceDecors, AssetType.STARS, 300, 200);

        this.randomStaticBodiesDef(
                this.gravityBodiesDef, 1, assets.gravityBodies, AssetType.PLANET, 150, 110);

        this.randomStaticBodiesDef(
                this.gravityBodiesDef, 1, assets.gravityBodies, AssetType.MOON, 45, 30);

        this.randomStaticBodiesDef(
                this.gravityBodiesDef, 1, assets.gravityBodies, AssetType.SUN, 25, 15);

        this.randomStaticBodiesDef(
                this.gravityBodiesDef, 1, assets.gravityBodies, AssetType.BLACK_HOLE, 40, 30);

        this.randomStaticBodiesDef(
                this.bombsDef, 0, assets.weapons, AssetType.BOMB, 30, 20);

        this.randomDBodiesDef(
                this.asteroidsDef, 6, assets.solidBodies, AssetType.ASTEROID, 15, 3);

        this.randomDBodiesDef(
                this.misilsDef, 1, assets.weapons, AssetType.MISIL, 30, 20);

        this.randomDBodiesDef(
                this.spaceshipsDef, 1, assets.spaceship, AssetType.SPACESHIP, 25, 25);

//        this.randomDBodiesDef(
//                this.spaceshipsDef, 2, assets.spaceship, AssetType.LAB, 35, 20);

        // WorldDefinition
        return new WorldDefinition(
                this.width, this.height,
                background, spaceDecoratorsDef, gravityBodiesDef,
                asteroidsDef, misilsDef, bombsDef, spaceshipsDef);
    }


    private void randomDecoratorsDef(
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


    private void randomDBodiesDef(ArrayList<DynamicBodyDef> dBodies,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        for (int i = 0; i < num; i++) {
            dBodies.add(new DynamicBodyDef(
                    catalog.randomId(type),
                    this.randomSize(maxSize, minSize),
                    this.randomAngle()));
        }
    }


    private void randomStaticBodiesDef(
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


    private BackgroundDef randomBackgroundDef() {
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
