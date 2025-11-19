
package _images;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * SpriteCache
 * 
 * Caches render-ready sprites (wich are BufferedImage) indexed by a composite 
 * key containing angle, color, imageId and size. This avoids regenerating 
 * sprites on every frame and ensures that the Renderer can blit pre-built 
 * GPU-compatible images at maximum performance.
 *
 * Each unique visual configuration is created once (createSprite) using the
 * current GraphicsConfiguration, producing a hardware-accelerated, compatible
 * BufferedImage. Subsequent requests for the same parameters return the same
 * cached image, minimizing CPU work and memory churn during rendering.
 *
 * In the current implementation, createSprite() provides a fallback procedural
 * sprite (a colored circle).
 */
public class SpriteCache {

    private GraphicsConfiguration gc;
    private final Map<SpriteKeyDTO, BufferedImage> cache = new HashMap<>(2048);


    public SpriteCache(GraphicsConfiguration gc) {
        this.gc = gc;
    }


    /**
     * PUBLICS
     */
    public BufferedImage getSprite(double angle, Color color, int imageId, int size) {
        SpriteKeyDTO key = new SpriteKeyDTO(angle, color, imageId, size);
        BufferedImage sprite = this.cache.get(key);

        if (sprite == null) {
            sprite = this.createSprite(angle, color, imageId, size);
            this.cache.put(key, sprite);
        }
        return sprite;
    }


    public void setGraphicsConfiguration(GraphicsConfiguration gc) {
        this.gc = gc;
    }


    /**
     * PRIVATES
     */
    private BufferedImage createSprite(double angle, Color color, int imageId, int size) {
        // To-do: Create sprite using imageId and angle.
        
        if (this.gc == null) {
            System.out.println("Error creating sprite. Graphics configuration is null · SpriteCache");
            return null;  // =================================================>
        }

        BufferedImage sprite = gc.createCompatibleImage(size, size, Transparency.BITMASK);
        Graphics2D g2 = sprite.createGraphics();

        try {
            g2.setColor(color);
            g2.fillOval(0, 0, size, size); // se dibuja UNA vez
        } finally {
            g2.dispose();
        }
        return sprite;
    }
}
