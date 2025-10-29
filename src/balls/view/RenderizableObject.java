/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls.view;


import balls.physics.PhysicalValuesDTO;
import Helpers.DoubleVector;
import java.awt.Graphics;


/**
 *
 * @author juanm
 */
public class RenderizableObject {

    private final int id;
    private final int imageId;
    private final int maxSizeInPx;
    private final PhysicalValuesDTO phyValues;
    // Color
    // ...


    public RenderizableObject(int id, int imageId, int maxSizeInPx, PhysicalValuesDTO phyValues) {

        this.id = id;
        this.imageId = imageId;
        this.maxSizeInPx = maxSizeInPx;
        this.phyValues = phyValues;
    }


    public DoubleVector getCoordinates() {
        return this.phyValues.position;
    }


    public int getId() {
        return this.id;
    }


    public int getImageId() {
        return this.imageId;
    }


    public int getMaxSizeInPx() {
        return this.maxSizeInPx;
    }


    synchronized public void paint(Graphics gr) {

    }
}
