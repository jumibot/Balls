package balls.model;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import balls.controller.Controller;
import balls.physics.PhysicsValuesDTO;
import balls.view.RenderableObject;
import balls.model.ModelState;


/**
 *
 * @author juanm
 */
public class Model {

    private Controller controller = null;
    private ModelState state = ModelState.STARTING;
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


    public void doBallMovement(Ball ball, PhysicsValuesDTO phyValues) {
        ball.doMovement(phyValues);
    }


    public void doBallHoritzontalRebound(Ball ball, PhysicsValuesDTO phyValues) {
        ball.horizontalRebound();
    }


    public void doBallVerticalRebound(Ball ball, PhysicsValuesDTO phyValues) {
        ball.verticalRebound();
    }


    synchronized public ArrayList<RenderableObject> getRenderableObjects() {
        ArrayList<RenderableObject> renderableObjects
                = new ArrayList(Ball.getAliveQuantity() * 2);

        this.balls.forEach((id, ball) -> {
            renderableObjects.add(ball.getRenderableObject());
        });

        return renderableObjects;
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
        this.state = ModelState.ALIVE;
    }


    /**
     * PROTECTED
     */
    protected void detectEvents(Ball ballToCheck, PhysicsValuesDTO phyValues) {
        if (ballToCheck.getState() != BallState.ALIVE) {
            return;
        }

        ArrayList<Ball> ballsWithEvent = new ArrayList(4096);

        // Check movent is out of universe limits 
        // Check if object want to goin special areas
        // Check for collisions with other objects
        this.balls.forEach((key, ball) -> {
        });

        if (ballsWithEvent.size() > 0) {
            // this.controller.eventManagement(ballToCheck, ballsWithEvent);
        }
    }


    protected boolean isAlive() {
        return this.state == ModelState.ALIVE;
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
