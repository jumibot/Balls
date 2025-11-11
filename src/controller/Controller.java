package controller;


import view.RenderableVObject;
import view.View;
import model.Model;
import model.vobject.VObject;
import model.vobject.VObjectAction;
import model.VObjectEventType;
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
    public void addVObject(VObject newVObject) {
        this.model.addVObject(newVObject);
    }


    public void newRandomVObject() {
        this.model.newRandomVObject();
    }


    public ArrayList<RenderableVObject> getRenderableObjects() {
        return this.model.getRenderableObjects();
    }


    public VObjectAction decideAction(VObjectEventType eventType) {
        VObjectAction vObjectAction;

        switch (eventType) {
            case NORTH_LIMIT_REACHED:
                vObjectAction = VObjectAction.REBOUND_IN_NORTH;
                break;

            case SOUTH_LIMIT_REACHED:
                vObjectAction = VObjectAction.REBOUND_IN_SOUTH;
                break;

            case EAST_LIMIT_REACHED:
                vObjectAction = VObjectAction.REBOUND_IN_EAST;
                break;

            case WEST_LIMIT_REACHED:
                vObjectAction = VObjectAction.REBOUND_IN_WEST;
                break;

            default:
                // To avoid zombie state
                vObjectAction = VObjectAction.NONE;
        }

        return vObjectAction;
    }


    public VObjectAction decideAction(VObjectEventType eventType, ArrayList<VObject> RelatedVObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
