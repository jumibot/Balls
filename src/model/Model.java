package model;


import controller.LifeGenerator;
import model.vobject.VObjectAction;
import model.vobject.VObjectState;
import model.vobject.VObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import model.physics.PhysicsValues;
import view.RenderInfoDTO;
import java.awt.Dimension;
import static java.lang.System.nanoTime;
import model.physics.PhysicsEngine;


/**
 *
 * @author juanm
 */
public class Model {

    private int maxVisualObjects;
    private Dimension worldDimensions;

    private Controller controller = null;
    private volatile ModelState state = ModelState.STARTING;
    private final Map<Integer, VObject> vObjects = new ConcurrentHashMap<>(15000);


    /**
     * CONSTRUCTORS
     */
    public Model() {

    }


    /**
     * PUBLIC
     */
    public void activate() {
        if (this.controller == null) {
            throw new IllegalArgumentException("Controller is not setted");
        }

        if (this.worldDimensions == null) {
            throw new IllegalArgumentException("Null world dimension");
        }

        if (this.maxVisualObjects <= 0) {
            throw new IllegalArgumentException("Max visual objects not setted");
        }
        this.state = ModelState.ALIVE;
    }


    synchronized public boolean addVObject(
            int imageId, int size,
            double posX, double posY,
            double speedX, double speedY,
            double accX, double accY,
            double angle) {

        if (VObject.getAliveQuantity() >= this.maxVisualObjects) {
            return false; // ========= Max vObject quantity reached ==========>>
        }
        
        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(), posX, posY, speedX, speedY, accX, accY, angle);

        VObject newVObject
                = new VObject(imageId, size, new PhysicsEngine(phyVals));

        return this.addVObject(newVObject);
    }


    synchronized public boolean addVObject(VObject newVObject) {
        if (VObject.getAliveQuantity() >= this.maxVisualObjects) {
            return false; // ========= Max vObject quantity reached ==========>>
        }

        newVObject.setModel(this);
        newVObject.activate();
        this.vObjects.put(newVObject.getId(), newVObject);
        return true;
    }


    public int getMaxVisualObjects() {
        return this.maxVisualObjects;
    }


    public ArrayList<RenderInfoDTO> getRenderableObjects() {
        long t0, t1, t2, create_array, full_get;

        t0 = nanoTime();

        ArrayList<RenderInfoDTO> renderableObjects
                = new ArrayList(VObject.getAliveQuantity() * 2);

        t1 = nanoTime();

        this.vObjects.forEach((id, vObject) -> {
            RenderInfoDTO rInfo = vObject.buildRenderInfo();
            if (rInfo != null) {
                renderableObjects.add(rInfo);
            }
        });

        t2 = nanoTime();
        create_array = (t1 - t0) / 1_000_000;
        full_get = (t2 - t0) / 1_000_000;

        return renderableObjects;
    }


    public ModelState getState() {
        return this.state;
    }


    public Dimension getWorldDimension() {
        return this.worldDimensions;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private void killVObject(VObject vObject) {
        /* 
        TO-DO:
        Change vObject state to finalize de thread execution
        Remove vObject from model
         */

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }


    public void newRandomVObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void setDimension(Dimension worldDim) {
        this.worldDimensions = worldDim;
    }


    public void setMaxVisualObjects(int maxVisualObjects) {
        this.maxVisualObjects = maxVisualObjects;
    }


    public void processVObjectEvents(
            VObject vObjectToCheck,
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues) {

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
        if (this.vObjects.remove(vObject.getId()) == null) {
            return; // ======= Elmento no esta en la lista ========>
        }
        vObject.die();
    }


    private VObjectEventType checkLimitEvent(PhysicsValues phyValues) {
        // Check if movement is out of world limits
        //     In a corner only one event is considered. 
        //     The order of conditions defines the event priority.

        if (phyValues.posX < 0) {
            return (VObjectEventType.EAST_LIMIT_REACHED);
        } else if (phyValues.posX >= this.worldDimensions.width) {
            return (VObjectEventType.WEST_LIMIT_REACHED);
        } else if (phyValues.posY < 0) {
            return (VObjectEventType.NORTH_LIMIT_REACHED);
        } else if (phyValues.posY >= this.worldDimensions.height) {
            return (VObjectEventType.SOUTH_LIMIT_REACHED);
        }

        return VObjectEventType.NONE;
    }


    private void doVObjectAction(
            VObjectAction vObjectAction,
            VObject vObject,
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues) {

        switch (vObjectAction) {
            case MOVE:
                vObject.doMovement(newPhyValues);
                vObject.setState(VObjectState.ALIVE);
                break;

            case REBOUND_IN_EAST:
                vObject.reboundInEast(newPhyValues, oldPhyValues, this.worldDimensions);
                vObject.setState(VObjectState.ALIVE);
                break;

            case REBOUND_IN_WEST:
                vObject.reboundInWest(newPhyValues, oldPhyValues, this.worldDimensions);
                vObject.setState(VObjectState.ALIVE);
                break;

            case REBOUND_IN_NORTH:
                vObject.reboundInNorth(newPhyValues, oldPhyValues, this.worldDimensions);
                vObject.setState(VObjectState.ALIVE);
                break;

            case REBOUND_IN_SOUTH:
                vObject.reboundInSouth(newPhyValues, oldPhyValues, this.worldDimensions);
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
