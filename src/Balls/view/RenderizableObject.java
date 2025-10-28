/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.view;


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
    private DoubleVector coordinates;
    // Color
    // ...


    public RenderizableObject(int id, int imageId, int maxSizeInPx, DoubleVector coordinates) {

        this.id = id;
        this.imageId = imageId;
        this.maxSizeInPx = maxSizeInPx;
        this.coordinates = coordinates;
    }


    public DoubleVector getCoordinates() {
        return this.coordinates;
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
