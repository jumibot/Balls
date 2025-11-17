/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _images;


import java.awt.Image;
import java.awt.image.BufferedImage;


/**
 *
 * @author juanm
 */
public class ImageDTO {

    public final int imageId;
    public final String uri;
    public final BufferedImage image;


    public ImageDTO(String uri, BufferedImage image) {
        this.uri = uri;
        this.image = image;
        this.imageId = this.uri.hashCode();
    }
}
