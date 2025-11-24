package controller;


import controller.Controller;
import model.Model;
import view.View;
import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class Main {

    public static void main(String[] args) {
        String assetsPath = "";
        ArrayList<String> backsFiles = new ArrayList<>();
        ArrayList<String> asteroidsFiles = new ArrayList<>();
        ArrayList<String> playersFiles = new ArrayList<>();

        backsFiles.add("src/resources/images/backgrounds/space-high-intensity-1.png");
        backsFiles.add("src/resources/images/backgrounds/space-low-intensity-1.jpeg");
        backsFiles.add("src/resources/images/backgrounds/space-low-intensity-2.jpg");
        backsFiles.add("src/resources/images/backgrounds/space-medium-intensity-1.jpeg");
        backsFiles.add("src/resources/images/backgrounds/space-medium-intensity-2.jpeg");
        
        asteroidsFiles.add("src/resources/images/solid_bodies/asteroid-1-mini.png");
        asteroidsFiles.add("src/resources/images/solid_bodies/asteroid-2-mini.png");
        asteroidsFiles.add("src/resources/images/solid_bodies/asteroid-3-mini.png");
        asteroidsFiles.add("src/resources/images/solid_bodies/asteroid-4-mini.png");
        asteroidsFiles.add("src/resources/images/solid_bodies/asteroid-5-mini.png");
        asteroidsFiles.add("src/resources/images/solid_bodies/asteroid-6-mini.png");

        playersFiles.add("src/resources/images/spaceship/spaceship-1.png");
        playersFiles.add("src/resources/images/spaceship/spaceship-2.png");

        Controller controller = new Controller();
        controller.setAssets(assetsPath, backsFiles, asteroidsFiles, playersFiles);
        controller.setWorldDimension(1000, 600);
        controller.setMaxVisualObjects(5);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

        controller.generateRandomLife(
                3, // max creation delay
                1000, 10, // mass
                0, 0, // fixed speed
                0, 100, // fixed acc
                35, 2); // size
        
//        controller.generateRandomLife(
//                3, // max creation delay
//                1000, 10, // mass
//                100, 0, // acc max module, speed max module
//                30, 10); // size
    }
}
