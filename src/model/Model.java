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
import java.util.Comparator;
import java.util.List;
import model.entities.AbstractEntity;
import model.entities.DecoEntity;
import model.entities.PlayerBody;
import model.entities.StaticBody;
import model.physics.BasicPhysicsEngine;
import model.weapons.BasicWeapon;
import model.weapons.Weapon;
import model.weapons.WeaponDto;
import view.renderables.EntityInfoDTO;


public class Model {

    private int maxDBody;
    private Dimension worldDim;

    private Controller controller = null;
    private volatile ModelState state = ModelState.STARTING;

    private static final int MAX_ENTITIES = 5000;
    private final Map<Integer, DynamicBody> dBodies = new ConcurrentHashMap<>(MAX_ENTITIES);
    private final Map<Integer, DecoEntity> decorators = new ConcurrentHashMap<>(100);
    private final Map<Integer, StaticBody> gravityBodies = new ConcurrentHashMap<>(50);
    private final Map<String, PlayerBody> pBodies = new ConcurrentHashMap<>(10);
    private final Map<Integer, StaticBody> sBodies = new ConcurrentHashMap<>(100);
    


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
            throw new IllegalArgumentException("Controller is not set");
        }

        if (this.worldDim == null) {
            throw new IllegalArgumentException("Null world dimension");
        }

        if (this.maxDBody <= 0) {
            throw new IllegalArgumentException("Max visual objects not set");
        }
        this.state = ModelState.ALIVE;
    }


    public boolean addDBody(String assetId, double size,
            double posX, double posY, double speedX, double speedY,
            double accX, double accY,
            double angle, double angularSpeed, double angularAcc,
            double thrust) {

        if (AbstractEntity.getAliveQuantity() >= this.maxDBody) {
            return false; // ========= Max vObject quantity reached ==========>>
        }

        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(), posX, posY, speedX, speedY,
                accX, accY, angle, angularSpeed, angularAcc, thrust);

        DynamicBody dBody
                = new DynamicBody(assetId, size, new BasicPhysicsEngine(phyVals));

        dBody.setModel(this);
        dBody.activate();
        this.dBodies.put(dBody.getEntityId(), dBody);

        return true;
    }


    public void addDecorator(String assetId, double size, double posX, double posY, double angle) {
        DecoEntity deco = new DecoEntity(assetId, size, posX, posY, angle);

        deco.setModel(this);
        deco.activate();
        this.decorators.put(deco.getEntityId(), deco);
    }


    public String addPlayer(String assetId, double size,
            double posX, double posY, double speedX, double speedY,
            double accX, double accY,
            double angle, double angularSpeed, double angularAcc,
            double thrust) {

        if (AbstractEntity.getAliveQuantity() >= this.maxDBody) {
            return null; // ========= Max vObject quantity reached ==========>>
        }

        PhysicsValues phyVals = new PhysicsValues(
                nanoTime(), posX, posY, speedX, speedY, accX, accY,
                angle, angularSpeed, angularAcc, thrust);

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


    public void addWeaponToPlayer(
            String playerId, String projectileAssetId, double projectileSize,
            double firingSpeed, double acceleration, double accelerationTime,
            double shootingOffset, int burstSize, double fireRate) {

        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody == null) {
            return; // ========= Player not found =========>
        }

        Weapon weapon = new BasicWeapon(
                projectileAssetId, projectileSize,
                firingSpeed, acceleration, accelerationTime,
                shootingOffset, burstSize, fireRate);

        pBody.addWeapon(weapon);
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


    public ArrayList<EntityInfoDTO> getStaticsInfo() {
        ArrayList<EntityInfoDTO> staticsInfo
                = new ArrayList(StaticBody.getAliveQuantity() * 2);

        this.decorators.forEach((id, deco) -> {
            EntityInfoDTO entityInfo = deco.buildEntityInfo();
            if (entityInfo != null) {
                staticsInfo.add(entityInfo);
            }
        });

        this.sBodies.forEach((id, sBody) -> {
            EntityInfoDTO bodyInfo = sBody.buildEntityInfo();
            if (bodyInfo != null) {
                staticsInfo.add(bodyInfo);
            }
        });

        return staticsInfo;
    }


    public ModelState getState() {
        return this.state;
    }


    public int getCreatedQuantity() {
        return AbstractEntity.getCreatedQuantity();
    }


    public int getAliveQuantity() {
        return AbstractEntity.getAliveQuantity();
    }


    public int getDeadQuantity() {
        return AbstractEntity.getDeadQuantity();
    }


    public Dimension getWorldDimension() {
        return this.worldDim;
    }


    public boolean isAlive() {
        return this.state == ModelState.ALIVE;
    }


    public void killDBody(DynamicBody dBody) {
        dBody.die();
        this.dBodies.remove(dBody.getEntityId());
    }


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

        try {
            List<EventDTO> events = this.detectEvents(
                    dBodyToCheck, newPhyValues, oldPhyValues);

            List<ActionDTO> actions = this.resolveActionsForEvents(
                    dBodyToCheck, events);

            this.doActions(
                    dBodyToCheck, actions, newPhyValues, oldPhyValues);

        } catch (Exception e) { // Fallback anti-zombi
            if (dBodyToCheck.getState() == EntityState.HANDS_OFF) {
                dBodyToCheck.setState(previousState);
            }

        } finally { // Getout: off HANDS_OFF ... if leaving
            if (dBodyToCheck.getState() == EntityState.HANDS_OFF) {
                dBodyToCheck.setState(EntityState.ALIVE);
            }
        }
    }


    public void selectNextWeapon(String playerId) {
        PlayerBody pBody = this.pBodies.get(playerId);
        if (pBody == null) {
            return;
        }

        pBody.selectNextWeapon();
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
    private List<EventDTO> checkLimitEvents(AbstractEntity entity, PhysicsValues phyValues) {
        List<EventDTO> limitEvents = new ArrayList<>(4);

        if (phyValues.posX < 0) {
            limitEvents.add(new EventDTO(entity, EventType.REACHED_EAST_LIMIT));
        }

        if (phyValues.posX >= this.worldDim.width) {
            limitEvents.add(new EventDTO(entity, EventType.REACHED_WEST_LIMIT));
        }

        if (phyValues.posY < 0) {
            limitEvents.add(new EventDTO(entity, EventType.REACHED_NORTH_LIMIT));
        }

        if (phyValues.posY >= this.worldDim.height) {
            limitEvents.add(new EventDTO(entity, EventType.REACHED_SOUTH_LIMIT));
        }

        return limitEvents;
    }


    private List<ActionDTO> resolveActionsForEvents(
            AbstractEntity entity, List<EventDTO> events) {

        List<ActionDTO> actionsFromController = this.controller.decideActions(entity, events);

        if (actionsFromController == null || actionsFromController.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        List<ActionDTO> actions = new ArrayList<>(actionsFromController.size());
        for (ActionDTO a : actionsFromController) {
            if (a != null && a.type != null && a.type != ActionType.NONE) {
                actions.add(a);
            }
        }

        return actions;
    }


    private List<EventDTO> detectEvents(DynamicBody body,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        List<EventDTO> events = this.checkLimitEvents(body, newPhyValues);

        if (body instanceof PlayerBody) {
            if (((PlayerBody) body).mustFireNow(newPhyValues)) {
                events.add(new EventDTO(body, EventType.MUST_FIRE));
            }
        }

        // Eventos de colisión, zonas, etc.
        return events;
    }


    private void doActions(
            DynamicBody body, List<ActionDTO> actions,
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues) {

        if (actions == null || actions.isEmpty()) {
            return;
        }

        actions.sort(Comparator.comparing(a -> a.priority));

        for (ActionDTO action : actions) {
            if (action == null || action.type == null) {
                continue;
            }

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
                this.spawnProjectileFrom(dBody, newPhyValues);
                break;

            case EXPLODE_IN_FRAGMENTS:
                break;

            default:
        }
    }


    private boolean isProcessable(AbstractEntity entity) {
        return entity != null
                && this.state == ModelState.ALIVE
                && entity.getState() == EntityState.ALIVE;
    }


    private void spawnProjectileFrom(DynamicBody shooter, PhysicsValues shooterNewPhy) {
        if (!(shooter instanceof PlayerBody)) {
            return;
        }
        PlayerBody pBody = (PlayerBody) shooter;

        Weapon activeWeapon = pBody.getActiveWeapon();
        if (activeWeapon == null) {
            return;
        }

        WeaponDto weaponConfig = activeWeapon.getWeaponConfig();
        if (weaponConfig == null) {
            return;
        }

        double angleDeg = shooterNewPhy.angle;
        double angleRad = Math.toRadians(angleDeg);

        double dirX = Math.cos(angleRad);
        double dirY = Math.sin(angleRad);

        double angleInRads = Math.toRadians(shooterNewPhy.angle - 90);
        double spawnX = shooterNewPhy.posX + Math.cos(angleInRads) * weaponConfig.shootingOffeset;
        double spawnY = shooterNewPhy.posY + Math.sin(angleInRads) * weaponConfig.shootingOffeset;

        double projSpeedX = shooterNewPhy.speedX + weaponConfig.firingSpeed * dirX;
        double projSpeedY = shooterNewPhy.speedY + weaponConfig.firingSpeed * dirY;

        double accX = weaponConfig.acceleration * dirX;
        double accY = weaponConfig.acceleration * dirY;

        this.addDBody(
                weaponConfig.projectileAssetId, weaponConfig.projectileSize,
                spawnX, spawnY, projSpeedX, projSpeedY,
                accX, accY, angleDeg, 0d, 0d, 0d);
    }
}
