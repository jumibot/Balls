/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.controller;


import Balls.dto.VisualBallCatalogDto;
import Balls.view.View;
import Balls.model.Model;
import Balls.model.Ball;
import Helpers.Position;
import Images.Images;


/**
 *
 * @author juanm
 */
public class Controller {

    Model model;
    View view;


    public Controller(View view, Model model) {

        this.model = model;
        this.model.setController(this);

        this.view = view;
        this.view.setController(this);

        /* TO-DO -> Arrancar el generador de bolas */
    }


    /**
     * PUBLICS
     */
    public void addBall(Ball newBall) {
        this.model.addBall(newBall);
    }


    public VisualBallCatalogDto getVisualBallSnapshot() {
        return this.model.getVisualBalls();
    }


    public Position getBallPosition() {
        return this.model.getBallPosition();
    }
}
