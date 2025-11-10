/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;


import controller.Controller;
import model.Model;
import view.View;
import _helpers.DoubleVector;


/**
 *
 * @author juanm
 */
public class MainObjects {

    private static Controller controller;


    public static void main(String[] args) {
        DoubleVector worldDimension = new DoubleVector(1000,600);
        View view = new View(worldDimension);
        Model model = new Model(50, worldDimension);
        MainObjects.controller = new Controller(view, model);
    }
}
