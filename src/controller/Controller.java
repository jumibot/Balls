package controller;


import view.RenderableObject;
import view.View;
import model.Model;
import model.object.Object;
import model.object.ObjectAction;
import model.ObjectEventType;
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
    public void addBall(Object newBall) {
        this.model.addBall(newBall);
    }


    public void newRandomBall() {
        this.model.newRandomBall();
    }


    public ArrayList<RenderableObject> getRenderableObjects() {
        return this.model.getRenderableObjects();
    }


    public ObjectAction decideAction(ObjectEventType eventType) {
        ObjectAction ballAction;

        switch (eventType) {
            case NORTH_LIMIT_REACHED:
                ballAction = ObjectAction.VERTICAL_REBOUND;
                break;
                
            case SOUTH_LIMIT_REACHED:
                ballAction = ObjectAction.VERTICAL_REBOUND;
                break;
                
            case EAST_LIMIT_REACHED:
                ballAction = ObjectAction.HORIZONTAL_REBOUND;
                break;
                
            case WEST_LIMIT_REACHED:
                ballAction = ObjectAction.HORIZONTAL_REBOUND;
                break;

            default:
                // To avoid zombie state
                ballAction = ObjectAction.NONE;
        }

        return ballAction;
    }


    public ObjectAction decideAction(ObjectEventType eventType, ArrayList<Object> RelatedBalls) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
