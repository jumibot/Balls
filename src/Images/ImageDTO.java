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
public class ImageDTO {
    public final String uri;
    public final Image image;
    
    public ImageDTO(String uri, Image image) {
        this.uri = uri;
        this.image = image;
    }
}
