package main;

/**
 * TO-DO 
 * ===== 
 * 1) Improve unacopled architecture using mappers
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
import generators.LifeDTO;
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
                                3500 , // Max dynamic bodies
                                new View(), new Model(),
                                worldDef.gameAssets);

                controller.activate();

                WorldGenerator worldGenerator = new WorldGenerator(controller, worldDef);


                LifeDTO lifeConfig = new LifeDTO(
                                5, // maxCreationDelay
                                12, 5, // maxSize, minSize
                                1000, 10, // maxMass, minMass
                                175, // maxSpeedModule
                                0); // maxAccModule

                LifeGenerator lifeGenerator = new LifeGenerator(
                                controller,
                                worldDef.asteroids,
                                lifeConfig);

                lifeGenerator.activate();
        }
}