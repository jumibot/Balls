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
        View view = new View();
        Model model = new Model(2, new DoubleVector(1000, 1000));
        MainBalls.controller = new Controller(view, model);
    }
}
