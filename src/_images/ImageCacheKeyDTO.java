/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _images;


import java.awt.Color;
import java.util.Objects;


/**
 *
 * @author juanm
 */
public class ImageCacheKeyDTO {

    public int angle;
    public String assetId;
    public int size;


    public ImageCacheKeyDTO(int angle, String assetId, int size) {
        this.angle = angle;
        this.assetId = assetId;
        this.size = size;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageCacheKeyDTO)) {
            return false;
        }
        
        ImageCacheKeyDTO other = (ImageCacheKeyDTO) o;
        
        return angle == other.angle
                && size == other.size
                && this.assetId.equals(other.assetId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(angle, assetId, size);
    }
}
