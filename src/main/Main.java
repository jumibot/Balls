package main;


/**
 * TO-DO:
 *
 * 3) Make spaceship controlable ··> A new physic engine may be needed 
 * 6) Firing
 * 2) Colision detection 
 * 7) Basic Fx
 * 9) Create a new physic engine with a gravitational field
 * 8) Game rules
 * 
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
        controller.setMaxDBody(30);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

        WorldGenerator worldGenerator = new WorldGenerator(controller, worldDef);
        LifeGenerator lifeGenerator = new LifeGenerator(
                controller, worldDef.asteroids, 1000, 12, 4, 1000, 10, 250, 0);

        lifeGenerator.activate();
    }
}
