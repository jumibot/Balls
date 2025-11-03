package balls.model;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import balls.controller.Controller;
import balls.physics.PhysicsValuesDTO;
import balls.view.RenderableObject;
import helpers.DoubleVector;


/**
 *
 * @author juanm
 */
public class Model {

    private final int maxBalls;
    private final DoubleVector wordDimension;

    private Controller controller = null;
    private BallGenerator ballGenerator = null;
    private ModelState state = ModelState.STARTING;
    private Map<Integer, Ball> balls = new ConcurrentHashMap<>(4096);


    /**
     * CONSTRUCTORS
     */
    public Model(int maxBallsQuantity, DoubleVector wordDimension) {
        this.maxBalls = maxBallsQuantity;
        this.wordDimension = wordDimension;

        this.ballGenerator = new BallGenerator(
                this, // Model
                400, 1, // Mass range
                300,
                0.001, // Max acceleration in px X millisecond^-2
                0.2, // MaxSpeed,
                40, 1 // Size range in px
        );

        this.ballGenerator.activate();
    }


    /**
     * PUBLIC
     */
    synchronized public boolean addBall(Ball newBall) {
        if (Ball.getAliveQuantity() >= this.maxBalls) {
            System.out.println("Max balls quantity reached · Model");
            return false; // =======================================>
        }

        newBall.setModel(this);
        if (!newBall.activate()) {
            System.out.println("Can not activate ball <" + newBall.getId() + "> · Model");
            return false;

        }
        System.out.println("Ball <" + newBall.getId() + "> Activated · Model");
        this.balls.put(newBall.getId(), newBall);

        return true;
    }


    public int getMaxBalls() {
        return this.maxBalls;
    }


    synchronized public ArrayList<RenderableObject> getRenderableObjects() {
        ArrayList<RenderableObject> renderableObjects
                = new ArrayList(Ball.getAliveQuantity() * 2);

        this.balls.forEach((id, ball) -> {
            renderableObjects.add(ball.getRenderableObject());
        });

        return renderableObjects;
    }


    public ModelState getState() {
        return this.state;
    }


    public DoubleVector getWorldDimension() {
        return this.wordDimension;
    }


    public boolean isAlive() {
        return this.state == ModelState.ALIVE;
    }


    public void killBall(int ballId) {
        /* 
        TO-DO:
        Change ball state to finalize de thread execution
        Remove ball from model
         */
    }


    public void newRandomBall() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void setController(Controller controller) {
        this.controller = controller;
        this.state = ModelState.ALIVE;
    }


    /**
     * PROTECTED
     */
    protected void processBallEvents(Ball ballToCheck, PhysicsValuesDTO phyValues) {
        if (ballToCheck.getState() != BallState.ALIVE) {
            return; // To avoid duplicate or unnecesary event processing ======>
        }

        BallState previousState = ballToCheck.getState();
        ballToCheck.setState(BallState.HANDS_OFF);
        EventType limitEvent = EventType.NONE;

        try {
            limitEvent = this.checkLimitEvent(phyValues);

            if (limitEvent != EventType.NONE) {
                System.err.println("Limit Event " + ballToCheck + " · Model"); //*+
                this.doBallAction(
                        this.controller.decideAction(
                                limitEvent), ballToCheck, phyValues);
            }
        } catch (Exception e) {
            // Fallback anti-zombi: If exception ocurrs back to previous state
            if (ballToCheck.getState() == BallState.HANDS_OFF) {
                ballToCheck.setState(previousState);
            }

        } finally {
            if (ballToCheck.getState() == BallState.HANDS_OFF) {
                ballToCheck.setState(BallState.ALIVE);
            }
        }

        if (limitEvent != EventType.NONE) {
            return; // ========================================================>
        }

        this.doBallAction(BallAction.MOVE, ballToCheck, phyValues);

        // 2 - Check if object want to go inside special areas
        // 3 - Check for collisions with other objects
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


    private EventType checkLimitEvent(PhysicsValuesDTO phyValues) {
        // Check if movement is out of world limits
        //     In a corner only one event is considered. 
        //     The order of conditions defines the event priority.

        if (phyValues.position.getX() < 0) {
            return (EventType.WEST_LIMIT_REACHED);
        } else if (phyValues.position.getX() >= this.wordDimension.getX()) {
            return (EventType.EAST_LIMIT_REACHED);
        } else if (phyValues.position.getY() < 0) {
            return (EventType.NORTH_LIMIT_REACHED);
        } else if (phyValues.position.getY() >= this.wordDimension.getY()) {
            return (EventType.SOUTH_LIMIT_REACHED);
        }

        return EventType.NONE;
    }


    private void doBallAction(BallAction ballAction, Ball ball, PhysicsValuesDTO phyNewValues) {
        switch (ballAction) {
            case MOVE:
                ball.doMovement(phyNewValues);
                ball.setState(BallState.ALIVE);
                break;

            case VERTICAL_REBOUND:
                ball.doVerticalRebound(phyNewValues);
                ball.setState(BallState.ALIVE);
                break;

            case HORIZONTAL_REBOUND:
                ball.doHorizontalRebound(phyNewValues);
                ball.setState(BallState.ALIVE);
                break;

            case DIE:
                this.killBall(ball);
                ball.setState(BallState.DEAD);
                break;

            case TRY_TO_GO_INSIDE:
            case EXPLODE_IN_FRAGMENTS:
                // to-do
                ball.setState(BallState.ALIVE);
                break;

            default:
                // To avoid zombie state
                ball.setState(BallState.ALIVE);

        }

    }


    private void killBall(Ball ball) {
        /* 
        TO-DO:
        Change ball state to finalize de thread execution
        Remove ball from model
         */
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
