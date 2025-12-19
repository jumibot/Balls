package main;

/**
 * TO-DO 
 * ===== 
 * 1) Implement creation of different weapon types from config in world generator, controller and model
 * 2) Full implemention of Factory pattern for entities, ...
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

                System.setProperty("sun.java2d.uiScale", "1.0");
                int worldWidth = 2450;
                int worldHeight = 1450;

                ProjectAssets projectAssets = new ProjectAssets();

                WorldDefinitionProvider world = new RandomWorldDefinitionProvider(worldWidth, worldHeight,
                                projectAssets);

                WorldDefinition worldDef = world.provide();

                Controller controller = new Controller(
                                worldWidth, worldHeight, // World dimensions
                                5000, // Max dynamic bodies
                                new View(), new Model(),
                                worldDef.gameAssets);

                controller.activate();

                WorldGenerator worldGenerator = new WorldGenerator(controller, worldDef);

                int maxCreationDelay = 500;
                int maxSize = 20;
                int minSize = 3;
                double maxMass = 1000;
                double minMass = 10;
                int maxSpeedModule = 175;
                int maxAccModule = 0;

                LifeGenerator lifeGenerator = new LifeGenerator(
                                controller,
                                worldDef.asteroidsDef,
                                maxCreationDelay,
                                maxSize, minSize,
                                maxMass, minMass,
                                maxSpeedModule, maxAccModule);

                lifeGenerator.activate();
        }
}
