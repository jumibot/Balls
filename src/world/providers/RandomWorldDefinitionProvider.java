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
                this.spaceDecorators, 2, assets.spaceDecors, AssetType.STARS, 250, 125);

        this.randomStaticBodies(
                this.gravityBodies, 1, assets.gravityBodies, AssetType.PLANET, 120, 80);

        this.randomStaticBodies(
                this.gravityBodies, 1, assets.gravityBodies, AssetType.MOON, 60, 15);

        this.randomStaticBodies(
                this.gravityBodies, 1, assets.gravityBodies, AssetType.SUN, 40, 15);

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
                this.spaceships, 1, assets.spaceship, AssetType.LAB, 55, 20);

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

        double x, y, size;
        int layer;
        String randomId;

        for (int i = 0; i < num; i++) {
            x = rnd.nextDouble() * width;
            y = rnd.nextDouble() * height;
            size = this.randomSize(maxSize, minSize);
            layer = 1;

            if (type == null) {
                randomId = catalog.randomId();
            } else {
                randomId = catalog.randomId(type);
            }

            decos.add(new DecoratorDef(randomId, layer, x, y, size, 0));
        }
    }


    private void randomDynamicBodies(ArrayList<DynamicBodyDef> dBodies,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        double size;
        String randomId;

        for (int i = 0; i < num; i++) {
            size = 100;

            if (type == null) {
                randomId = catalog.randomId();
            } else {
                randomId = catalog.randomId(type);
            }

            dBodies.add(new DynamicBodyDef(randomId, size));
        }
    }


    private void randomStaticBodies(
            ArrayList<StaticBodyDef> sBodies,
            int num, AssetCatalog catalog, AssetType type,
            int maxSize, int minSize) {

        double x, y, size, rotationDeg;
        String randomId;

        for (int i = 0; i < num; i++) {
            x = rnd.nextDouble() * width;
            y = rnd.nextDouble() * height;
            size = this.randomSize(maxSize, minSize);
            rotationDeg = 0;

            if (type == null) {
                randomId = catalog.randomId();
            } else {
                randomId = catalog.randomId(type);
            }

            sBodies.add(new StaticBodyDef(randomId, x, y, size, rotationDeg));
        }
    }


    private BackgroundDef randomBackground() {
        String randomId = this.assets.backgrounds.randomId();

        return new BackgroundDef(randomId, 0.0d, 0.0d);
    }


    private int randomSize(int maxSize, int minSize) {
        return (int) (minSize + (this.rnd.nextFloat() * (maxSize - minSize)));
    }
}
