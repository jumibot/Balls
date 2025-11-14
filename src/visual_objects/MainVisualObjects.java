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
        Dimension worlDim = new Dimension(1000, 600);
        View view = new View(worlDim);
        Model model = new Model(50, worlDim);
        MainVisualObjects.controller = new Controller(view, model);
    }
}
