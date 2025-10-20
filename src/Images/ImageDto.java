/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images;


import java.awt.Image;


/**
 *
 * @author juanm
 */
public class ImageDto {
    public final int id;
    public final Image image;
    
    public ImageDto(int id, Image image) {
        this.id = id;
        this.image = image;
    }
}
