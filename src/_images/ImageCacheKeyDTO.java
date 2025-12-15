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
public class ImageCacheKeyDTO {

    public double angle;
    public String assetId;
    public int size;
    
    
    public ImageCacheKeyDTO( double angle, String assetId, int size){
        this.angle = angle;
        this.assetId = assetId;
        this.size = size;
    }
}
