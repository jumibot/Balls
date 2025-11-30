package controller;


import assets.Assets;
import java.awt.Dimension;
import view.renderables.DBodyInfoDTO;
import view.View;
import model.Model;
import model.entities.DynamicBody;
import model.entities.BodyAction;
import model.EventType;
import java.util.ArrayList;
import view.renderables.EntityInfoDTO;
import world.WorldDefinition;


public class Controller {

    private Assets assets;
    private volatile ControllerState controllerState;
    private int maxDBody;
    private Model model;
    private View view;
    private WorldDefinition world;
    private Dimension worldDimension;


    public Controller() {
        this.controllerState = ControllerState.STARTING;
    }


    public Controller(View view, Model model, Assets assets) {
        this.controllerState = ControllerState.STARTING;
        this.assets = assets;

        this.setModel(model);
        this.setView(view);
    }


    /**
     * PUBLICS
     */
    public void activate() {
        if (this.worldDimension == null) {
            throw new IllegalArgumentException("Null world dimension");
        }

        if (this.assets == null) {
            throw new IllegalArgumentException("Assets are not setted");
        }

        if (this.assets == null) {
            throw new IllegalArgumentException("Assets are not setted");
        }

        if (this.world == null) {
            throw new IllegalArgumentException("World definition not setted");
        }

        if (this.maxDBody <= 0) {
            throw new IllegalArgumentException("Max visual objects not setted");
        }

        if (this.view == null) {
            throw new IllegalArgumentException("No view injected");
        }

        if (this.model == null) {
            throw new IllegalArgumentException("No model injected");
        }

        this.view.loadAssets(this.assets, this.world);
        this.view.setDimension(this.worldDimension);
        this.view.activate();

        this.model.setDimension(this.worldDimension);
        this.model.setMaxDBody(this.maxDBody);
        this.model.activate();

        this.controllerState = ControllerState.ALIVE;
    }


    public void addDBody(String assetId, double size, double posX, double posY,
            double speedX, double speedY, double accX, double accY, double angle) {

        this.model.addDBody(
                assetId, size, posX, posY, speedX, speedY, accX, accY, angle);

    }


    public void addSBody(
            String assetId, double size, double posX, double posY, double angle) {

        this.model.addSBody(assetId, size, posX, posY, angle);

        ArrayList<EntityInfoDTO> bodiesInfo = this.getSBodyInfo();
        this.view.updateSBodyInfo(bodiesInfo);
    }


    public void addDecorator(String assetId, double size, double posX, double posY, double angle) {
        this.model.addDecorator(assetId, size, posX, posY, angle);

        ArrayList<EntityInfoDTO> decosInfo = this.getDecoratorInfo();
        this.view.updateDecoratorsInfo(decosInfo);
    }


    public BodyAction decideAction(EventType eventType) {
        BodyAction bAction;

        switch (eventType) {
            case NORTH_LIMIT_REACHED:
                bAction = BodyAction.DIE;
                break;

            case SOUTH_LIMIT_REACHED:
                bAction = BodyAction.DIE;
                break;

            case EAST_LIMIT_REACHED:
                bAction = BodyAction.DIE;
                break;

            case WEST_LIMIT_REACHED:
                bAction = BodyAction.DIE;
                break;

            default:
                // To avoid zombie state
                bAction = BodyAction.NONE;
        }

        return bAction;
    }


    public BodyAction decideAction(EventType eventType, ArrayList<DynamicBody> RelatedDBody) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public ControllerState getState() {
        return this.controllerState;
    }


    public ArrayList<DBodyInfoDTO> getDBodyInfo() {
        return this.model.getDBodyInfo();
    }


    private ArrayList<EntityInfoDTO> getDecoratorInfo() {
        return this.model.getDecoratorsInfo();
    }


    public ArrayList<EntityInfoDTO> getSBodyInfo() {
        return this.model.getSBodyInfo();
    }


    public Dimension getWorldDimension() {
        return this.worldDimension;
    }


    public void setAssets(Assets assets) {
        this.assets = assets;
    }


    public void setModel(Model model) {
        this.model = model;
        this.model.setController(this);
    }


    public void setView(View view) {
        this.view = view;
        this.view.setController(this);
    }


    public void setWorld(WorldDefinition world) {
        this.world = world;
    }


    public void setWorldDimension(int width, int height) {
        this.worldDimension = new Dimension(width, height);
    }


    public void setMaxDBody(int maxDBody) {
        this.maxDBody = maxDBody;
    }
}
