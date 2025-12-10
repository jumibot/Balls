package model;


import model.entities.EntityState;
import model.entities.DynamicBody;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import controller.Controller;
import model.physics.PhysicsValues;
import view.renderables.DBodyInfoDTO;
import java.awt.Dimension;
import static java.lang.System.nanoTime;
import model.entities.AbstractEntity;
import model.entities.DecoEntity;
import model.entities.PlayerBody;
import model.entities.StaticBody;
import model.physics.BasicPhysicsEngine;
import view.renderables.EntityInfoDTO;


public class Model {

    private int maxDBody;
    private Dimension worldDim;

    private Controller controller = null;
    private volatile ModelState state = ModelState.STARTING;

    private final Map<Integer, DynamicBody> dBodies = new ConcurrentHashMap<>(10000);
    private final Map<String, PlayerBody> pBodies = new ConcurrentHashMap<>(10);
    private final Map<Integer, StaticBody> gravityBodies = new ConcurrentHashMap<>(50);
    private final Map<Integer, StaticBody> sBodies = new ConcurrentHashMap<>(100);
    private final Map<Integer, DecoEntity> decorators = new ConcurrentHashMap<>(100);


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


    public boolean addDBody(String assetId, double size,
            double posX, double posY, double speedX, double speedY,
            double accX, double accY, double angle) {

        if (AbstractEntity.getAliveQuantity() >= this.maxDBody) {
            return false; // ========= Max vObject quantity reached ==========>>
        }

        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(),
                posX, posY,
                speedX, speedY,
                accX, accY,
                angle, 0d, 0d,
                0d);

        DynamicBody dBody
                = new DynamicBody(assetId, size, new BasicPhysicsEngine(phyVals));

        dBody.setModel(this);
        dBody.activate();
        this.dBodies.put(dBody.getEntityId(), dBody);

        return true;
    }


    public String addPlayer(String assetId, double size,
            double posX, double posY, double speedX, double speedY,
            double accX, double accY, double angle) {

        if (AbstractEntity.getAliveQuantity() >= this.maxDBody) {
            return null; // ========= Max vObject quantity reached ==========>>
        }

        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(),
                posX, posY,
                speedX, speedY,
                accX, accY,
                angle, 0d, 0d,
                0d);

        PlayerBody pBody
                = new PlayerBody(assetId, size, new BasicPhysicsEngine(phyVals));

        pBody.setModel(this);
        pBody.activate();
        this.dBodies.put(pBody.getEntityId(), pBody);
        this.pBodies.put(pBody.getPlayerId(), pBody);

        return pBody.getPlayerId();
    }


    public void addSBody(String assetId, double size,
            double posX, double posY, double angle) {

        StaticBody sBody = new StaticBody(assetId, size, posX, posY, angle);

        sBody.setModel(this);
        sBody.activate();
        this.sBodies.put(sBody.getEntityId(), sBody);
    }


    public void addDecorator(String assetId, double size, double posX, double posY, double angle) {
        DecoEntity deco = new DecoEntity(assetId, size, posX, posY, angle);

        deco.setModel(this);
        deco.activate();
        this.decorators.put(deco.getEntityId(), deco);
    }


    public int getMaxDBody() {
        return this.maxDBody;
    }


    public ArrayList<DBodyInfoDTO> getDBodyInfo() {
        ArrayList<DBodyInfoDTO> dBodyInfoList
                = new ArrayList(DynamicBody.getAliveQuantity() * 2);

        this.dBodies.forEach((id, dBody) -> {
            DBodyInfoDTO bodyInfo = dBody.buildEntityInfo();
            if (bodyInfo != null) {
                dBodyInfoList.add(bodyInfo);
            }
        });

        return dBodyInfoList;
    }


    public ArrayList<EntityInfoDTO> getDecoratorsInfo() {
        ArrayList<EntityInfoDTO> decoInfoList
                = new ArrayList(StaticBody.getAliveQuantity() * 2);

        this.decorators.forEach((id, deco) -> {
            EntityInfoDTO entityInfo = deco.buildEntityInfo();
            if (entityInfo != null) {
                decoInfoList.add(entityInfo);
            }
        });

        return decoInfoList;
    }


    public ArrayList<EntityInfoDTO> getSBodyInfo() {
        ArrayList<EntityInfoDTO> bodyInfoList
                = new ArrayList(StaticBody.getAliveQuantity() * 2);

        this.sBodies.forEach((id, sBody) -> {
            EntityInfoDTO bodyInfo = sBody.buildEntityInfo();
            if (bodyInfo != null) {
                bodyInfoList.add(bodyInfo);
            }
        });

        return bodyInfoList;
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


    synchronized public void killDBody(DynamicBody dBody) {
        this.dBodies.remove(dBody.getEntityId());
        dBody.die();
    }


    // ==== API de control, SIEMPRE con playerId ====
    public void playerFire(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody != null) {
            pBody.requestFire();
        }
    }


    public void playerThrustOn(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody != null) {
            pBody.thrustOn();
        }
    }


    public void playerThrustOff(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody != null) {
            pBody.thrustOff();
        }
    }


    public void playerReverseThrust(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody != null) {
            pBody.reverseThrust();
        }
    }


    public void playerRotateLeftOn(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody != null) {
            pBody.rotateLeftOn();
        }
    }


    public void playerRotateOff(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody != null) {
            pBody.rotateOff();
        }
    }


    public void playerRotateRightOn(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody != null) {
            pBody.rotateRightOn();
        }
    }


    public void processDBodyEvents(DynamicBody dBodyToCheck,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

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

        this.doDBodyAction(BodyActionType.MOVE, dBodyToCheck, newPhyValues, oldPhyValues);

        // 2 - Check if object want to go inside special areas
        // 3 - Check for collisions with other objects
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void setDimension(Dimension worldDim) {
        this.worldDim = worldDim;
    }


    public void setMaxDBody(int maxDynamicBody) {
        this.maxDBody = maxDynamicBody;
    }


    /**
     * PRIVATE
     */
    private EventType checkLimitEvent(PhysicsValues phyValues) {
        // Check if movement is out of world limits
        //     In a corner only one event is considered. 
        //     The order of conditions defines the event priority.

        if (phyValues.posX < 0) {
            return (EventType.EAST_LIMIT_REACHED);
        } else if (phyValues.posX >= this.worldDim.width) {
            return (EventType.WEST_LIMIT_REACHED);
        } else if (phyValues.posY < 0) {
            return (EventType.NORTH_LIMIT_REACHED);
        } else if (phyValues.posY >= this.worldDim.height) {
            return (EventType.SOUTH_LIMIT_REACHED);
        }

        return EventType.NONE;
    }


    private void doDBodyAction(BodyActionType action, DynamicBody dBody,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

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
