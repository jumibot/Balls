package view.renderables;


import _images.ImageCache;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class SBodyRenderable {

    private final int entityId;
    private int lastFrameSeen;
    private final ImageCache cache;
    private DBodyRenderInfoDTO renderInfo = null;
    private BufferedImage image = null;


    public SBodyRenderable(DBodyRenderInfoDTO renderInfo, ImageCache cache, int currentFrame) {
        this.entityId = renderInfo.entityId;
        this.lastFrameSeen = currentFrame;
        this.renderInfo = renderInfo;
        this.cache = cache;
        this.updateImageFromCache(renderInfo.assetId, renderInfo.size, renderInfo.angle);
    }


    /**
     * PUBLICS
     */
    public int getLastFrameSeen() {
        return this.lastFrameSeen;
    }


    public void update(DBodyRenderInfoDTO renderInfo, int currentFrame) {
        this.updateImageFromCache(renderInfo.assetId, renderInfo.size, renderInfo.angle);
        this.lastFrameSeen = currentFrame;
        this.renderInfo = renderInfo;
    }


    public void paint(Graphics2D g) {
        int x = (int) (this.renderInfo.posX - this.renderInfo.size);
        int y = (int) (this.renderInfo.posY - this.renderInfo.size);

        if (this.image != null) {
            g.drawImage(this.image, x, y, null);
        }
    }


    /**
     * PRIVATE
     */
    private boolean updateImageFromCache(String assetId, int size, double angle) {
        boolean imageNeedsUpdate
                = this.image == null
                || this.renderInfo == null
                || !this.renderInfo.assetId.equals(assetId)
                || this.renderInfo.size != size
                || this.renderInfo.angle != angle;

        if (imageNeedsUpdate) {
            this.image = this.cache.getImage(angle, assetId, size);
            return true;
        }
        
        return false;
    }
}
