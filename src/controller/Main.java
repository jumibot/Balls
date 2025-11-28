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
 * 9) Create a new
 * 10) Physic engine with gravity field
 */
import assets.Assets;
import controller.Controller;
import generators.AsteroidGenerator;
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

        Controller controller = new Controller();
        controller.setAssets(assets);
        controller.setWorld(worldDef);
        controller.setWorldDimension(1000, 600);
        controller.setMaxVisualObjects(10);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

//        AsteroidGenerator aGenerator = new AsteroidGenerator(
//                controller, worldDef.asteroids, 3, 25, 3, 1000, 10, 0, 0);

        AsteroidGenerator aGenerator = new AsteroidGenerator(
                controller, worldDef.asteroids, 3, 25, 3, 1000, 10, 145, 0);

        aGenerator.activate();
    }
}
