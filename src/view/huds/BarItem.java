package view.huds;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class BarItem extends Item {
    final int barWidth;

    BarItem(String label, int maxLenLabel, int barWidth) {
        super(maxLenLabel, label);
        this.barWidth = barWidth;
    }

    @Override
    public void draw(Graphics2D g, FontMetrics fm, int posY, int posX, Object value) {

        if (!(value instanceof Double)) {
            throw new IllegalArgumentException("BarItem '" + label + "' expects Double");
        }
        double progress = (Double) value;

        g.drawString(this.getPaddedLabel(), posX, posY);

        // Calculate bar position (after label)
        final int barX = posX + fm.stringWidth(this.getPaddedLabel());
        final int barY = posY - (int) (fm.getFont().getSize() * 0.5); // Align with text baseline
        final int barHeight = (int) (fm.getFont().getSize() * 0.5);

        // Border
        final int arc = barHeight / 2; // rounded if you want, or set 0 for square
        g.drawRoundRect(barX, barY, barWidth, barHeight, arc, arc);

        // Progess bar
        progress = Math.max(0.0, Math.min(1.0, progress));
        int fillWidth = (int) ((barWidth - 2) * progress);
        float hue = (float) (0.33 * progress); // Hue: 0.0 = red, ~0.33 = green
        Color baseColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        Color fillColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 90);

        Color oldColor = g.getColor();
        g.setColor(fillColor);
        g.fillRoundRect(barX + 1, barY + 1, fillWidth, barHeight - 2, arc, arc);

        // Draw percentage text
        g.setColor(oldColor);
        String percentText = String.format("%d%%", (int) (progress * 100));
        int textX = barX + barWidth + 15;
        g.drawString(percentText, textX, posY);

        g.setColor(oldColor);
    }

}