package visual_objects;


import controller.Controller;
import model.Model;
import view.View;
import java.awt.Dimension;


/**
 *
 * @author juanm
 */
public class MainVisualObjects {

    private static Controller controller;


    public static void main(String[] args) {
        Dimension worldDim = new Dimension(1000, 600);
        View view = new View(worldDim);
        Model model = new Model(5000, worldDim);
        MainVisualObjects.controller = new Controller(view, model);
    }
}
