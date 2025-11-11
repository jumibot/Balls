/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        Model model = new Model(100, worlDim);
        MainVisualObjects.controller = new Controller(view, model);
    }
}
