package world;


import java.util.ArrayList;


public class WorldDefinition {

    public final int worldWidth;
    public final int worldHeight;

    public final BackgroundDef background;
    public final ArrayList<DecoratorDef> spaceDecorators;
    public final ArrayList<StaticBodyDef> gravityBodies;
    public final ArrayList<DynamicBodyDef> asteroids;
    public final ArrayList<DynamicBodyDef> misils;
    public ArrayList<StaticBodyDef> bombs;
    public ArrayList<DynamicBodyDef> spaceships;


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
        this.background = background;
        this.spaceDecorators = spaceDecorators;
        this.gravityBodies = gravityBodies;
        this.asteroids = asteroids;
        this.misils = misils;
        this.bombs = bombs;
        this.spaceships = spaceships;
    }
}
