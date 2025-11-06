package model;


import model.object.ObjectGenerator;
import model.object.ObjectAction;
import model.object.ObjectState;
import model.object.Object;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import model.physics.PhysicsValuesDTO;
import view.RenderableObject;
import _helpers.DoubleVector;


/**
 *
 * @author juanm
 */
public class Model {

    private final int maxBalls;
    private final DoubleVector wordDimension;

    private Controller controller = null;
    private ObjectGenerator ballGenerator = null;
    private ModelState state = ModelState.STARTING;
    private Map<Integer, Object> balls = new ConcurrentHashMap<>(4096);


    /**
     * CONSTRUCTORS
     */
    public Model(int maxBallsQuantity, DoubleVector wordDimension) {
        this.maxBalls = maxBallsQuantity;
        this.wordDimension = wordDimension;

        this.ballGenerator = new ObjectGenerator(
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
    synchronized public boolean addBall(Object newBall) {
        if (Object.getAliveQuantity() >= this.maxBalls) {
            // Max balls quantity reached
            return false; // =================================================>
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
                = new ArrayList(Object.getAliveQuantity() * 2);

        this.balls.forEach((id, ball) -> {
            if (ball.getRenderableObject() != null) {
                renderableObjects.add(ball.getRenderableObject());
            }
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
    public void processBallEvents(
            Object ballToCheck,
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues) {

        if (ballToCheck.getState() != ObjectState.ALIVE) {
            return; // To avoid duplicate or unnecesary event processing ======>
        }

        ObjectState previousState = ballToCheck.getState();
        ballToCheck.setState(ObjectState.HANDS_OFF);
        ObjectEventType limitEvent = ObjectEventType.NONE;

        try {
            limitEvent = this.checkLimitEvent(newPhyValues);

            if (limitEvent != ObjectEventType.NONE) {
                this.doBallAction(
                        this.controller.decideAction(limitEvent),
                        ballToCheck,
                        newPhyValues,
                        oldPhyValues);
            }
        } catch (Exception e) {
            // Fallback anti-zombi: If exception ocurrs back to previous state
            if (ballToCheck.getState() == ObjectState.HANDS_OFF) {
                ballToCheck.setState(previousState);
            }

        } finally {
            if (ballToCheck.getState() == ObjectState.HANDS_OFF) {
                ballToCheck.setState(ObjectState.ALIVE);
            }
        }

        if (limitEvent != ObjectEventType.NONE) {
            return; // ========================================================>
        }

        this.doBallAction(ObjectAction.MOVE, ballToCheck, newPhyValues, oldPhyValues);

        // 2 - Check if object want to go inside special areas
        // 3 - Check for collisions with other objects
    }


    /**
     * PRIVATE
     */
    synchronized private void removeBall(Object ball) {
        if (this.balls.remove(ball.getId()) == null) {
            return; // ======= Elmento no esta en la lista ========>
        }
        ball.die();
    }


    private ObjectEventType checkLimitEvent(PhysicsValuesDTO phyValues) {
        // Check if movement is out of world limits
        //     In a corner only one event is considered. 
        //     The order of conditions defines the event priority.

        if (phyValues.position.x< 0) {
            return (ObjectEventType.WEST_LIMIT_REACHED);
        } else if (phyValues.position.x >= this.wordDimension.x) {
            return (ObjectEventType.EAST_LIMIT_REACHED);
        } else if (phyValues.position.y< 0) {
            return (ObjectEventType.NORTH_LIMIT_REACHED);
        } else if (phyValues.position.y>= this.wordDimension.y) {
            return (ObjectEventType.SOUTH_LIMIT_REACHED);
        }

        return ObjectEventType.NONE;
    }


    private void doBallAction(
            ObjectAction ballAction,
            Object ball,
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues) {

        switch (ballAction) {
            case MOVE:
                ball.doMovement(newPhyValues);
                ball.setState(ObjectState.ALIVE);
                break;

            case VERTICAL_REBOUND:
                ball.doVerticalRebound(newPhyValues, oldPhyValues);
                ball.setState(ObjectState.ALIVE);
                break;

            case HORIZONTAL_REBOUND:
                ball.doHorizontalRebound(newPhyValues, oldPhyValues);
                ball.setState(ObjectState.ALIVE);
                break;

            case DIE:
                this.killBall(ball);
                ball.setState(ObjectState.DEAD);
                break;

            case TRY_TO_GO_INSIDE:
            case EXPLODE_IN_FRAGMENTS:
                // to-do
                ball.setState(ObjectState.ALIVE);
                break;

            default:
                // To avoid zombie state
                ball.setState(ObjectState.ALIVE);

        }

    }


    private void killBall(Object ball) {
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
        return Object.getAliveQuantity();
    }


    public static long getCreatedBallsQuantity() {
        return Object.getCreatedQuantity();
    }


    public static long getDeadBallsQuantity() {
        return Object.getDeadQuantity();
    }

}
