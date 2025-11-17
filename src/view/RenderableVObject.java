/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import java.awt.Color;
import model.physics.PhysicsValuesDTO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/**
 *
 * @author juanm
 */
public class RenderableVObject {

    private final int id;
    private final int imageId;
    private final Color color;

    private int maxSize;
    private BufferedImage image;
    private PhysicsValuesDTO phyValues;
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


    public void paint(Graphics2D gr) {
        gr.setColor(this.color);
        gr.fillOval(
                (int) (this.phyValues.position.x - this.maxSize),
                (int) (this.phyValues.position.y - this.maxSize),
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
