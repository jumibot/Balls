/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls;


import balls.controller.Controller;
import balls.view.View;
import balls.model.Model;
import Helpers.State;
import Images.Images;


/**
 *
 * @author juanm
 */
public class MainBalls {

    private static Controller controller;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        View view = new View();
        Model model = new Model(100);
        MainBalls.controller = new Controller(view, model);
    }
}
