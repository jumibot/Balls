package model;


import model.vobject.VObjectGenerator;
import model.vobject.VObjectAction;
import model.vobject.VObjectState;
import model.vobject.VObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import model.physics.PhysicsValuesDTO;
import view.RenderableVObject;
import java.awt.Dimension;


/**
 *
 * @author juanm
 */
public class Model {

    private final int maxVObject;
    private final Dimension wordDim;

    private Controller controller = null;
    private VObjectGenerator vObjectGenerator = null;
    private ModelState state = ModelState.STARTING;
    private Map<Integer, VObject> vObject = new ConcurrentHashMap<>(4096);


    /**
     * CONSTRUCTORS
     */
    public Model(int maxVObjectQuantity, Dimension wordDimension) {
        this.maxVObject = maxVObjectQuantity;
        this.wordDim = wordDimension;

        this.vObjectGenerator = new VObjectGenerator(
                this, // .... Model
                400, 1, // .. Mass range
                2, // ..... Max creation delay 
                0.0002, // ....... Max acceleration in px X millisecond^-2
                0.5, // ..... MaxSpeed,
                4, 1 // ..... Size range in px
        );

        this.vObjectGenerator.activate();
    }


    /**
     * PUBLIC
     */
    synchronized public boolean addVObject(VObject newVObject) {
        if (VObject.getAliveQuantity() >= this.maxVObject) {
            // Max vObject quantity reached
            return false; // =================================================>
        }

        newVObject.setModel(this);
        if (!newVObject.activate()) {
            System.out.println("Can not activate vObject <" + newVObject.getId() + "> · Model");
            return false;

        }
//        System.out.println("VObject <" + newVObject.getId() + "> Activated · Model");
        this.vObject.put(newVObject.getId(), newVObject);

        return true;
    }


    public int getMaxVObject() {
        return this.maxVObject;
    }


    synchronized public ArrayList<RenderableVObject> getRenderableObjects() {
        ArrayList<RenderableVObject> renderableObjects
                = new ArrayList(VObject.getAliveQuantity() * 2);

        this.vObject.forEach((id, vObject) -> {
            if (vObject.getRenderableObject() != null) {
                renderableObjects.add(vObject.getRenderableObject());
            }
        });

        return renderableObjects;
    }


    public ModelState getState() {
        return this.state;
    }


    public Dimension getWorldDim() {
        return this.wordDim;
    }


    public boolean isAlive() {
        return this.state == ModelState.ALIVE;
    }


    public void killVObject(int vObjectId) {
        /* 
        TO-DO:
        Change vObject state to finalize de thread execution
        Remove vObject from model
         */
    }


    public void newRandomVObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void setController(Controller controller) {
        this.controller = controller;
        this.state = ModelState.ALIVE;
    }


    /**
     * PROTECTED
     */
    public void processVObjectEvents(
            VObject vObjectToCheck,
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues) {

        if (vObjectToCheck.getState() != VObjectState.ALIVE) {
            return; // To avoid duplicate or unnecesary event processing ======>
        }

        VObjectState previousState = vObjectToCheck.getState();
        vObjectToCheck.setState(VObjectState.HANDS_OFF);
        VObjectEventType limitEvent = VObjectEventType.NONE;

        try {
            limitEvent = this.checkLimitEvent(newPhyValues);

            if (limitEvent != VObjectEventType.NONE) {
                this.doVObjectAction(
                        this.controller.decideAction(limitEvent),
                        vObjectToCheck,
                        newPhyValues,
                        oldPhyValues);
            }
        } catch (Exception e) {
            // Fallback anti-zombi: If exception ocurrs back to previous state
            if (vObjectToCheck.getState() == VObjectState.HANDS_OFF) {
                vObjectToCheck.setState(previousState);
            }

        } finally {
            if (vObjectToCheck.getState() == VObjectState.HANDS_OFF) {
                vObjectToCheck.setState(VObjectState.ALIVE);
            }
        }

        if (limitEvent != VObjectEventType.NONE) {
            return; // ========================================================>
        }

        this.doVObjectAction(VObjectAction.MOVE, vObjectToCheck, newPhyValues, oldPhyValues);

        // 2 - Check if object want to go inside special areas
        // 3 - Check for collisions with other objects
    }


    /**
     * PRIVATE
     */
    synchronized private void removeVObject(VObject vObject) {
        if (this.vObject.remove(vObject.getId()) == null) {
            return; // ======= Elmento no esta en la lista ========>
        }
        vObject.die();
    }


    private VObjectEventType checkLimitEvent(PhysicsValuesDTO phyValues) {
        // Check if movement is out of world limits
        //     In a corner only one event is considered. 
        //     The order of conditions defines the event priority.

        if (phyValues.position.x < 0) {
            return (VObjectEventType.EAST_LIMIT_REACHED);
        } else if (phyValues.position.x >= this.wordDim.width) {
            return (VObjectEventType.WEST_LIMIT_REACHED);
        } else if (phyValues.position.y < 0) {
            return (VObjectEventType.NORTH_LIMIT_REACHED);
        } else if (phyValues.position.y >= this.wordDim.height) {
            return (VObjectEventType.SOUTH_LIMIT_REACHED);
        }

        return VObjectEventType.NONE;
    }


    private void doVObjectAction(
            VObjectAction vObjectAction,
            VObject vObject,
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues) {

        switch (vObjectAction) {
            case MOVE:
                vObject.doMovement(newPhyValues);
                vObject.setState(VObjectState.ALIVE);
                break;

            case REBOUND_IN_EAST:
                vObject.reboundInEast(newPhyValues, oldPhyValues, this.wordDim);
                vObject.setState(VObjectState.ALIVE);
                break;
                
            case REBOUND_IN_WEST:
                vObject.reboundInWest(newPhyValues, oldPhyValues, this.wordDim);
                vObject.setState(VObjectState.ALIVE);
                break;
                
            case REBOUND_IN_NORTH:
                vObject.reboundInNorth(newPhyValues, oldPhyValues, this.wordDim);
                vObject.setState(VObjectState.ALIVE);
                break;
                
            case REBOUND_IN_SOUTH:
                vObject.reboundInSouth(newPhyValues, oldPhyValues, this.wordDim);
                vObject.setState(VObjectState.ALIVE);
                break;

            case DIE:
                this.killVObject(vObject);
                vObject.setState(VObjectState.DEAD);
                break;

            case TRY_TO_GO_INSIDE:
            case EXPLODE_IN_FRAGMENTS:
                // to-do
                vObject.setState(VObjectState.ALIVE);
                break;

            default:
                // To avoid zombie state
                vObject.setState(VObjectState.ALIVE);

        }

    }


    private void killVObject(VObject vObject) {
        /* 
        TO-DO:
        Change vObject state to finalize de thread execution
        Remove vObject from model
         */
    }


    /**
     * STATIC
     */
    public static long getAlivedVObjectQuantity() {
        return VObject.getAliveQuantity();
    }


    public static long getCreatedVObjectQuantity() {
        return VObject.getCreatedQuantity();
    }


    public static long getDeadVObjectQuantity() {
        return VObject.getDeadQuantity();
    }

}
