package view.huds;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class TextItem extends Item {
    TextItem(int maxLenLabel, String label) {
        super(maxLenLabel, label);
    }

    @Override
    public void draw(Graphics2D g, FontMetrics fm, int posY, int posX, Object value) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("TextItem '" + label + "' expects String");
        }

        final String text = String.format("%s%s", this.paddedLabel, (String) value);
        g.drawString(text, posX, posY);
    }
}
