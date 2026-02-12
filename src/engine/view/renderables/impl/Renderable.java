package engine.view.renderables.impl;

// region Imports
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.utils.images.ImageCache;
import engine.view.renderables.ports.RenderDTO;
// endregion

public class Renderable {

    // region Fields
    private final String renderableId;
    private final String assetId;
    private final ImageCache cache;

    private long lastFrameSeen;
    private RenderDTO renderData = null;
    private BufferedImage image = null;
    private String lastImageAssetId = null;
    private int lastImageAngle = Integer.MIN_VALUE;
    private int lastImageSize = -1;
    // endregion

    // region Constructors
    public Renderable(RenderDTO renderData, String assetId, ImageCache cache, long currentFrame) {
        if (assetId == null || assetId.isEmpty()) {
            throw new IllegalArgumentException("Asset ID not set");
        }
        if (cache == null) {
            throw new IllegalArgumentException("Image cache not set");
        }

        this.renderableId = renderData.entityId;
        this.assetId = assetId;
        this.lastFrameSeen = currentFrame;
        this.renderData = renderData;
        this.cache = cache;
        this.updateImageFromCache(this.assetId, (int) renderData.size, renderData.angle);
    }

    public Renderable(String renderableId, String assetId, ImageCache cache, long currentFrame) {
        if (renderableId == null || renderableId.isEmpty()) {
            throw new IllegalArgumentException("Entity ID not set");
        }
        if (assetId == null || assetId.isEmpty()) {
            throw new IllegalArgumentException("Asset ID not set");
        }
        if (cache == null) {
            throw new IllegalArgumentException("Image cache not set");
        }

        this.renderableId = renderableId;
        this.assetId = assetId;
        this.lastFrameSeen = currentFrame;
        this.cache = cache;
        this.image = null;
        this.renderData = null;
    }
    // endregion

    // *** PUBLIC ***

    // region Getters (get***)
    public long getLastFrameSeen() {
        return this.lastFrameSeen;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public String getRenderableId() {
        return this.renderableId;
    }

    public RenderDTO getRenderData() {
        return this.renderData;
    }

    public BufferedImage getImage() {
        return this.image;
    }
    // endregion

    public void paint(Graphics2D g, long currentFrame) {
        if (this.image == null) {
            throw new IllegalStateException("Cannot paint Renderable without an image " + this.assetId);
        }

        final double posX = this.renderData.posX;
        final double posY = this.renderData.posY;

        // Using the REAL size of the sprite for the offset
        final double halfW = this.image.getWidth(null) * 0.5;
        final double halfH = this.image.getHeight(null) * 0.5;

        final int drawX = (int) (posX - halfW);
        final int drawY = (int) (posY - halfH);

        g.drawImage(this.image, drawX, drawY, null);
    }

    public void releaseRenderData() {
        if (this.renderData != null)
            this.renderData.release();

        this.renderData = null;
    }

    // region Upadate (update***)
    public void update(RenderDTO renderInfo, long currentFrame) {
        this.updateImageFromCache(this.assetId, (int) renderInfo.size, renderInfo.angle);
        this.lastFrameSeen = currentFrame;
        this.renderData = renderInfo;
    }

    public void updateImageFromCache(RenderDTO entityInfo) {
        this.updateImageFromCache(this.assetId, (int) entityInfo.size, entityInfo.angle);
    }
    // endregion

    // *** PRIVATE ***

    private boolean updateImageFromCache(String assetId, int size, double angle) {
        int normalizedAngle = ((int) angle % 360 + 360) % 360;
        boolean imageNeedsUpdate = this.image == null
                || this.lastImageAssetId == null
                || !this.lastImageAssetId.equals(assetId)
                || this.lastImageSize != size
                || this.lastImageAngle != normalizedAngle;

        if (imageNeedsUpdate) {
            this.image = this.cache.getImage(normalizedAngle, assetId, size);
            this.lastImageAssetId = assetId;
            this.lastImageAngle = normalizedAngle;
            this.lastImageSize = size;

            return true; // ====
        }

        return false;
    }
}
