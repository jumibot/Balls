/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.vobject;


import model.physics.PhysicsEngine;
import model.physics.PhysicsValuesDTO;
import view.RenderableVObject;
import _helpers.DoubleVector;
import java.awt.Color;
import java.awt.Dimension;
import model.Model;
import model.ModelState;


/**
 *
 * @author juanm
 */
public class VObject implements Runnable {

    /* TO-DO: Replace individual static counters by one static array of counters */
    private static int aliveQuantity = 0;
    private static int createdQuantity = 0;
    private static int deadQuantity = 0;

    private Model model;
    private Thread thread;
    private VObjectState state;

    private final int id;
    public final int imageId;
    public final int radius;
    private Color color;

    private final PhysicsEngine phyEngine; // Convert to Atomic Reference


    /**
     * CONSTRUCTORS
     */
    public VObject(int imageId, int radius, PhysicsEngine phyEngine) {

        this.model = null;
        this.thread = null;

        this.id = VObject.incCreatedQuantity();
        this.imageId = imageId;
        this.radius = radius;
        this.color = Color.BLUE;

        this.phyEngine = phyEngine;
        this.state = VObjectState.STARTING;
    }


    /**
     * PUBLICS
     */
    public void doMovement(PhysicsValuesDTO phyValues) {

        this.phyEngine.setPhysicsValues(phyValues);
    }


    public int getId() {
        return this.id;
    }


    public synchronized boolean activate() {
        if (this.model == null) {
            System.err.println("Activation error due vObject model is null! · (VObject)");
            return false;
        }

        if (!this.model.isAlive()) {
            System.err.println("Activation error due model is not alive! · (VObject)");
            return false;
        }

        if (this.state != VObjectState.STARTING) {
            System.err.println("Activation error due vObject is not starting! · (VObject)");
            return false;
        }

        VObject.incAliveQuantity();
        this.thread = new Thread(this);
        this.thread.setName("VObject Thread · " + this.id);
        this.thread.setPriority(Thread.NORM_PRIORITY-1);
        this.setState(VObjectState.ALIVE);
        this.thread.start();
        return true;
    }


    public synchronized void die() {
        if (this.state == VObjectState.ALIVE) {
            this.state = VObjectState.DEAD;
            VObject.deadQuantity++;
            VObject.aliveQuantity--;
        }
    }


    public  RenderableVObject buildRenderableObject() {
        if (this.state == VObjectState.DEAD || this.state == VObjectState.STARTING) {
            return null;
        }

        return new RenderableVObject(
                this.id, this.imageId, this.radius, this.color, this.phyEngine.getPhysicalValues()
        );
    }


    public VObjectState getState() {
        return this.state;
    }


    public void reboundInEast(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        this.color = Color.pink;
        this.phyEngine.reboundInEast(newPhyValues, oldPhyValues, worldDim);
    }


    public void reboundInWest(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        this.color = Color.yellow;
        this.phyEngine.reboundInWest(newPhyValues, oldPhyValues, worldDim);
    }


    public void reboundInNorth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        this.color = Color.red;
        this.phyEngine.reboundInNorth(newPhyValues, oldPhyValues, worldDim);
    }


    public void reboundInSouth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDim) {

        this.color = Color.cyan;
        this.phyEngine.reboundInSouth(newPhyValues, oldPhyValues, worldDim);
    }


    @Override
    public void run() {
        PhysicsValuesDTO newPhyValues;

        while ((this.getState() != VObjectState.DEAD)
                && (this.model.getState() != ModelState.STOPPED)) {

            if ((this.getState() == VObjectState.ALIVE)
                    && (this.model.getState() == ModelState.ALIVE)) {

                newPhyValues = this.phyEngine.calcNewPhysicsValues();
                this.model.processVObjectEvents(this, newPhyValues, this.phyEngine.getPhysicalValues());
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in vObject thread! (VObject) · " + ex.getMessage());
            }
        }
    }


    public void setModel(Model model) {
        this.model = model;
    }


    public void setState(VObjectState state) {
        this.state = state;
    }


    /**
     * PROTECTED
     */
    protected PhysicsValuesDTO getPhysicsValues() {
        return this.phyEngine.getPhysicalValues();
    }


    @Override
    public String toString() {
        return "VObject<" + this.id
                + "> p" + this.phyEngine.getPhysicalValues().position
                + " s" + this.phyEngine.getPhysicalValues().speed;
    }


    /**
     * PRIVATE
     */
    private DoubleVector adjustBounds(
            DoubleVector worldDimension, PhysicsValuesDTO phyValues, double delta) {

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
        return VObject.createdQuantity;
    }


    static public int getAliveQuantity() {
        return VObject.aliveQuantity;
    }


    static public int getDeadQuantity() {
        return VObject.deadQuantity;
    }


    static synchronized protected int incCreatedQuantity() {
        VObject.createdQuantity++;

        return VObject.createdQuantity;
    }


    static synchronized protected int incAliveQuantity() {
        VObject.aliveQuantity++;

        return VObject.aliveQuantity;
    }
}
