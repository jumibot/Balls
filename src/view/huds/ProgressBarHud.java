package view.huds;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Example HUD demonstrating the use of progress bars.
 * This class shows how to use the drawProgressBar method.
 */
public class ProgressBarHud extends Hud {
    public ProgressBarHud() {
        super(Color.GRAY, 10, 12, 35);
        
        // Set maxLenLabel to ensure proper spacing for labels
        this.maxLenLabel = 7; // Length of "Shield" + 1
    }
    
    /**
     * Draws the HUD with progress bars instead of simple text.
     * 
     * @param g Graphics2D context
     * @param healthProgress Health value (0.0 to 1.0)
     * @param energyProgress Energy value (0.0 to 1.0)
     * @param shieldProgress Shield value (0.0 to 1.0)
     */
    public void drawWithProgressBars(Graphics2D g, double healthProgress, double energyProgress, double shieldProgress) {
        // Set font and prepare for drawing
        g.setFont(this.font);
        
        // Draw progress bars for each stat
        this.drawProgressBar(g, 1, "Health", healthProgress, 200);
        this.drawProgressBar(g, 2, "Energy", energyProgress, 200);
        this.drawProgressBar(g, 3, "Shield", shieldProgress, 200);
    }
}
