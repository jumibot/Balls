package view.huds;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

abstract class Item {
    final String label;
    final String paddedLabel;

    Item(int maxLenLabel, String label) {
        this.label = label;
        this.paddedLabel = String.format("%-" + maxLenLabel + "s", label);
    }

    public String getPaddedLabel() {
        return paddedLabel;
    }

    abstract void draw(Graphics2D g, FontMetrics fm, int posY, int posX, Object value);
}