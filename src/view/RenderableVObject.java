/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import _helpers.DoubleVector;
import java.awt.Color;
import model.physics.PhysicsValuesDTO;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 *
 * @author juanm
 */
public class RenderableVObject {

    public final int id;
    public final int imageId;
    public final int maxSize;
    public final Color color;
    
    public final PhysicsValuesDTO phyValues;
    // ...


    public RenderableVObject(
            int id,
            int imageId,
            int maxSize,
            Color color,
            PhysicsValuesDTO phyValues) {

        this.id = id;
        this.imageId = imageId;
        this.maxSize = maxSize;
        this.color = color;
        this.phyValues = phyValues;
    }


    public void paint(Graphics2D gr, DoubleVector pos) {
        gr.setColor(this.color);
        gr.fillOval(
                (int) Math.round(pos.x - this.maxSize),
                (int) Math.round(pos.y - this.maxSize),
                2 * this.maxSize,
                2 * this.maxSize);
    }


    @Override
    public String toString() {
        return "RenderableObject<" + this.id
                + "> p" + this.phyValues.position
                + " s" + this.phyValues.speed;
    }
}
