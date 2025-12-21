package view.renderables;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import images.ImageCache;


public class DBodyRenderable extends EntityRenderable {

    private final boolean debugMode = false;


    public DBodyRenderable(DBodyInfoDTO bodyInfo, ImageCache cache, long currentFrame) {
        super(bodyInfo, cache, currentFrame);

    }


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
