package visual_objects;


import controller.Controller;
import model.Model;
import view.View;
import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class MainVisualObjects {

    public static void main(String[] args) {
        String assetsPath = "src/_images/assets/";
        ArrayList<String> backsFiles = new ArrayList<>();
        ArrayList<String> asteroidsFiles = new ArrayList<>();
        ArrayList<String> playersFiles = new ArrayList<>();

        backsFiles.add("background-1.png");
        backsFiles.add("background-2.jpeg");
        backsFiles.add("background-3.jpeg");
        backsFiles.add("background-4.jpeg");
        backsFiles.add("background-5.jpg");
        asteroidsFiles.add("asteroid-1-mini.png");
        asteroidsFiles.add("asteroid-2-mini.png");
        asteroidsFiles.add("asteroid-3-mini.png");
        asteroidsFiles.add("asteroid-4-mini.png");
        asteroidsFiles.add("asteroid-5-mini.png");
        asteroidsFiles.add("asteroid-6-mini.png");
        playersFiles.add("spaceship-1.png");
        playersFiles.add("spaceship-2.png");

        Controller controller = new Controller();
        controller.setAssets(assetsPath, backsFiles, asteroidsFiles, playersFiles);
        controller.setWorldDimension(1000, 600);
        controller.setMaxVisualObjects(9000);

        controller.setModel(new Model());
        controller.setView(new View());
        controller.activate();

        controller.generateRandomLife(
                3, // max creation delay
                1000, 10, // mass
                0, 0, // fixed speed
                0, 100, // fixed acc
                15, 2); // size
        
//        controller.generateRandomLife(
//                3, // max creation delay
//                1000, 10, // mass
//                100, 0, // acc max module, speed max module
//                30, 10); // size
    }
}
