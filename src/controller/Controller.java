package controller;


import _helpers.DoubleVector;
import java.awt.Dimension;
import view.RenderInfoDTO;
import view.View;
import model.Model;
import model.entities.DynamicBody;
import model.entities.BodyAction;
import model.EventType;
import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class Controller {

    private Model model;
    private View view;
    private volatile ControllerState controllerState = ControllerState.STARTING;
    private String assetsPath;
    private ArrayList<String> backgroundFiles;
    private ArrayList<String> asteroidFiles;
    private ArrayList<String> playerFiles;
    private Dimension worldDimension;
    private int maxVisualObjects;

    private RandomWorldDTO lifeParameters = null;
    private RandomWorld lifeGenerator = null;


    public Controller() {
    }


    public Controller(View view, Model model) {

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

        if (this.assetsPath == null) {
            throw new IllegalArgumentException("Null path");
        }

        if (this.asteroidFiles == null || this.backgroundFiles == null || this.playerFiles == null) {
            throw new IllegalArgumentException("Null file list");
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

        this.view.setAssets(this.assetsPath, this.backgroundFiles, this.asteroidFiles, this.playerFiles);
        this.view.setDimension(this.worldDimension);
        this.view.activate();

        this.model.setDimension(this.worldDimension);
        this.model.setMaxVisualObjects(this.maxVisualObjects);
        this.model.activate();

        this.controllerState = ControllerState.ALIVE;
    }


    public void addDynamicBody(
            int imageId, int size,
            DoubleVector pos, DoubleVector speed, DoubleVector acc,
            double angle) {

        this.model.addDynamicBody(
                imageId, size, pos.x, pos.y, speed.x, speed.y, acc.x, acc.y, angle);
    }


    public void addDynamicBody(DynamicBody newVObject) {
        this.model.addVObject(newVObject);
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


    public BodyAction decideAction(EventType eventType, ArrayList<DynamicBody> RelatedVObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void generateRandomLife(int maxCreationDelay,
            double maxMass, double minMass,
            double speed_x, double speed_y,
            double acc_x, double acc_y,
            int maxSize, int minSize) {

        RandomDBodyDTO dBodyParams = new RandomDBodyDTO(
                maxSize, minSize,
                maxMass, minMass,
                speed_x, speed_y,
                acc_x, acc_y);

        RandomSBodyDTO sBodyParams = new RandomSBodyDTO(
                maxSize, minSize,
                maxMass, minMass);

        this.lifeParameters = new RandomWorldDTO(
                maxCreationDelay, dBodyParams, sBodyParams);

        this.lifeGenerator = new RandomWorld(this, this.lifeParameters, this.asteroidFiles);
        this.lifeGenerator.activate();
    }


    public void generateRandomLife(int maxCreationDelay,
            double maxMass, double minMass,
            double speedMaxModule, double accMaxModule,
            int maxSize, int minSize) {

        RandomDBodyDTO dBodyParams = new RandomDBodyDTO(
                maxSize, minSize,
                maxMass, minMass,
                speedMaxModule, accMaxModule);

        RandomSBodyDTO sBodyParams = new RandomSBodyDTO(
                maxSize, minSize,
                maxMass, minMass);

        this.lifeParameters = new RandomWorldDTO(
                maxCreationDelay, dBodyParams, sBodyParams);

        this.lifeGenerator = new RandomWorld(this, this.lifeParameters, this.asteroidFiles);
        this.lifeGenerator.activate();
    }


    public ControllerState getState() {
        return this.controllerState;
    }


    public ArrayList<RenderInfoDTO> getRenderableObjects() {
        return this.model.getRenderableObjects();
    }


    public Dimension getWorldDimension() {
        return this.worldDimension;
    }


    public void setAssets(String assetsPath, ArrayList<String> background,
            ArrayList<String> asteroid, ArrayList<String> player) {

        this.assetsPath = assetsPath;
        this.backgroundFiles = background;
        this.asteroidFiles = asteroid;
        this.playerFiles = player;
    }


    public void setModel(Model model) {
        this.model = model;
        this.model.setController(this);
    }


    public void setView(View view) {
        this.view = view;
        this.view.setController(this);
    }


    public void setWorldDimension(int width, int height) {
        this.worldDimension = new Dimension(width, height);
    }


    public void setMaxVisualObjects(int maxVisualObjects) {
        this.maxVisualObjects = maxVisualObjects;
    }

}
