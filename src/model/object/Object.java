/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.object;


import model.physics.BasicPhysicsEngine;
import model.physics.PhysicsValuesDTO;
import view.RenderableObject;
import _helpers.DoubleVector;
import _helpers.Position;
import java.awt.Color;
import model.Model;
import model.ModelState;


/**
 *
 * @author juanm
 */
public class Object implements Runnable {

    /* TO-DO: Replace individual static counters by one static array of counters */
    private static int aliveQuantity = 0;
    private static int createdQuantity = 0;
    private static int deadQuantity = 0;

    private Model model;
    private Thread thread;
    private ObjectState state;

    private final int id;
    public final int imageId;
    public final int radius;
    private Color color;

    private final BasicPhysicsEngine phyEngine; // Convert to Atomic Reference


    /**
     * CONSTRUCTORS
     */
    public Object(
            int imageId,
            int radius,
            BasicPhysicsEngine phyEngine) {
        this.model = null;
        this.thread = null;

        this.id = Object.incCreatedQuantity();
        this.imageId = imageId;
        this.radius = radius;
        this.color = Color.BLUE;

        this.phyEngine = phyEngine;
        this.state = ObjectState.STARTING;
    }


    /**
     * PUBLICS
     */
    public void doMovement(PhysicsValuesDTO phyValues) {

//        System.out.println("Do movement · Ball <" + this.id + "> " + phyValues.position);
        this.phyEngine.doMovement(phyValues);
    }


    public int getId() {
        return this.id;
    }


    public synchronized boolean activate() {
        if (this.model == null) {
            System.err.println("Activation error due ball model is null! · (Ball)");
            return false;
        }

        if (!this.model.isAlive()) {
            System.err.println("Activation error due model is not alive! · (Ball)");
            return false;
        }

        if (this.state != ObjectState.STARTING) {
            System.err.println("Activation error due ball is not starting! · (Ball)");
            return false;
        }

        Object.incAliveQuantity();
        this.thread = new Thread(this);
        this.thread.setName("Ball Thread · " + this.id);

        this.setState(ObjectState.ALIVE);
        this.thread.start();
        return true;
    }


    public synchronized void die() {
        if (this.state == ObjectState.ALIVE) {
            this.state = ObjectState.DEAD;
            Object.deadQuantity++;
            Object.aliveQuantity--;
        }
    }


    public synchronized RenderableObject getRenderableObject() {
        if (this.state == ObjectState.DEAD || this.state == ObjectState.STARTING) {
            return null;
        }

        return new RenderableObject(
                this.id,
                this.imageId,
                this.radius,
                this.color,
                this.phyEngine.getPhysicalValues()
        );
    }


    public ObjectState getState() {
        return this.state;
    }


    public void reboundInEast(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            DoubleVector worldDimension) {

        this.color = Color.pink;
        this.phyEngine.reboundInEast(newPhyValues, oldPhyValues, worldDimension);
    }


    public void reboundInWest(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            DoubleVector worldDimension) {

        this.color = Color.yellow;
        this.phyEngine.reboundInWest(newPhyValues, oldPhyValues, worldDimension);
    }


    public void reboundInNorth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues, DoubleVector worldDimension) {

        this.color = Color.red;
        this.phyEngine.reboundInNorth(newPhyValues, oldPhyValues, worldDimension);
    }

    public void reboundInSouth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues, DoubleVector worldDimension) {

        this.color = Color.cyan;
        this.phyEngine.reboundInSouth(newPhyValues, oldPhyValues, worldDimension);
    }


    public void setModel(Model model) {
        this.model = model;
    }


    public synchronized void setState(ObjectState state) {
        this.state = state;
    }


    /**
     * PROTECTED
     */
    protected PhysicsValuesDTO getPhysicsValues() {
        return this.phyEngine.getPhysicalValues();
    }


    @Override
    public void run() {
        PhysicsValuesDTO newPhyValues;

        while ((this.getState() != ObjectState.DEAD)
                && (this.model.getState() != ModelState.STOPPED)) {

            if ((this.getState() == ObjectState.ALIVE)
                    && (this.model.getState() == ModelState.ALIVE)) {

                newPhyValues = this.phyEngine.calcNewPhysicsValues();
                this.model.processBallEvents(this, newPhyValues, this.phyEngine.getPhysicalValues());
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in ball thread! (Ball) · " + ex.getMessage());
            }
        }
    }


    @Override
    public String toString() {
        return "Ball<" + this.id
                + "> p" + this.phyEngine.getPhysicalValues().position
                + " s" + this.phyEngine.getPhysicalValues().speed;
    }


    /**
     * PRIVATE
     */
    private DoubleVector adjustBounds(
            DoubleVector worldDimension,
            PhysicsValuesDTO phyValues, double delta) {

        double x = Math.max(phyValues.position.x, delta);
        x = Math.min(x, worldDimension.x - delta);

        double y = Math.max(phyValues.position.y, delta);
        y = Math.min(y, worldDimension.y - delta);

        return new DoubleVector(x, y);
    }


    /**
     * STATICS
     */
    static public int getCreatedQuantity() {
        return Object.createdQuantity;
    }


    static public int getAliveQuantity() {
        return Object.aliveQuantity;
    }


    static public int getDeadQuantity() {
        return Object.deadQuantity;
    }


    static synchronized protected int incCreatedQuantity() {
        Object.createdQuantity++;

        return Object.createdQuantity;
    }


    static synchronized protected int incAliveQuantity() {
        Object.aliveQuantity++;

        return Object.aliveQuantity;
    }
}
