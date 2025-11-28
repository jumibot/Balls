package world.providers;


import assets.AssetCatalog;
import assets.AssetType;
import assets.Assets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import world.BackgroundDef;
import world.DecoratorDef;
import world.DynamicBodyDef;
import world.StaticBodyDef;
import world.StaticShapeType;
import world.WorldDefinition;
import world.WorldDefinitionProvider;


public class RandomWorldDefinitionProvider implements WorldDefinitionProvider {

    private final Random rnd = new Random();
    private final int width;
    private final int height;
    private final Assets assets;


    public RandomWorldDefinitionProvider(int worldWidth, int worldHeight, Assets assets) {
        this.width = worldWidth;
        this.height = worldHeight;
        this.assets = assets;
    }


    @Override
    public WorldDefinition provide() {

        BackgroundDef background = randomBackground();
        ArrayList<DecoratorDef> spaceDecorators = this.randomDecorators(
                3, assets.spaceDecors, null);

        ArrayList<DecoratorDef> labs = this.randomDecorators(
                1, assets.solidBodies, AssetType.LAB);

        ArrayList<StaticBodyDef> gravityBodies = this.randomStaticBodies(
                2, assets.gravityBodies, null);

        ArrayList<StaticBodyDef> bombs = this.randomStaticBodies(
                1, assets.weapons, AssetType.BOMB);

        ArrayList<DynamicBodyDef> asteroids = this.randomDynamicBodies(
                5, assets.solidBodies, AssetType.ASTEROID);

        ArrayList<DynamicBodyDef> misils = this.randomDynamicBodies(
                1, assets.weapons, AssetType.MISIL);

        ArrayList<DynamicBodyDef> spaceships = this.randomDynamicBodies(
                1, assets.spaceship, AssetType.SPACESHIP);

        // WorldDefinition
        return new WorldDefinition(
                this.width, this.height,
                background, spaceDecorators, gravityBodies,
                asteroids, misils, bombs, spaceships, labs);
    }


    public ArrayList<DecoratorDef> randomDecorators(
            int num, AssetCatalog catalog, AssetType type) {

        double x, y, size, rotationDeg;
        int layer;
        String randomId;
        ArrayList<DecoratorDef> decos = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            x = rnd.nextDouble() * width;
            y = rnd.nextDouble() * height;
            size = 100;
            rotationDeg = 180;
            layer = 1;

            if (type == null) {
                randomId = catalog.randomId();
            } else {
                randomId = catalog.randomId(type);
            }

            decos.add(new DecoratorDef(
                    randomId, StaticShapeType.RECTANGLE, layer,
                    x, y, size, rotationDeg));
        }

        return decos;
    }


    public ArrayList<DynamicBodyDef> randomDynamicBodies(
            int num, AssetCatalog catalog, AssetType type) {

        double size;
        String randomId;
        ArrayList<DynamicBodyDef> dBodies = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            size = 100;

            if (type == null) {
                randomId = catalog.randomId();
            } else {
                randomId = catalog.randomId(type);
            }

            dBodies.add(
                    new DynamicBodyDef(randomId, StaticShapeType.RECTANGLE, size));
        }

        return dBodies;
    }


    public ArrayList<StaticBodyDef> randomStaticBodies(
            int num, AssetCatalog catalog, AssetType type) {

        double x, y, size, rotationDeg;
        String randomId;
        ArrayList<StaticBodyDef> sBodies = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            x = rnd.nextDouble() * width;
            y = rnd.nextDouble() * height;
            size = 100;
            rotationDeg = 180;

            if (type == null) {
                randomId = catalog.randomId();
            } else {
                randomId = catalog.randomId(type);
            }

            sBodies.add(new StaticBodyDef(
                    randomId, StaticShapeType.RECTANGLE,
                    x, y, size, rotationDeg));
        }

        return sBodies;
    }


    public BackgroundDef randomBackground() {
        String randomId = this.assets.backgrounds.randomId();

        return new BackgroundDef(randomId, 0.0d, 0.0d);
    }
}
