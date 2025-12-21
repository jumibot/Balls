package images;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


/**
 * ImageCache
 *
 * Caches render-ready images (wich are BufferedImage) indexed by a composite
 * key containing angle, color, imageId and size. This avoids regenerating
 * images on every frame and ensures that the Renderer can blit pre-built
 * GPU-compatible images at maximum performance.
 *
 * Each unique visual configuration is created once (putInCache()) using the
 * current GraphicsConfiguration, producing a hardware-accelerated, compatible
 * BufferedImage. Subsequent requests for the same parameters return the same
 * cached image, minimizing CPU work and memory churn during rendering.
 *
 * In the current implementation, createSprite() provides a fallback procedural
 * sprite (a colored circle).
 */
public class ImageCache {

    private GraphicsConfiguration gc;
    private Images baseImages;
    private final Map<ImageCacheKeyDTO, BufferedImage> cache = new HashMap<>(2048);
    private volatile long hits = 0;
    private volatile long fails = 0;


    public ImageCache(GraphicsConfiguration gc, Images baseImages) {
        this.gc = gc;
        this.baseImages = baseImages;
    }


    /**
     * PUBLICS
     */
    public BufferedImage getImage(int angle, String assetId, int size) {
        ImageCacheKeyDTO key = new ImageCacheKeyDTO(angle, assetId, size);
        BufferedImage image = this.cache.get(key);

        if (image == null) {
            this.fails++;
            image = this.putInCache(angle, assetId, size);
            this.cache.put(key, image);
        } else {
            this.hits++;
        }

        return image;
    }


    public long getHits() {
        return this.hits;
    }


    public double getHitsPercentage() {
        if (this.hits == 0) {
            return 0d;
        }

        double hitsPctg = (double) this.hits / (double) (this.hits + this.fails);
        return hitsPctg * 100d;
    }


    public long getFails() {
        return this.fails;
    }


    public int size() {
        return this.cache.size();
    }


    public void setGraphicsConfiguration(GraphicsConfiguration gc) {
        this.gc = gc;
    }


    /**
     * PRIVATES
     */
    private BufferedImage putInCache(int angle, String assetId, int size) {
        if (this.gc == null) {
            System.err.println("Graphics configuration is null · ImageCache");
            return null;  // =================================================>
        }

        BufferedImage image = gc.createCompatibleImage(size, size, Transparency.BITMASK);
        Graphics2D g2 = image.createGraphics();

        // Poner aquí la imagen que toca
        ImageDTO imageDto = this.baseImages.getImage(assetId);

        try {
            if (imageDto != null) {
                g2.drawImage(imageDto.image, 0, 0, size, size, null);
            } else {
                g2.setColor(Color.RED);
                g2.fillOval(0, 0, size, size); // se dibuja UNA vez
            }
        } finally {
            g2.dispose();
        }
        return image;
    }
}
