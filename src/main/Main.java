package main;


/**
 * TO-DO
 * =====
 * 0) Mostrar FPS
 * 1) Firing
 * 2) Colision detection 
 * 3) Basic Fx
 * 4) Create a new physic engine with a gravitational field
 * 5) Game rules
 * =====
 */
import assets.Assets;
import controller.Controller;
import generators.LifeGenerator;
import generators.WorldGenerator;
import model.Model;
import view.View;
import world.WorldDefinition;
import world.WorldDefinitionProvider;
import world.providers.RandomWorldDefinitionProvider;


/**
 *
 * @author juanm
 */
public class Main {

    public static void main(String[] args) {

        Assets assets = new Assets();

        WorldDefinitionProvider world
                = new RandomWorldDefinitionProvider(1150, 650, assets);

        WorldDefinition worldDef = world.provide();

        // Setting controller
        Controller controller = new Controller();
        controller.setAssets(assets);
        controller.setWorld(worldDef);
        controller.setWorldDimension(1150, 650);
        controller.setMaxDBody(200);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

        WorldGenerator worldGenerator = new WorldGenerator(controller, worldDef);
        LifeGenerator lifeGenerator = new LifeGenerator(
                controller, worldDef.asteroidsDef, 850, 12, 4, 1000, 10, 175, 0);

        lifeGenerator.activate();
    }
}
