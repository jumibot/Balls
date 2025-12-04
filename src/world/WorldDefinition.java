package world;


import java.util.ArrayList;


public class WorldDefinition {

    public final int worldWidth;
    public final int worldHeight;

    public final BackgroundDef backgroundDef;
    public final ArrayList<DecoratorDef> spaceDecoratorsDef;
    public final ArrayList<StaticBodyDef> gravityBodiesDef;
    public final ArrayList<DynamicBodyDef> asteroidsDef;
    public final ArrayList<DynamicBodyDef> misilsDef;
    public ArrayList<StaticBodyDef> bombsDef;
    public ArrayList<DynamicBodyDef> spaceshipsDef;


    public WorldDefinition(
            int worldWidth,
            int worldHeight,
            BackgroundDef background,
            ArrayList<DecoratorDef> spaceDecorators,
            ArrayList<StaticBodyDef> gravityBodies,
            ArrayList<DynamicBodyDef> asteroids,
            ArrayList<DynamicBodyDef> misils,
            ArrayList<StaticBodyDef> bombs,
            ArrayList<DynamicBodyDef> spaceships) {

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.backgroundDef = background;
        this.spaceDecoratorsDef = spaceDecorators;
        this.gravityBodiesDef = gravityBodies;
        this.asteroidsDef = asteroids;
        this.misilsDef = misils;
        this.bombsDef = bombs;
        this.spaceshipsDef = spaceships;
    }
}
