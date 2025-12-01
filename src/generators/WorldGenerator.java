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
    private final ArrayList<DecoratorDef> spaceDecorators;
    private final ArrayList<StaticBodyDef> gravityBodies;
    private final ArrayList<StaticBodyDef> bombs;
    private final ArrayList<DynamicBodyDef> spaceships;


    public WorldGenerator(Controller controller, WorldDefinition worldDef) {
        this.controller = controller;
        this.gravityBodies = worldDef.gravityBodies;
        this.bombs = worldDef.bombs;
        this.spaceships = worldDef.spaceships;
        this.spaceDecorators = worldDef.spaceDecorators;

        this.createWorld();
    }


    private void createWorld() {
        this.createSpaceDecorators(this.spaceDecorators);
        this.createSBodies(this.gravityBodies);
        this.createSBodies(this.bombs);
        this.createSpaceships(this.spaceships);
    }


    private void createSBodies(ArrayList<StaticBodyDef> sBodies) {
        for (StaticBodyDef body : sBodies) {
            this.controller.addSBody(body.assetId, body.size, body.posX, body.posY, body.angle);
        }
    }


    private void createSpaceDecorators(ArrayList<DecoratorDef> decorators) {

        for (DecoratorDef deco : decorators) {
            this.controller.addDecorator(
                    deco.assetId, deco.size, deco.pos_x, deco.pos_y, deco.angle);
        }
    }


    private void createSpaceships(ArrayList<DynamicBodyDef> dBodies) {
        for (DynamicBodyDef body : dBodies) {

            DoubleVector pos = this.randomPosition();
            this.controller.addDBody(
                    body.assetId, 25, pos.x, pos.y, 0, 0, 0, 0, body.angle);
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
