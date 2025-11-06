/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls;


import controller.Controller;
import model.Model;
import view.View;
import _helpers.DoubleVector;


/**
 *
 * @author juanm
 */
public class MainBalls {

    private static Controller controller;


    public static void main(String[] args) {
        DoubleVector worldDimension = new DoubleVector(800,800);
        View view = new View(worldDimension);
        Model model = new Model(2, worldDimension);
        MainBalls.controller = new Controller(view, model);
    }
}
