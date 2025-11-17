package _images;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


public class SpriteCache {

    private final GraphicsConfiguration gc;
    private final Map<SpriteKeyDTO, BufferedImage> cache = new HashMap<>(2048);


    public SpriteCache(GraphicsConfiguration gc) {
        this.gc = gc;
    }


    public BufferedImage getSprite(double angle, Color color, int imageId, int size) {
        SpriteKeyDTO key = new SpriteKeyDTO(angle, color, imageId, size);
        BufferedImage img = cache.get(key);
        
        if (img == null) {
            img = createSprite(angle, color, imageId, size);
            cache.put(key, img);
        }
        return img;
    }


    private BufferedImage createSprite(double angle, Color color, int imageId, int size) {
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
