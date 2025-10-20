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
import Images.Images;
import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class Controller {

    Model model;
    View view;
    Images ballImages;


    public Controller(View view, Model model) {
        this.loadImages();

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
        return this.model.getVisualBallSnapshot();
    }


    /**
     * PRIVATES
     */
    private void loadImages() {
        this.ballImages = new Images("src/tg/images/assets/");
        this.ballImages.addImageToManifest("asteroid-1-mini.png");
        this.ballImages.addImageToManifest("asteroid-2-mini.png");
        this.ballImages.addImageToManifest("spaceship-1.png");
        this.ballImages.addImageToManifest("spaceship-2.png");
    }
}
