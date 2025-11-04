package controller;


import view.RenderableObject;
import view.View;
import model.Model;
import model.Ball;
import model.BallAction;
import model.EventType;
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
        this.view.activate();

        /* TO-DO -> Arrancar el generador de bolas */
    }


    /**
     * PUBLICS
     */
    public void addBall(Ball newBall) {
        this.model.addBall(newBall);
    }


    public void newRandomBall() {
        this.model.newRandomBall();
    }


    public ArrayList<RenderableObject> getRenderableObjects() {
        return this.model.getRenderableObjects();
    }


    public BallAction decideAction(EventType eventType) {
        BallAction ballAction;

        switch (eventType) {
            case NORTH_LIMIT_REACHED:
                ballAction = BallAction.VERTICAL_REBOUND;
                break;
            case SOUTH_LIMIT_REACHED:
                ballAction = BallAction.VERTICAL_REBOUND;
                break;
            case EAST_LIMIT_REACHED:
                ballAction = BallAction.HORIZONTAL_REBOUND;

                break;
            case WEST_LIMIT_REACHED:
                ballAction = BallAction.HORIZONTAL_REBOUND;
                break;

            default:
                // To avoid zombie state
                ballAction = BallAction.NONE;
        }

        return ballAction;
    }


    public BallAction decideAction(EventType eventType, ArrayList<Ball> RelatedBalls) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
