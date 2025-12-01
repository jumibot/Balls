package view.renderables;


import _images.ImageCache;
import java.awt.Graphics2D;


public class DBodyRenderable extends EntityRenderable {

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
        
        // Show vectors in future
    }
}
