package main;


/**
 * TO-DO 
 * ===== 
 * 0) Include all the assets in the same Images instance
 * 1) More weapon types like missils and shoot in burts 
 * 2) Colision detection 
 * 3) Basic Fx 
 * 4) Create a new physic engine with a gravitational field 
 * 5) Game rules 
 * =====
 */
import assets.ProjectAssets;
import controller.Controller;
import generators.LifeGenerator;
import generators.WorldGenerator;
import model.Model;
import view.View;
import world.WorldDefinition;
import world.WorldDefinitionProvider;
import world.providers.RandomWorldDefinitionProvider;


public class Main {

    public static void main(String[] args) {

        ProjectAssets assets = new ProjectAssets();

        WorldDefinitionProvider world
                = new RandomWorldDefinitionProvider(1150, 650, assets);

        WorldDefinition worldDef = world.provide();

        Controller controller = new Controller();
        controller.setAssets(assets);
        controller.setWorld(worldDef);
        controller.setWorldDimension(1150, 650);
        controller.setMaxDBody(500);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

        WorldGenerator worldGenerator = new WorldGenerator(controller, worldDef);
        LifeGenerator lifeGenerator = new LifeGenerator(
                controller, worldDef.asteroidsDef, 1500, 11, 3, 1000, 10, 175, 0);

        lifeGenerator.activate();
    }
}
