package worlds;


import java.util.List;


public class WorldDefinition {

    public final int worldWidth;
    public final int worldHeight;

    public final BackgroundDef background;
    public final List<DecoratorDef> spaceDecorators;
    public final List<StaticBodyDef> gravityBodies;
    public final List<StaticBodyDef> solidBodies;
    public final DynamicBodyDef misil;
    public final StaticBodyDef bomb;
    public final DynamicBodyDef spaceship;
    public final DecoratorDef lab;


    public WorldDefinition(
            int worldWidth,
            int worldHeight,
            BackgroundDef background,
            List<DecoratorDef> spaceDecorators,
            List<StaticBodyDef> gravityBodies, 
            List<StaticBodyDef> solidBodies,
            DynamicBodyDef misil,
            StaticBodyDef bomb,
            DynamicBodyDef spaceship,
            DecoratorDef lab) {

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.background = background;
        this.spaceDecorators = spaceDecorators;
        this.gravityBodies = gravityBodies;
        this.solidBodies = solidBodies;
        this.misil = misil;
        this.bomb = bomb;
        this.spaceship = spaceship;
        this.lab = lab;
    }
}
