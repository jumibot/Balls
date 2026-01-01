package view.huds;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Hud {
    public final int initRow;
    public final int initCol;
    public final int interline;
    public final Color color;
    public Font font = new Font("Monospaced", Font.PLAIN, 28); // tamaño 24

    public int maxLenLabel = 0;

    public ArrayList<String> labels = new ArrayList<>();

    public Hud(Color color, int initRow, int initCol, int interline) {
        this.initRow = initRow;
        this.initCol = initCol;
        this.interline = interline;
        this.color = color;
    }

    public void addLine(String label) {
        this.labels.add(label);
        this.maxLenLabel = Math.max(this.maxLenLabel, label.length()) + 1;
    }

    public void draw(Graphics2D g, String[] data) {
        Color old = g.getColor();

        g.setFont(this.font);
        g.setColor(Color.YELLOW);

        g.setColor(this.color);
        int row = 1;
        for (String label : this.labels) {
            this.drawLine(g, row, label, data[row - 1]);
            row++;
        }
    }

    public void drawProgressBar(Graphics2D g, int row, String label, double progress, int barWidth) {
        Color oldColor = g.getColor();
        g.setColor(this.color);
        g.setFont(this.font);

        String labelText = String.format("%-" + (this.maxLenLabel) + "s", label);
        int posY = this.initRow + (this.interline * row);
        g.drawString(labelText, this.initCol, posY);

        // Calculate bar position (after label)
        final int barX = this.initCol + g.getFontMetrics(this.font).stringWidth(labelText);
        final int barY = posY - (int) (this.font.getSize() * 0.5); // Align with text baseline
        final int barHeight = (int) (this.font.getSize() * 0.5);

        // Border
        final int arc = barHeight / 2; // rounded if you want, or set 0 for square
        g.setColor(this.color);
        g.drawRoundRect(barX, barY, barWidth, barHeight, arc, arc);

        // Progess bar
        progress = Math.max(0.0, Math.min(1.0, progress));
        int fillWidth = (int) ((barWidth - 2) * progress);
        float hue = (float) (0.33 * progress); // Hue: 0.0 = red, ~0.33 = green
        Color baseColor = Color.getHSBColor(hue, 1.0f, 1.0f);
        Color fillColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 90);

        g.setColor(fillColor);
        g.fillRoundRect(barX + 1, barY + 1, fillWidth, barHeight - 2, arc, arc);

        // Draw percentage text
        g.setColor(this.color);
        String percentText = String.format("%d%%", (int) (progress * 100));
        int textX = barX + barWidth + 15;
        g.drawString(percentText, textX, posY);

        g.setColor(oldColor);
    }


    /**
     * PRIVATE METHODS
     */
    private void drawLine(Graphics2D g, int row, String line, String data) {
        String text = String.format("%-" + (this.maxLenLabel) + "s%s", line, data);
        g.drawString(text, this.initCol, this.initRow + (this.interline * row));
    }
}