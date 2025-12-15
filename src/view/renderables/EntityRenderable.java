package view.renderables;


import _images.ImageCache;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class EntityRenderable {

    private final int entityId;
    private long lastFrameSeen;
    private final ImageCache cache;
    private EntityInfoDTO entityInfo = null;
    private BufferedImage image = null;


    public EntityRenderable(EntityInfoDTO renderInfo, ImageCache cache, long currentFrame) {
        this.entityId = renderInfo.entityId;
        this.lastFrameSeen = currentFrame;
        this.entityInfo = renderInfo;
        this.cache = cache;
        this.updateImageFromCache(renderInfo.assetId, (int) renderInfo.size, renderInfo.angle);
    }


    /**
     * PUBLICS
     */
    public long getLastFrameSeen() {
        return this.lastFrameSeen;
    }


    public EntityInfoDTO getEntityInfo() {
        return this.entityInfo;
    }


    public BufferedImage getImage() {
        return this.image;
    }


    public void update(EntityInfoDTO renderInfo, long currentFrame) {
        this.updateImageFromCache(renderInfo.assetId, (int) renderInfo.size, renderInfo.angle);
        this.lastFrameSeen = currentFrame;
        this.entityInfo = renderInfo;
    }


    public void paint(Graphics2D g) {

        if (this.image == null) {
            return;
        }

        AffineTransform defaultTransform = g.getTransform();

        AffineTransform mainRotation = AffineTransform.getRotateInstance(
                Math.toRadians(this.entityInfo.angle),
                this.entityInfo.posX, this.entityInfo.posY);

        g.setTransform(mainRotation);
        
        g.drawImage(
                this.image,
                (int) (this.entityInfo.posX - this.entityInfo.size / 2),
                (int) (this.entityInfo.posY - this.entityInfo.size / 2),
                null);
        g.setTransform(defaultTransform);
    }


    public void updateImageFromCache(EntityInfoDTO entityInfo) {
        this.updateImageFromCache(entityInfo.assetId, (int) entityInfo.size, entityInfo.angle);
    }


    private boolean updateImageFromCache(String assetId, int size, double angle) {
        boolean imageNeedsUpdate
                = this.image == null
                || this.entityInfo == null
                || !this.entityInfo.assetId.equals(assetId)
                || this.entityInfo.size != size
                || this.entityInfo.angle != angle;

        if (imageNeedsUpdate) {
            this.image = this.cache.getImage(angle, assetId, size);
            return true;
        }

        return false;
    }
}