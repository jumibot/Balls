package model;


import model.entities.BodyAction;
import model.entities.EntityState;
import model.entities.DynamicBody;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import model.physics.PhysicsValues;
import view.RenderInfoDTO;
import java.awt.Dimension;
import static java.lang.System.nanoTime;
import model.entities.AbstractEntity;
import model.physics.BasicPhysicsEngine;


/**
 *
 * @author juanm
 */
public class Model {

    private int maxVisualObjects;
    private Dimension worldDim;

    private Controller controller = null;
    private volatile ModelState state = ModelState.STARTING;
    private final Map<Integer, DynamicBody> dynamicBodies = new ConcurrentHashMap<>(15000);


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

        if (this.worldDim == null) {
            throw new IllegalArgumentException("Null world dimension");
        }

        if (this.maxVisualObjects <= 0) {
            throw new IllegalArgumentException("Max visual objects not setted");
        }
        this.state = ModelState.ALIVE;
    }


    synchronized public boolean addDynamicBody(
            String assetId, int size,
            double posX, double posY,
            double speedX, double speedY,
            double accX, double accY,
            double angle) {

        if (AbstractEntity.getAliveQuantity() >= this.maxVisualObjects) {
            return false; // ========= Max vObject quantity reached ==========>>
        }

        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(), posX, posY, speedX, speedY, accX, accY, angle);

        DynamicBody newVObject
                = new DynamicBody(assetId, size, new BasicPhysicsEngine(phyVals));

        return this.addDynamicBody(newVObject);
    }


    synchronized public boolean addDynamicBody(DynamicBody newVObject) {
        if (AbstractEntity.getAliveQuantity() >= this.maxVisualObjects) {
            return false; // ========= Max vObject quantity reached ==========>>
        }

        newVObject.setModel(this);
        newVObject.activate();
        this.dynamicBodies.put(newVObject.getId(), newVObject);
        return true;
    }


    public int getMaxVisualObjects() {
        return this.maxVisualObjects;
    }


    public ArrayList<RenderInfoDTO> getRenderableEntities() {
        ArrayList<RenderInfoDTO> renderableObjects
                = new ArrayList(DynamicBody.getAliveQuantity() * 2);

        this.dynamicBodies.forEach((id, vObject) -> {
            RenderInfoDTO rInfo = vObject.buildRenderInfo();
            if (rInfo != null) {
                renderableObjects.add(rInfo);
            }
        });

        return renderableObjects;
    }


    public ModelState getState() {
        return this.state;
    }


    public Dimension getWorldDimension() {
        return this.worldDim;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }


    private void killVObject(DynamicBody vObject) {
        /* 
        TO-DO:
        Change vObject state to finalize de thread execution
        Remove vObject from model
         */

        throw new UnsupportedOperationException("Not supported yet.");

    }


    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void setDimension(Dimension worldDim) {
        this.worldDim = worldDim;
    }


    public void setMaxVisualObjects(int maxVisualObjects) {
        this.maxVisualObjects = maxVisualObjects;
    }


    public void processVObjectEvents(
            DynamicBody vObjectToCheck,
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues) {

        if (vObjectToCheck.getState() != EntityState.ALIVE) {
            return; // To avoid duplicate or unnecesary event processing ======>
        }

        EntityState previousState = vObjectToCheck.getState();
        vObjectToCheck.setState(EntityState.HANDS_OFF);
        EventType limitEvent = EventType.NONE;

        try {
            limitEvent = this.checkLimitEvent(newPhyValues);

            if (limitEvent != EventType.NONE) {
                this.doVObjectAction(
                        this.controller.decideAction(limitEvent),
                        vObjectToCheck,
                        newPhyValues,
                        oldPhyValues);
            }
        } catch (Exception e) {
            // Fallback anti-zombi: If exception ocurrs back to previous state
            if (vObjectToCheck.getState() == EntityState.HANDS_OFF) {
                vObjectToCheck.setState(previousState);
            }

        } finally {
            if (vObjectToCheck.getState() == EntityState.HANDS_OFF) {
                vObjectToCheck.setState(EntityState.ALIVE);
            }
        }

        if (limitEvent != EventType.NONE) {
            return; // ========================================================>
        }

        this.doVObjectAction(BodyAction.MOVE, vObjectToCheck, newPhyValues, oldPhyValues);

        // 2 - Check if object want to go inside special areas
        // 3 - Check for collisions with other objects
    }


    /**
     * PRIVATE
     */
    synchronized private void removeVObject(DynamicBody vObject) {
        if (this.dynamicBodies.remove(vObject.getId()) == null) {
            return; // ======= Elmento no esta en la lista ========>
        }
        vObject.die();
    }


    private EventType checkLimitEvent(PhysicsValues phyValues) {
        // Check if movement is out of world limits
        //     In a corner only one event is considered. 
        //     The order of conditions defines the event priority.

        if (phyValues.pos_x < 0) {
            return (EventType.EAST_LIMIT_REACHED);
        } else if (phyValues.pos_x >= this.worldDim.width) {
            return (EventType.WEST_LIMIT_REACHED);
        } else if (phyValues.pos_y < 0) {
            return (EventType.NORTH_LIMIT_REACHED);
        } else if (phyValues.pos_y >= this.worldDim.height) {
            return (EventType.SOUTH_LIMIT_REACHED);
        }

        return EventType.NONE;
    }


    private void doVObjectAction(
            BodyAction action,
            DynamicBody bEntity,
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues) {

        switch (action) {
            case MOVE:
                bEntity.doMovement(newPhyValues);
                bEntity.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_EAST:
                bEntity.reboundInEast(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                bEntity.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_WEST:
                bEntity.reboundInWest(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                bEntity.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_NORTH:
                bEntity.reboundInNorth(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                bEntity.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_SOUTH:
                bEntity.reboundInSouth(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                bEntity.setState(EntityState.ALIVE);
                break;

            case DIE:
                this.killVObject(bEntity);
                bEntity.setState(EntityState.DEAD);
                break;

            case TRY_TO_GO_INSIDE:
            case EXPLODE_IN_FRAGMENTS:
                // to-do
                bEntity.setState(EntityState.ALIVE);
                break;

            default:
                // To avoid zombie state
                bEntity.setState(EntityState.ALIVE);

        }

    }


    /**
     * STATIC
     */
    public static long getAlivedVObjectQuantity() {
        return AbstractEntity.getAliveQuantity();
    }


    public static long getCreatedVObjectQuantity() {
        return AbstractEntity.getCreatedQuantity();
    }


    public static long getDeadVObjectQuantity() {
        return AbstractEntity.getDeadQuantity();
    }

}
