package view.renderables;


import _images.ImageCache;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class DBodyRenderable extends EntityRenderable {

    private final boolean debugMode = false;


    public DBodyRenderable(DBodyInfoDTO bodyInfo, ImageCache cache, int currentFrame) {
        super(bodyInfo, cache, currentFrame);

    }


    /**
     * PUBLICS
     */
    @Override
    public void paint(Graphics2D g) {
        DBodyInfoDTO bodyInfo = (DBodyInfoDTO) this.getEntityInfo();

        super.paint(g);

        if (!this.debugMode) {
            return;
        }

        int x = (int) bodyInfo.posX;
        int y = (int) bodyInfo.posY;

        // Speed vector
        if ((bodyInfo.speedX != 0) || (bodyInfo.speedY != 0)) {
            g.setStroke(new BasicStroke(1f));
            g.setColor(Color.YELLOW);
            g.drawLine(x, y, x + (int) (bodyInfo.speedX / 4d), y + (int) (bodyInfo.speedY / 4d));
        }

        // Acc vector
        if ((bodyInfo.accX != 0) || (bodyInfo.accY != 0)) {
            g.setStroke(new BasicStroke(1.0f));
            g.setColor(Color.RED);
            g.drawLine(x, y, x + (int) (bodyInfo.accX) / 5, y + (int) (bodyInfo.accY) / 5);
        }
    }
}

// Arrow end -> to be debugged
// double angle = Math.atan2(y2 - y, x2 - x);
//            AffineTransform old = g.getTransform();
//            g.translate(x + (int) (bodyInfo.speedX) / 10, y + (int) (bodyInfo.speedY) / 10 + 5);
//            g.rotate(angle);
//            g.setFont(g.getFont().deriveFont(10f));
//            g.drawString("►", 0, 0);
//            g.setTransform(old);
