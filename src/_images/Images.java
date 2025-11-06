package _images;


import _helpers.RandomArrayList;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author juanm
 */
public class Images {

    private RandomArrayList<String> imagesManifest;
    private RandomArrayList<ImageDTO> images;
    private boolean imagesLoaded = false;
    private String assetsPath;
    private int imagesQuantity;


    /**
     * CONSTRUCTORS
     */
    public Images(String assetsPath) {
        this.imagesManifest = new RandomArrayList(100);
        this.images = new RandomArrayList(100);
        this.assetsPath = assetsPath;
        this.imagesQuantity = 0;
    }


    /**
     * PUBLIC
     */
    public void addImageToManifest(String fileName) {
        // fileName without a path
        this.imagesManifest.add(fileName);
    }


    public ImageDTO getRamdomImage() {
        if (!this.imagesLoaded) {
            this.loadAllImages();
        }

        return this.images.choice();
    }


    public ImageDTO getImage(int order) {
        if (!this.imagesLoaded) {
            this.loadAllImages();
        }

        if (order >= 0 && order <= this.imagesQuantity) {
            System.out.println("The order of image specified is not avalaible · IMAGES");
            return this.images.get(order);
        } else {
            return this.images.choice();
        }
    }


    public int getImageQuantity() {
        return this.imagesQuantity;
    }


    public void loadAllImages() {
        if (this.imagesLoaded) {
            return;
        }

        for (String imageUri : this.imagesManifest) {
            this.images.add(this.loadImage(assetsPath + imageUri));
        }

        this.imagesLoaded = true;
        System.out.println("All images loaded!");
    }


    private ImageDTO loadImage(String uri) {
        ImageDTO imageDto;
        Image image;

        imageDto = null;
        try {
            image = ImageIO.read(new File(uri));
            this.imagesQuantity++;
            imageDto = new ImageDTO(uri, image);

        } catch (IOException e) {
            System.err.println("Load image error · <Images> · [" + uri + "] · " + e.getMessage());
            imageDto = null;
        }

        return imageDto;
    }
}
