/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.model;


import Balls.controller.Controller;
import Balls.dto.VisualBallCatalogDto;
import Helpers.State;
import java.util.HashMap;


/**
 *
 * @author juanm
 */
public class Model {

    private final HashMap<Integer, Ball> balls;
    private Controller controller;
    private State state;
    private final int maxBallsQuantity;
    private VisualBallCatalogDto visualBallSnapshot;


    /**
     * CONSTRUCTORS
     */
    public Model(int maxBallsQuantity) {
        this.maxBallsQuantity = maxBallsQuantity;
        this.state = State.STARTING;
        this.controller = null;
        this.balls = new HashMap<>();
    }


    /**
     * PUBLIC
     */
    synchronized public boolean addBall(Ball newBall) {
        if (this.maxBallsQuantity >= Ball.getAliveQuantity()) {
            System.out.println("Max balls quantity reached · Model");
            return false; // =======================================>
        }

        this.balls.put(newBall.getId(), newBall);
        newBall.setModel(this);
        newBall.activate();

        // TO-DO -> Update snapshot
        return true;
    }


    synchronized public VisualBallCatalogDto getVisualBallSnapshot() {
        return this.visualBallSnapshot;
    }


    public boolean isAlive() {
        return this.state == State.ALIVE;
    }


    synchronized public void removeBall(Ball ball) {
        this.balls.remove(ball.getId());
        ball.die();

        // TO-DO -> Update snapshot
    }


    public void setController(Controller controller) {
        this.controller = controller;
        this.state = State.ALIVE;
    }

    
    /**
     * PRIVATES
     */
    private void updateVisualBallSnapshot() {
        // TO-DO ALL ... :-O
        // Recorrer todas las bolas, seleccionar las que están vivas
        // y añadir referencias en el sanpshot hacia su visualObject
        //
    }
    
    /**
     * STATIC METHODES
     */
    public static long getAlivedBallsQuantity() {
        return Ball.getAliveQuantity();
    }


    public static long getCreatedBallsQuantity() {
        return Ball.getCreatedQuantity();
    }


    public static long getDeadBallsQuantity() {
        return Ball.getDeadQuantity();
    }
}
