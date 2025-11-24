/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _images;


import java.awt.Color;


/**
 *
 * @author juanm
 */
public class CachedImageKeyDTO {

    public double angle;
    public Color color;
    public int imageId;
    public int size;
    
    
    public CachedImageKeyDTO( double angle, Color color, int imageId, int size){
        this.angle = angle;
        this.color = color;
        this.imageId = imageId;
        this.size = size;
    }
}
