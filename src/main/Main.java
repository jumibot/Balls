package main;


/**
 * TO-DO:
 *
 * 3) Asteroids not rebound in limit of world -> Implementing entity die 
 * 4) Make spaceship controlable .> May be a new physic engine will be needed 
 * 5) Colision detection 
 * 6) Shotting 
 * 7) Basic Fx 
 * 8) Game rules 
 * 9) Create a new Physic engine with gravity field
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
                = new RandomWorldDefinitionProvider(1000, 600, assets);

        WorldDefinition worldDef = world.provide();

        // Setting controller
        Controller controller = new Controller();
        controller.setAssets(assets);
        controller.setWorld(worldDef);
        controller.setWorldDimension(1250, 750);
        controller.setMaxDBody(14);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

        WorldGenerator worldGenerator = new WorldGenerator(controller, worldDef);
        LifeGenerator lifeGenerator = new LifeGenerator(
                controller, worldDef.asteroids, 3000, 12, 4, 1000, 10, 100, 0);

        lifeGenerator.activate();
    }
}
