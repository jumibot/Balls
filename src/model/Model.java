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
import java.util.List;
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

        if (!isProcessable(dBodyToCheck)) {
            return; // To avoid duplicate or unnecesary event processing ======>
        }

        EntityState previousState = dBodyToCheck.getState();
        dBodyToCheck.setState(EntityState.HANDS_OFF);
        EventType event = EventType.NONE;

        List<EventType> events = new ArrayList<>();

        try {
            event = this.checkLimitEvent(newPhyValues);

            if (event != EventType.NONE) {
                this.doDBodyAction(
                        this.controller.decideAction(event),
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

        if (event != EventType.NONE) {
            return; // ========================================================>
        }

        this.doDBodyAction(ActionType.MOVE, dBodyToCheck, newPhyValues, oldPhyValues);

        // 2 - Check if object want to go inside special areas
        // 3 - Check for collisions with other objects
    }


    public void processDBodyEvents2(DynamicBody dBodyToCheck,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        if (!isProcessable(dBodyToCheck)) {
            return; // To avoid duplicate or unnecesary event processing ======>
        }

        EntityState previousState = dBodyToCheck.getState();
        dBodyToCheck.setState(EntityState.HANDS_OFF);

        try {
            List<EventDTO> events = this.detectEvents(
                    dBodyToCheck, newPhyValues, oldPhyValues);

            List<ActionDTO> actions = this.decideActions(
                    dBodyToCheck, events, newPhyValues, oldPhyValues);

            this.doActions(
                    dBodyToCheck, actions, newPhyValues, oldPhyValues);

        } catch (Exception e) {
            // Fallback anti-zombi
            if (dBodyToCheck.getState() == EntityState.HANDS_OFF) {
                dBodyToCheck.setState(previousState);
            }

        } finally {
            // Getout off HANDS_OFF ... if leaving
            if (dBodyToCheck.getState() == EntityState.HANDS_OFF) {
                dBodyToCheck.setState(EntityState.ALIVE);
            }
        }
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
            return (EventType.REACHED_EAST_LIMIT);
        } else if (phyValues.posX >= this.worldDim.width) {
            return (EventType.REACHED_WEST_LIMIT);
        } else if (phyValues.posY < 0) {
            return (EventType.REACHED_NORTH_LIMIT);
        } else if (phyValues.posY >= this.worldDim.height) {
            return (EventType.REACHED_SOUTH_LIMIT);
        }

        return EventType.NONE;
    }


    private java.util.List<ActionDTO> decideActions(
            DynamicBody body, List<EventDTO> events,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        java.util.List<ActionDTO> actions = new java.util.ArrayList<>(4);

        // 1) Eventos externos (límites, colisiones...) → Controller decide
        if (events != null) {
            for (EventDTO ev : events) {
                if (ev == null || ev.eventType == null || ev.eventType == EventType.NONE) {
                    continue;
                }

                // Controller aplica reglas de juego: EventType -> ActionType
                ActionType typeFromController = this.controller.decideAction(ev.eventType);
                if (typeFromController != null && typeFromController != ActionType.NONE) {
                    // Por defecto, las acciones del controller las ejecuta el BODY
                    actions.add(new ActionDTO(
                            typeFromController,
                            ActionExecutor.BODY,
                            ActionPriority.NORMAL
                    ));
                }
            }
        }

        // 2) Acción interna: disparo (no viene como EventType)
        //    Solo si el body sabe disparar (PlayerBody, etc.)
        if (body instanceof PlayerBody) {
            double dtSeconds = (newPhyValues.timeStamp - oldPhyValues.timeStamp)
                    / 1_000_000_000.0;

            PlayerBody pBody = (PlayerBody) body;
            if (pBody.mustFireNow(dtSeconds)) {
                actions.add(new ActionDTO(
                        ActionType.FIRE,
                        ActionExecutor.MODEL, // Crear proyectil lo hace el Model
                        ActionPriority.HIGHT // Ojo: enum está escrito HIGHT
                ));
            }
        }

        // 3) Acción por defecto: MOVE si nadie ha pedido muerte/fragmentación
        boolean hasDie = actions.stream()
                .anyMatch(a -> a.type == ActionType.DIE || a.type == ActionType.EXPLODE_IN_FRAGMENTS);

        if (!hasDie) {
            actions.add(new ActionDTO(
                    ActionType.MOVE,
                    ActionExecutor.BODY,
                    ActionPriority.NORMAL
            ));
        }

        // 4) Ordenamos por prioridad: HIGHT -> NORMAL -> LOW
        actions.sort(java.util.Comparator.comparing(a -> a.priority));

        return actions;
    }


    private List<EventDTO> detectEvents(DynamicBody body,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        List<EventDTO> events = new java.util.ArrayList<>(2);

        // 1) Eventos de límite de mundo (depende del nuevo estado físico)
        EventType limitEventType = this.checkLimitEvent(newPhyValues);
        if (limitEventType != EventType.NONE) {
            events.add(new EventDTO(body, limitEventType));
        }

        // 2) TODO: eventos de colisión, zonas, etc.
        return events;
    }


    private void doActions(
            DynamicBody body, List<ActionDTO> actions,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        if (actions == null || actions.isEmpty()) {
            return;
        }

        for (ActionDTO action : actions) {
            if (action == null || action.type == null) {
                continue;
            }

            // Sort actions by priority
            switch (action.executor) {
                case BODY:
                    doDBodyAction(action.type, body, newPhyValues, oldPhyValues);
                    break;

                case MODEL:
                    doModelAction(action.type, body, newPhyValues, oldPhyValues);
                    break;

                default:
                // Nada
            }

            if (body.getState() == EntityState.DEAD) {
                return; // no seguimos con más acciones
            }
        }
    }


    private void doDBodyAction(ActionType action, DynamicBody dBody,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        switch (action) {
            case MOVE:
                dBody.doMovement(newPhyValues);
                break;

            case REBOUND_IN_EAST:
                dBody.reboundInEast(newPhyValues, oldPhyValues,
                                    this.worldDim.width, this.worldDim.height);
                break;

            case REBOUND_IN_WEST:
                dBody.reboundInWest(newPhyValues, oldPhyValues,
                                    this.worldDim.width, this.worldDim.height);
                break;

            case REBOUND_IN_NORTH:
                dBody.reboundInNorth(newPhyValues, oldPhyValues,
                                     this.worldDim.width, this.worldDim.height);
                break;

            case REBOUND_IN_SOUTH:
                dBody.reboundInSouth(newPhyValues, oldPhyValues,
                                     this.worldDim.width, this.worldDim.height);
                break;

            case DIE:
                this.killDBody(dBody);
                break;

            case GO_INSIDE:
                // To-Do: lógica futura
                break;

            case NONE:
            default:
                // Nada que hacer
        }
    }


    private void doModelAction(ActionType action, DynamicBody dBody,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        switch (action) {
            case FIRE:
//                this.spawnProjectileFrom(dBody, newPhyValues);
                break;

            case EXPLODE_IN_FRAGMENTS:
                break;

            default:
        }
    }


    private boolean isProcessable(DynamicBody body) {
        if (body == null) {
            return false;
        }

        if (this.state != ModelState.ALIVE) {
            return false;
        }

        return body.getState() == EntityState.ALIVE;
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