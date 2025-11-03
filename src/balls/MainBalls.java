/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls;


import balls.controller.Controller;
import balls.model.Model;
import balls.view.View;
import helpers.DoubleVector;


/**
 *
 * @author juanm
 */
public class MainBalls {

    private static Controller controller;


    public static void main(String[] args) {
        View view = new View();
        Model model = new Model(2, new DoubleVector(1000, 1000));
        MainBalls.controller = new Controller(view, model);
    }
}
