/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls.controller;


import balls.view.RenderableObject;
import balls.view.View;
import balls.model.Model;
import balls.model.Ball;
import java.util.ArrayList;


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


    public ArrayList<RenderableObject> getRenderizableObjects() {
        return this.model.getRenderableObjects();
    }


    public void eventManagement(Ball mainBall, ArrayList<Ball> RelatedBalls) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
