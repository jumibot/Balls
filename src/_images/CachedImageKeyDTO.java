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
    public String assetId;
    public int size;
    
    
    public CachedImageKeyDTO( double angle, Color color, String assetId, int size){
        this.angle = angle;
        this.color = color;
        this.assetId = assetId;
        this.size = size;
    }
}
