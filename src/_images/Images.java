package _images;


import _helpers.RandomArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


    public Images(String assetsPath, ArrayList<String> imagesManifest) {
        this.imagesManifest = new RandomArrayList(128);
        this.assetsPath = assetsPath;
        this.imagesQuantity = 0;

        this.add(imagesManifest);
    }


    /**
     * PUBLIC
     */
    public void add(String fileName) {
        // fileName without a path
        this.imagesManifest.add(fileName);
        this.images.put(fileName.hashCode(), this.loadImage(assetsPath + fileName));
    }


    public void add(ArrayList<String> manifest) {
        // fileName without a path

        for (String imageUri : manifest) {
            this.imagesManifest.add(imageUri);
            this.images.put(imageUri.hashCode(), this.loadImage(assetsPath + imageUri));
        }
    }


    public ImageDTO getImage(int imageId) {
        if (!this.isLoaded) {
            this.loadAllImages();
        }

        return this.images.get(imageId);
    }


    public int getImageQuantity() {
        return this.imagesQuantity;
    }


    public ImageDTO getRamdomImageDTO() {
        if (!this.isLoaded) {
            this.loadAllImages();
        }

        return this.choice();
    }


    public BufferedImage getRamdomBufferedImage() {
        if (!this.isLoaded) {
            this.loadAllImages();
        }

        return this.choice().image;
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
