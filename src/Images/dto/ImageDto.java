/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images.dto;


import java.awt.Image;


/**
 *
 * @author juanm
 */
public class ImageDto {
    public final String uri;
    public final Image image;
    
    public ImageDto(String uri, Image image) {
        this.uri = uri;
        this.image = image;
    }
}
