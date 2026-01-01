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
    public Font font = new Font("Courier New", Font.PLAIN, 28); // tamaño 24

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


    /**
     * Draws a progress bar on a specific row of the HUD.
     * 
     * @param g Graphics2D context to draw on
     * @param row The row number where the progress bar should be drawn
     * @param label The text label for the progress bar
     * @param progress The progress value (0.0 to 1.0, where 1.0 is 100%)
     * @param barWidth The width of the progress bar in pixels
     */
    public void drawProgressBar(Graphics2D g, int row, String label, double progress, int barWidth) {
        // Clamp progress between 0.0 and 1.0
        progress = Math.max(0.0, Math.min(1.0, progress));
        
        Color oldColor = g.getColor();
        
        // Draw label
        g.setFont(this.font);
        g.setColor(this.color);
        String labelText = String.format("%-" + (this.maxLenLabel) + "s", label);
        int yPos = this.initRow + (this.interline * row);
        g.drawString(labelText, this.initCol, yPos);
        
        // Calculate bar position (after label)
        int barX = this.initCol + g.getFontMetrics(this.font).stringWidth(labelText);
        int barY = yPos - (int)(this.font.getSize() * 0.7); // Align with text baseline
        int barHeight = (int)(this.font.getSize() * 0.8);
        
        // Draw bar background (outline)
        g.setColor(Color.DARK_GRAY);
        g.drawRect(barX, barY, barWidth, barHeight);
        
        // Fill background
        g.setColor(new Color(40, 40, 40));
        g.fillRect(barX + 1, barY + 1, barWidth - 2, barHeight - 2);
        
        // Draw filled portion based on progress
        if (progress > 0) {
            int fillWidth = (int)((barWidth - 2) * progress);
            // Color gradient based on progress: red -> yellow -> green
            Color fillColor;
            if (progress < 0.33) {
                fillColor = Color.RED;
            } else if (progress < 0.66) {
                fillColor = Color.YELLOW;
            } else {
                fillColor = Color.GREEN;
            }
            g.setColor(fillColor);
            g.fillRect(barX + 1, barY + 1, fillWidth, barHeight - 2);
        }
        
        // Draw percentage text
        String percentText = String.format("%d%%", (int)(progress * 100));
        g.setColor(Color.WHITE);
        int textX = barX + barWidth + 5;
        g.drawString(percentText, textX, yPos);
        
        g.setColor(oldColor);
    }

    /**
     * Draws a countdown timer on a specific row of the HUD.
     * Shows remaining time in seconds with optional graphical representation.
     * 
     * @param g Graphics2D context to draw on
     * @param row The row number where the countdown should be drawn
     * @param label The text label for the countdown
     * @param remainingSeconds The remaining time in seconds
     * @param totalSeconds The total countdown duration in seconds (used for graphical representation)
     * @param graphical If true, shows a visual bar; if false, shows only text
     */
    public void drawCountdown(Graphics2D g, int row, String label, double remainingSeconds, double totalSeconds, boolean graphical) {
        // Clamp remaining seconds to valid range
        remainingSeconds = Math.max(0.0, remainingSeconds);
        
        Color oldColor = g.getColor();
        
        // Draw label
        g.setFont(this.font);
        g.setColor(this.color);
        String labelText = String.format("%-" + (this.maxLenLabel) + "s", label);
        int yPos = this.initRow + (this.interline * row);
        g.drawString(labelText, this.initCol, yPos);
        
        // Format time as MM:SS or SS.S depending on duration
        String timeText;
        if (remainingSeconds >= 60) {
            int minutes = (int)(remainingSeconds / 60);
            int seconds = (int)(remainingSeconds % 60);
            timeText = String.format("%d:%02d", minutes, seconds);
        } else {
            timeText = String.format("%.1fs", remainingSeconds);
        }
        
        int textStartX = this.initCol + g.getFontMetrics(this.font).stringWidth(labelText);
        
        if (graphical && totalSeconds > 0) {
            // Draw graphical countdown bar
            int barWidth = 150;
            int barX = textStartX;
            int barY = yPos - (int)(this.font.getSize() * 0.7);
            int barHeight = (int)(this.font.getSize() * 0.8);
            
            // Calculate remaining time ratio (1.0 = full time, 0.0 = no time)
            double timeRatio = Math.min(1.0, remainingSeconds / totalSeconds);
            
            // Draw bar outline
            g.setColor(Color.DARK_GRAY);
            g.drawRect(barX, barY, barWidth, barHeight);
            
            // Fill background
            g.setColor(new Color(40, 40, 40));
            g.fillRect(barX + 1, barY + 1, barWidth - 2, barHeight - 2);
            
            // Draw remaining time portion
            if (timeRatio > 0) {
                int fillWidth = (int)((barWidth - 2) * timeRatio);
                // Color based on urgency: green when plenty of time, red when urgent
                Color fillColor;
                if (timeRatio > 0.66) {
                    fillColor = Color.GREEN;  // Plenty of time remaining
                } else if (timeRatio > 0.33) {
                    fillColor = Color.YELLOW; // Getting low
                } else {
                    fillColor = Color.RED;    // Urgent!
                }
                g.setColor(fillColor);
                g.fillRect(barX + 1, barY + 1, fillWidth, barHeight - 2);
            }
            
            // Draw time text next to bar
            g.setColor(Color.WHITE);
            int timeX = barX + barWidth + 5;
            g.drawString(timeText, timeX, yPos);
        } else {
            // Draw text-only countdown
            g.setColor(Color.WHITE);
            g.drawString(timeText, textStartX, yPos);
        }
        
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