package main;


/**
 * TO-DO 
 * ===== 
 * 1) Implment weapon burts 
 * 2) Full implemention of Factory pattern for weapons, physics, worlds, entities, ...
 * 2) Colision detection 
 * 3) Basic Fx 
 * 4) Create a new physic engine with a gravitational field 
 * 5) Game rules
 * 6) Comms
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

        ProjectAssets projectAssets = new ProjectAssets();

        WorldDefinitionProvider world
                = new RandomWorldDefinitionProvider(1150, 650, projectAssets);

        WorldDefinition worldDef = world.provide();

        Controller controller = new Controller(
                1150, 650, // World dimensions
                5000, // Max dynamic bodies
                new View(), new Model(),
                worldDef.gameAssets);

        controller.activate();

        WorldGenerator worldGenerator = new WorldGenerator(controller, worldDef);
        LifeGenerator lifeGenerator = new LifeGenerator(
                controller, worldDef.asteroidsDef, 150, 11, 3, 1000, 10, 175, 0);

        lifeGenerator.activate();
    }
}
