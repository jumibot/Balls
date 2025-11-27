package _images;


import java.awt.image.BufferedImage;


/**
 * Data holder that represents a loaded image resource. Stores the
 * image URI, the BufferedImage instance, and a stable identifier derived from
 * the URI's hash code. Used by the Images manager to reference and retrieve
 * image assets efficiently.
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
