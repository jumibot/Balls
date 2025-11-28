package controller;


import _helpers.DoubleVector;
import assets.Assets;
import java.awt.Dimension;
import view.renderables.DBodyRenderInfoDTO;
import view.View;
import model.Model;
import model.entities.DynamicBody;
import model.entities.BodyAction;
import model.EventType;
import java.util.ArrayList;
import world.WorldDefinition;


public class Controller {

    private Assets assets;
    private volatile ControllerState controllerState = ControllerState.STARTING;
    private int maxVisualObjects;
    private Model model;
    private View view;
    private WorldDefinition world;
    private Dimension worldDimension;


    public Controller() {
    }


    public Controller(View view, Model model, Assets assets) {
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

        if (this.maxVisualObjects <= 0) {
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
        this.model.setMaxDBodyObjects(this.maxVisualObjects);
        this.model.activate();

        this.controllerState = ControllerState.ALIVE;
    }


    public void addDynamicBody(
            String assetId, int size,
            DoubleVector pos, DoubleVector speed, DoubleVector acc,
            double angle) {

        this.model.addDBody(
                assetId, size, pos.x, pos.y, speed.x, speed.y, acc.x, acc.y, angle);
    }


    public BodyAction decideAction(EventType eventType) {
        BodyAction vObjectAction;

        switch (eventType) {
            case NORTH_LIMIT_REACHED:
                vObjectAction = BodyAction.REBOUND_IN_NORTH;
                break;

            case SOUTH_LIMIT_REACHED:
                vObjectAction = BodyAction.REBOUND_IN_SOUTH;
                break;

            case EAST_LIMIT_REACHED:
                vObjectAction = BodyAction.REBOUND_IN_EAST;
                break;

            case WEST_LIMIT_REACHED:
                vObjectAction = BodyAction.REBOUND_IN_WEST;
                break;

            default:
                // To avoid zombie state
                vObjectAction = BodyAction.NONE;
        }

        return vObjectAction;
    }


    public BodyAction decideAction(EventType eventType, ArrayList<DynamicBody> RelatedDBody) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public ControllerState getState() {
        return this.controllerState;
    }


    public ArrayList<DBodyRenderInfoDTO> getDBodyRenderInfo() {
        return this.model.getDBodyRenderInfo();
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


    public void setMaxVisualObjects(int maxVisualObjects) {
        this.maxVisualObjects = maxVisualObjects;
    }
}
