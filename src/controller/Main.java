package controller;

/**
 * TO-DO:
 * 
 * 1) Render static elements in world including dercoration
 * 2) Render spaceship
 * 3) Asteroids not rebound in limit of world -> Implementing entity die
 * 4) Make spaceship controlable .> May be a new physic engine will be needed
 * 5) Colision detection
 * 6) Shotting
 * 7) Basic Fx
 * 8) Game rules
 * 9) Create a new Physic engine with gravity field
 * 
 * 
 */
import assets.Assets;
import controller.Controller;
import model.Model;
import view.View;
import worlds.WorldDefinition;
import worlds.WorldDefinitionProvider;
import worlds.providers.RandomWorldDefinitionProvider;


/**
 *
 * @author juanm
 */
public class Main {

    public static void main(String[] args) {

        Assets assets = new Assets();

        WorldDefinitionProvider world = 
                new RandomWorldDefinitionProvider(1000, 600, assets);
        WorldDefinition worldDef = world.provide();
        
        Controller controller = new Controller();
        controller.setAssets(assets);
        controller.setWorld(worldDef);
        controller.setWorldDimension(1000, 600);
        controller.setMaxVisualObjects(10);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

//        controller.generateRandomLife(
//                3, // max creation delay
//                1000, 10, // mass
//                300, -20, // fixed speed
//                10, 50, // fixed acc
//                15, 5); // size

        controller.generateRandomLife(
                3, // max creation delay
                1000, 10, // mass
                200, 0, // acc max module, speed max module
                20, 2); // size
    }
}
