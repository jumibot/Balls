package generators;


import _helpers.DoubleVector;
import controller.Controller;
import java.util.ArrayList;
import java.util.Random;
import world.DecoratorDef;
import world.DynamicBodyDef;
import world.StaticBodyDef;
import world.WorldDefinition;


public class WorldGenerator {

    private final Random rnd = new Random();

    private final Controller controller;
    private final ArrayList<DecoratorDef> spaceDecoratorsDef;
    private final ArrayList<StaticBodyDef> gravityBodiesDef;
    private final ArrayList<StaticBodyDef> bombsDef;
    private final ArrayList<DynamicBodyDef> spaceshipsDef;


    public WorldGenerator(Controller controller, WorldDefinition worldDef) {
        this.controller = controller;
        this.gravityBodiesDef = worldDef.gravityBodiesDef;
        this.bombsDef = worldDef.bombsDef;
        this.spaceshipsDef = worldDef.spaceshipsDef;
        this.spaceDecoratorsDef = worldDef.spaceDecoratorsDef;

        this.createWorld();
    }


    private void createWorld() {
        this.createSpaceDecorators(this.spaceDecoratorsDef);
        this.createSBodies(this.gravityBodiesDef);
        this.createSBodies(this.bombsDef);
        this.createPlayers(this.spaceshipsDef);
    }


    private void createSBodies(ArrayList<StaticBodyDef> sBodies) {
        for (StaticBodyDef body : sBodies) {
            this.controller.addSBody(body.assetId, body.size, body.posX, body.posY, body.angle);
        }
    }


    private void createSpaceDecorators(ArrayList<DecoratorDef> decorators) {

        for (DecoratorDef deco : decorators) {
            this.controller.addDecorator(deco.assetId, deco.size, deco.posX, deco.posY, deco.angle);
        }
    }


    private void createPlayers(ArrayList<DynamicBodyDef> dBodies) {
        String playerId = null;

        for (DynamicBodyDef body : dBodies) {
            DoubleVector pos = this.randomPosition();
            playerId = this.controller.addPlayer(
                    body.assetId, body.size, 600, 300, 0, 0, 0, 0, 0);
//                    body.assetId, body.size, pos.x, pos.y, 0, 0, 0, 0, body.angle);
        }

        if (playerId != null) {
            this.controller.setLocalPlayer(playerId);
        }
    }


    private DoubleVector randomPosition() {
        double x, y;

        // Recuperar tamaño del mundo establecido en el modelo
        x = this.rnd.nextFloat() * this.controller.getWorldDimension().width;
        y = this.rnd.nextFloat() * this.controller.getWorldDimension().height;

        return new DoubleVector(x, y);
    }
}
