package view.fx;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import view.DBodyRenderable;


/**
 *
 * @author juanm
 */
public abstract class Fx extends Thread {

    private long delayMills;
    private DBodyRenderable renderable;
    private FxImage fxImage;
    private FxTyoe animationType;


    /**
     * CONSTRUCTORS
     */
    public Fx() {
        this.delayMills = 0;
    }


    /**
     * STATICS
     */
    static public BufferedImage loadImage(String fileName) {
        BufferedImage img;

        img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.err.println("ERROR: Loading image · <Animation> · [" + fileName + "] · " + e.getMessage());
            img = null;
        }

        return img;
    }


    /**
     * PUBLICS
     */
    public FxTyoe getAnimationType() {
        return this.animationType;
    }


    public long getDelayMillis() {
        return this.delayMills;
    }


    public DBodyRenderable getRenderable() {
        return this.renderable;
    }


    public FxImage getVOImage() {
        return this.fxImage;
    }


    public boolean isVOImageSetted() {
        return this.fxImage != null;
    }


    public void setAnimationType(FxTyoe animationType) {
        this.animationType = animationType;
    }


    public void setDelayMillis(long delayMillis) {
        this.delayMills = delayMillis;
    }


    public void setRenderable(DBodyRenderable renderable) {
        this.renderable = renderable;
    }


    public void setVOImage(FxImage fxImg) {
        this.fxImage = fxImg;
        this.fxImage.setAnimation(this);
    }

}
