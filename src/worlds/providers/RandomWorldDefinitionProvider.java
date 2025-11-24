package worlds.providers;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import worlds.BackgroundDef;
import worlds.DecoratorDef;
import worlds.DynamicBodyDef;
import worlds.StaticBodyDef;
import worlds.StaticShapeType;
import worlds.WorldDefinition;
import worlds.WorldDefinitionProvider;


public class RandomWorldDefinitionProvider implements WorldDefinitionProvider {

    private final Random rnd = new Random();
    private final int width;
    private final int height;


    public RandomWorldDefinitionProvider(int worldWidth, int worldHeight) {
        this.width = worldWidth;
        this.height = worldHeight;
    }


    @Override
    public WorldDefinition provide(int worldWidth, int worldHeight) {

        BackgroundDef background = getRandomBackground();

        List<DecoratorDef> spaceDecorators = this.getRandomSpaceDecorators();

        List<StaticBodyDef> gravityBodies = this.getRandomGravityBodies();

        List<StaticBodyDef> solidBodies = this.getRandomSolidBodies();

        DynamicBodyDef misil = this.getRandomMisil();

        StaticBodyDef bomb = this.getRandomBomb();

        DynamicBodyDef spaceship = this.getRandomSpaceship();

        DecoratorDef lab = this.getRandomLab();

        // WorldDefinition
        return new WorldDefinition(
                this.width, this.height,
                background, spaceDecorators, gravityBodies, solidBodies,
                misil, bomb, spaceship, lab);
    }


    public BackgroundDef getRandomBackground() {
        return new BackgroundDef("bg_space_01", 0.0d, 0.0d);
    }


    public StaticBodyDef getRandomBomb() {
        StaticBodyDef staticBody;
        double x, y, size, rotationDeg;

        x = 100;
        y = 100;
        size = 100;
        rotationDeg = 180;

        staticBody = new StaticBodyDef("floor", StaticShapeType.RECTANGLE,
                                       x, y, size, rotationDeg);

        return staticBody;
    }


    public List<StaticBodyDef> getRandomGravityBodies() {
        List<StaticBodyDef> statics = new ArrayList<>();
        double x, y, size, rotationDeg;

        x = 100;
        y = 100;
        size = 100;
        rotationDeg = 180;

        statics.add(new StaticBodyDef(
                "floor", StaticShapeType.RECTANGLE,
                x, y, size, rotationDeg));

        return statics;
    }


    public DecoratorDef getRandomLab() {
        DecoratorDef decorator = null;
        double x, y, size, rotationDeg;
        int layer;

        for (int i = 0; i < 10; i++) {
            x = rnd.nextDouble() * width;
            y = rnd.nextDouble() * height;
            layer = 1;
            size = 100;
            rotationDeg = 180;

            decorator = new DecoratorDef(
                    "dec_rock_01", StaticShapeType.RECTANGLE, layer,
                    x, y, size, rotationDeg);
        }

        return decorator;
    }


    public DynamicBodyDef getRandomMisil() {
        DynamicBodyDef dynamic;
        double x, y, size, rotationDeg;

        x = 100;
        y = 100;
        size = 100;
        rotationDeg = 180;

        dynamic = new DynamicBodyDef(
                "floor", StaticShapeType.RECTANGLE,
                x, y, size, rotationDeg);

        return dynamic;
    }


    public List<StaticBodyDef> getRandomSolidBodies() {
        List<StaticBodyDef> statics = new ArrayList<>();
        double x, y, size, rotationDeg;

        x = 100;
        y = 100;
        size = 100;
        rotationDeg = 180;

        statics.add(new StaticBodyDef(
                "floor", StaticShapeType.RECTANGLE,
                x, y, size, rotationDeg));

        return statics;
    }


    public List<DecoratorDef> getRandomSpaceDecorators() {
        List<DecoratorDef> decorators = new ArrayList<>();
        double x, y, size, rotationDeg;
        int layer;

        for (int i = 0; i < 10; i++) {
            x = rnd.nextDouble() * width;
            y = rnd.nextDouble() * height;
            layer = 1;
            size = 100;
            rotationDeg = 180;

            decorators.add(new DecoratorDef(
                    "dec_rock_01", StaticShapeType.RECTANGLE, layer,
                    x, y, size, rotationDeg));
        }

        return decorators;
    }


    public DynamicBodyDef getRandomSpaceship() {
        DynamicBodyDef dynamic;
        double x, y, size, rotationDeg;

        x = 100;
        y = 100;
        size = 100;
        rotationDeg = 180;

        dynamic = new DynamicBodyDef(
                "floor", StaticShapeType.RECTANGLE,
                x, y, size, rotationDeg);

        return dynamic;
    }

}
