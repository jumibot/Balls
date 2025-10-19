/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.model;


import Balls.controller.Controller;
import Balls.controller.Controller;
import Balls.model.Ball;
import Helpers.RandomArrayList;
import Helpers.State;
import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class Model {

    private final ArrayList<Ball> balls;
    private Controller controller;
    private State state;
    private long maxBallsQuantity;


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


    /**
     * CONSTRUCTORS
     */
    public Model(long maxBallsQuantity) {
        this.maxBallsQuantity = maxBallsQuantity;
        this.state = State.STARTING;
        this.controller = null;
        this.balls = new ArrayList<>();
    }


    public Model() {
        this.maxBallsQuantity = 25;
        this.state = State.STARTING;
        this.controller = null;
        this.balls = new ArrayList<>();

    }


    /**
     * PUBLIC
     */
    synchronized public boolean addBall(Ball newBall) {
        if (Ball.getAliveQuantity() > this.maxBallsQuantity) {
            return false;
        }

        this.balls.add(newBall);
        newBall.setModel(this);

        newBall.activate();
        return true;
    }


    synchronized public RandomArrayList<Ball> getBalls() {
        return (RandomArrayList) this.balls.clone();
    }


    public boolean isAlive() {
        return this.state == State.ALIVE;
    }


    synchronized public void removeBall(Ball ball) {
        this.balls.remove(ball);
        ball.die();
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }
}
