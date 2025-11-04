/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls.view;


import balls.physics.PhysicsValuesDTO;
import helpers.DoubleVector;
import java.awt.Graphics;


/**
 *
 * @author juanm
 */
public class RenderableObject {

    public final int id;
    public final int imageId;
    public final int maxSizeInPx;
    public final PhysicsValuesDTO phyValues;
    // Color
    // ...


    public RenderableObject(int id, int imageId, int maxSizeInPx, PhysicsValuesDTO phyValues) {

        this.id = id;
        this.imageId = imageId;
        this.maxSizeInPx = maxSizeInPx;
        this.phyValues = phyValues;
    }


    synchronized public void paint(Graphics gr) {

    }


    @Override
    public String toString() {
        return "RenderableObject<" + this.id
                + "> p" + this.phyValues.position
                + " s" + this.phyValues.speed;
    }
}
