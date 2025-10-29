package balls.model;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import balls.controller.Controller;
import balls.physics.PhysicalValuesDTO;
import balls.view.RenderizableObject;
import Helpers.State;


/**
 *
 * @author juanm
 */
public class Model {

    private Controller controller = null;
    private State state = State.STARTING;
    private final int maxBallsQuantity;
    private Map<Integer, Ball> balls = new ConcurrentHashMap<>(4096);


    /**
     * CONSTRUCTORS
     */
    public Model(int maxBallsQuantity) {
        this.maxBallsQuantity = maxBallsQuantity;
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

        return true;
    }


    public void doBallMovement(Ball ball, PhysicalValuesDTO phyValues) {
        ball.doMovement(phyValues);
    }


    public void doBallVerticalRebound(Ball ball, PhysicalValuesDTO phyValues) {
        ball.verticalRebound();
    }


    public void doBallHoritzontalRebound(Ball ball, PhysicalValuesDTO phyValues) {
        ball.horizontalRebound();
    }


    synchronized public ArrayList<RenderizableObject> getRenderizableObjects() {
        ArrayList<RenderizableObject> renderizableObjects
                = new ArrayList(Ball.getAliveQuantity() * 2);

        this.balls.forEach((id, ball) -> {
            renderizableObjects.add(ball.getRenderizableObject());
        });

        return renderizableObjects;
    }


    public void killBall(Ball ball) {
        /* 
        TO-DO:
        Change ball state to finalize de thread execution
        Remove ball from model
         */
    }


    public void setController(Controller controller) {
        this.controller = controller;
        this.state = State.ALIVE;
    }


    /**
     * PROTECTED
     */
    protected void eventDetection(Ball ballToCheck, PhysicalValuesDTO phyValues) {
        if (ballToCheck.getState() != BallState.ALIVE) {
            return;
        }

        ArrayList<Ball> ballsWithEvent = new ArrayList(4096);

        // Check limits
        // Check for events with other objects
        this.balls.forEach((key, ball) -> {
        });

        if (ballsWithEvent.size() > 0) {
            // this.controller.eventManagement(ballToCheck, ballsWithEvent);
        }
    }


    protected boolean isAlive() {
        return this.state == State.ALIVE;
    }


    /**
     * PRIVATE
     */
    synchronized private void removeBall(Ball ball) {
        if (this.balls.remove(ball.getId()) == null) {
            return; // ======= Elmento no esta en la lista ========>
        }
        ball.die();
    }


    /**
     * STATIC
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
