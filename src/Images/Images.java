/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Images;


import Helpers.RandomArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author juanm
 */
public class Images {

    private static RandomArrayList<BufferedImage> bImages;
    private static boolean imagesLoaded = false;


    /**
     * STATICS
     */
    public static BufferedImage getImage() {
        if (!Images.imagesLoaded) {
            Images.loadAllImages();
        }

        return Images.bImages.choice();
    }


    public static void loadAllImages() {
        if (Images.imagesLoaded) {
            return;
        }

        String assetsPath = "src/tg/images/assets/";

        Images.bImages.add( Images.loadImage(assetsPath + "asteroid-1-mini.png"));
        Images.bImages.add( Images.loadImage(assetsPath + "asteroid-2-mini.png"));
        Images.bImages.add( Images.loadImage(assetsPath + "spaceship-1.png"));
        Images.bImages.add( Images.loadImage(assetsPath + "spaceship-2.png"));


        Images.imagesLoaded = true;

        System.out.println("All images loaded!");
    }


    private static BufferedImage loadImage(String fileName) {
        BufferedImage img;

        img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.err.println("Load image error · <Images> · [" + fileName + "] · " + e.getMessage());
            img = null;
        }

        return img;
    }
}
