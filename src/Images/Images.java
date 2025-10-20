/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images;


import Helpers.RandomArrayList;
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
    private RandomArrayList<ImageDto> images;
    private boolean imagesLoaded = false;
    private String assetsPath;
    private int imagesQuantity;


    /**
     * CONSTRUCTORS
     */
    public Images(String assetsPath) {
        this.assetsPath = assetsPath;
        this.imagesQuantity = 0;
    }


    /**
     * PUBLIC
     */
    public void addImageToManifest(String uri) {
        this.imagesManifest.add(uri);
    }


    public ImageDto getImage() {
        if (!this.imagesLoaded) {
            this.loadAllImages();
        }

        return this.images.choice();
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


    private ImageDto loadImage(String fileName) {
        ImageDto imageDto;
        Image image;

        imageDto = null;
        try {
            image = ImageIO.read(new File(fileName));
            this.imagesQuantity++;
            imageDto = new ImageDto(this.imagesQuantity, image);

        } catch (IOException e) {
            System.err.println("Load image error · <Images> · [" + fileName + "] · " + e.getMessage());
            imageDto = null;
        }

        return imageDto;
    }
}
