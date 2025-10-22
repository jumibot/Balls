/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls;


import Balls.controller.Controller;
import Balls.view.View;
import Balls.model.Model;
import Helpers.State;
import Images.Images;


/**
 *
 * @author juanm
 */
public class MainTask {

    private static Controller controller;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        View view = new View();
        Model model = new Model(100);
        MainTask.controller = new Controller(view, model);
    }
}
