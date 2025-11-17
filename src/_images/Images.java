package _images;


import _helpers.RandomArrayList;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;


/**
 *
 * @author juanm
 */
public class Images {

    private final String assetsPath;

    private final Map<Integer, ImageDTO> images = new ConcurrentHashMap<>(128);
    private final RandomArrayList<String> imagesManifest;

    private int imagesQuantity;
    private boolean isLoaded = false;


    /**
     * CONSTRUCTORS
     */
    public Images(String assetsPath) {
        this.imagesManifest = new RandomArrayList(128);
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


    public ImageDTO getImage(int order) {
        if (!this.isLoaded) {
            this.loadAllImages();
        }

        if (order >= 0 && order <= this.imagesQuantity) {
            System.out.println("The order of image specified is not avalaible · IMAGES");
            return this.images.get(order);
        } else {
            return this.choice();
        }
    }


    public int getImageQuantity() {
        return this.imagesQuantity;
    }


    public ImageDTO getRamdomImage() {
        if (!this.isLoaded) {
            this.loadAllImages();
        }

        return this.choice();
    }


    /**
     * PRIVATE
     */
    private ImageDTO choice() {
        int imageId = this.imagesManifest.choice().hashCode();

        return this.images.get(imageId);
    }


    private void loadAllImages() {
        if (this.isLoaded) {
            return;
        }

        for (String imageUri : this.imagesManifest) {
            this.images.put(
                    imageUri.hashCode(),
                    this.loadImage(assetsPath + imageUri));
        }

        this.isLoaded = true;
        System.out.println("All images loaded!");
    }


    private ImageDTO loadImage(String uri) {
        ImageDTO imageDto = null;
        BufferedImage image;

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
