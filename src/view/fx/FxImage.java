package view.fx;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


/**
 *
 * @author juanm
 */
public class FxImage {

    private BufferedImage image;
    private BufferedImage imageScaled;
    private Fx fx;

    private double imgDim_x;
    private double imgDim_y;
    private double imgDimScaled_x;
    private double imgDimScaled_y;

    private double offset_x;
    private double offset_y;
    private double angle;
    private double scale;

    private Rectangle2D.Double boundingBox;
    private Ellipse2D.Double boundingEllipse;
    private Color boundingBoxColor;


    /**
     * CONSTRUCTOR
     */
    public FxImage() {
        this.init();
    }


    public FxImage(BufferedImage bfImage) {
        this.init();

        this.setImageAndDimension(bfImage);
    }


    /**
     * PRIVATES
     */
    private void init() {
        this.image = null;
        this.imageScaled = null;
        this.fx = null;
        this.imgDim_x = 0d;
        this.imgDim_y = 0d;
        this.offset_x = 0d;
        this.offset_y = 0d;
        this.angle = 0d;

        this.imgDimScaled_x = 0;
        this.imgDimScaled_y = 0;
        this.boundingBoxColor = Color.GRAY;
        this.boundingEllipse = new Ellipse2D.Double();
        this.boundingBox = new Rectangle2D.Double(0, 0, 0, 0);
        this.scale = 1;
    }


    private void updateImageScaled() {
        this.imgDimScaled_x = this.imgDim_x * this.getScale();
        this.imgDimScaled_y = this.imgDim_y * this.getScale();

        if (this.imgDimScaled_x == 0 || this.imgDimScaled_y == 0) {
            return;
        }

        int type = this.image.getType();
        if (type == 0) {
            type = BufferedImage.TYPE_INT_ARGB;
        }

        this.imageScaled
                = new BufferedImage(
                        (int) this.imgDimScaled_x,
                        (int) this.imgDimScaled_y,
                        type);

        Graphics2D g = this.imageScaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(this.image, 0, 0,
                    (int) this.imgDimScaled_x,
                    (int) this.imgDimScaled_y,
                    null);

        g.dispose();
    }


    /**
     * PUBLICS
     */
    public void rotate(double rotation) {
        this.angle += rotation;
    }


    public Fx getAnimation() {
        return this.fx;
    }


    public Rectangle2D.Double getBoundingBox(double x, double y) {

        this.boundingBox.x = x - this.imgDimScaled_x / 2;
        this.boundingBox.y = y - this.imgDimScaled_y / 2;
        this.boundingBox.width = this.imgDimScaled_x;
        this.boundingBox.height = this.imgDimScaled_y;

        return this.boundingBox;
    }


    public Color getBoundingBoxColor() {
        return this.boundingBoxColor;
    }


    public Ellipse2D.Double getBoundingEllipse(double x, double y) {
        this.boundingEllipse.x = x - this.imgDimScaled_x * 0.9d / 2;
        this.boundingEllipse.y = y - this.imgDimScaled_y * 0.9d / 2;
        this.boundingEllipse.width = this.imgDimScaled_x * 0.9d;
        this.boundingEllipse.height = this.imgDimScaled_y * 0.9d;

        return this.boundingEllipse;
    }


    public BufferedImage getImage() {
        // return this.image;
        return this.imageScaled;
    }


    public double getAngle() {
        return this.angle;
    }


    public double getScale() {
        return this.scale;
    }


    public void setAnimation(Fx animation) {
        this.fx = animation;
    }


    public void setBoundingBoxColor(Color color) {
        this.boundingBoxColor = color;
    }


    public void setImage(BufferedImage image) {
        this.image = image;
        this.updateImageScaled();
    }


    public void setImageAndDimension(BufferedImage image) {
        this.imgDimScaled_x = image.getWidth();
        this.imgDimScaled_y = image.getHeight();
        this.image = image;
        this.updateImageScaled();
    }


    public void setOffset(double x, double y) {
        this.offset_x = x;
        this.offset_y = x;
    }


    public void setAngle(double rotation) {
        this.angle = rotation;
    }


    public void setScale(double scale) {
        this.scale = scale;
        this.updateImageScaled();
    }


    public void updateScale(double factor) {
        this.scale *= factor;

        this.updateImageScaled();
    }

}
