package model;


import model.entities.BodyAction;
import model.entities.EntityState;
import model.entities.DynamicBody;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import model.physics.PhysicsValues;
import view.renderables.DBodyRenderInfoDTO;
import java.awt.Dimension;
import static java.lang.System.nanoTime;
import model.entities.AbstractEntity;
import model.physics.BasicPhysicsEngine;


/**
 *
 * @author juanm
 */
public class Model {

    private int maxDBody;
    private Dimension worldDim;

    private Controller controller = null;
    private volatile ModelState state = ModelState.STARTING;
    private final Map<Integer, DynamicBody> dBodies = new ConcurrentHashMap<>(15000);


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

        if (this.maxDBody <= 0) {
            throw new IllegalArgumentException("Max visual objects not setted");
        }
        this.state = ModelState.ALIVE;
    }


    synchronized public boolean addDBody(
            String assetId, int size,
            double posX, double posY,
            double speedX, double speedY,
            double accX, double accY,
            double angle) {

        if (AbstractEntity.getAliveQuantity() >= this.maxDBody) {
            return false; // ========= Max vObject quantity reached ==========>>
        }

        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(), posX, posY, speedX, speedY, accX, accY, angle);

        DynamicBody newVObject
                = new DynamicBody(assetId, size, new BasicPhysicsEngine(phyVals));

        return this.addDBody(newVObject);
    }


    synchronized public boolean addDBody(DynamicBody newVObject) {
        if (AbstractEntity.getAliveQuantity() >= this.maxDBody) {
            return false; // ========= Max vObject quantity reached ==========>>
        }

        newVObject.setModel(this);
        newVObject.activate();
        this.dBodies.put(newVObject.getId(), newVObject);
        return true;
    }


    public int getMaxDBody() {
        return this.maxDBody;
    }


    public ArrayList<DBodyRenderInfoDTO> getDBodyRenderInfo() {
        ArrayList<DBodyRenderInfoDTO> dBodyRenderInfo
                = new ArrayList(DynamicBody.getAliveQuantity() * 2);

        this.dBodies.forEach((id, vObject) -> {
            DBodyRenderInfoDTO rInfo = vObject.buildRenderInfo();
            if (rInfo != null) {
                dBodyRenderInfo.add(rInfo);
            }
        });

        return dBodyRenderInfo;
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


    public void killDBody(int entityId) {
        /* 
        TO-DO:
        Change vObject state to finalize de thread execution
        Remove vObject from model
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }


    private void killDBody(DynamicBody entityId) {
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


    public void setMaxDBodyObjects(int maxDynamicBody) {
        this.maxDBody = maxDynamicBody;
    }


    public void processDBodyEvents(
            DynamicBody dBodyToCheck,
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues) {

        if (dBodyToCheck.getState() != EntityState.ALIVE) {
            return; // To avoid duplicate or unnecesary event processing ======>
        }

        EntityState previousState = dBodyToCheck.getState();
        dBodyToCheck.setState(EntityState.HANDS_OFF);
        EventType limitEvent = EventType.NONE;

        try {
            limitEvent = this.checkLimitEvent(newPhyValues);

            if (limitEvent != EventType.NONE) {
                this.doDBodyAction(
                        this.controller.decideAction(limitEvent),
                        dBodyToCheck,
                        newPhyValues,
                        oldPhyValues);
            }
        } catch (Exception e) {
            // Fallback anti-zombi: If exception ocurrs back to previous state
            if (dBodyToCheck.getState() == EntityState.HANDS_OFF) {
                dBodyToCheck.setState(previousState);
            }

        } finally {
            if (dBodyToCheck.getState() == EntityState.HANDS_OFF) {
                dBodyToCheck.setState(EntityState.ALIVE);
            }
        }

        if (limitEvent != EventType.NONE) {
            return; // ========================================================>
        }

        this.doDBodyAction(BodyAction.MOVE, dBodyToCheck, newPhyValues, oldPhyValues);

        // 2 - Check if object want to go inside special areas
        // 3 - Check for collisions with other objects
    }


    /**
     * PRIVATE
     */
    synchronized private void removeDBody(DynamicBody dBody) {
        if (this.dBodies.remove(dBody.getId()) == null) {
            return; // ======= Elmento no esta en la lista ========>
        }
        dBody.die();
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


    private void doDBodyAction(
            BodyAction action,
            DynamicBody dBody,
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues) {

        switch (action) {
            case MOVE:
                dBody.doMovement(newPhyValues);
                dBody.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_EAST:
                dBody.reboundInEast(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                dBody.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_WEST:
                dBody.reboundInWest(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                dBody.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_NORTH:
                dBody.reboundInNorth(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                dBody.setState(EntityState.ALIVE);
                break;

            case REBOUND_IN_SOUTH:
                dBody.reboundInSouth(newPhyValues, oldPhyValues, this.worldDim.width, this.worldDim.height);
                dBody.setState(EntityState.ALIVE);
                break;

            case DIE:
                this.killDBody(dBody);
                dBody.setState(EntityState.DEAD);
                break;

            case TRY_TO_GO_INSIDE:
            case EXPLODE_IN_FRAGMENTS:
                // to-do
                dBody.setState(EntityState.ALIVE);
                break;

            default:
                // To avoid zombie state
                dBody.setState(EntityState.ALIVE);

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
