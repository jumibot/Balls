package view.huds;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Example HUD demonstrating the use of progress bars.
 * This class shows how to use the drawProgressBar method.
 */
public class ProgressBarHud extends Hud {
    public ProgressBarHud() {
        super(Color.GRAY, 10, 2000, 35);

        // Set maxLenLabel to ensure proper spacing for labels
        this.maxLenLabel = 7; // Length of "Shield" + 1
    }

    public void drawProgressBars(Graphics2D g, double healthProgress, double energyProgress, double shieldProgress) {
        // Set font and prepare for drawing
        g.setFont(this.font);

        // Draw progress bars for each stat
        this.drawProgressBar(g, 1, "Health", healthProgress, 200);
        this.drawProgressBar(g, 2, "Energy", energyProgress, 200);
        this.drawProgressBar(g, 3, "Shield", shieldProgress, 200);


    }
}
