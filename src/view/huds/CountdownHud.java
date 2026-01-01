package view.huds;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Example HUD demonstrating the use of countdown timers.
 * This class shows how to use the drawCountdown method for weapon reload timers.
 */
public class CountdownHud extends Hud {
    public CountdownHud() {
        super(Color.GRAY, 10, 12, 35);
        
        // Set maxLenLabel to ensure proper spacing for labels
        this.maxLenLabel = 10; // Length of "Weapon 2" + 1
    }
    
    /**
     * Draws the HUD with countdown timers for weapon reloads.
     * 
     * @param g Graphics2D context
     * @param weapon1Reload Remaining reload time for weapon 1 (seconds)
     * @param weapon1Total Total reload time for weapon 1 (seconds)
     * @param weapon2Reload Remaining reload time for weapon 2 (seconds)
     * @param weapon2Total Total reload time for weapon 2 (seconds)
     */
    public void drawWeaponReloads(Graphics2D g, double weapon1Reload, double weapon1Total, 
                                   double weapon2Reload, double weapon2Total) {
        // Set font
        g.setFont(this.font);
        
        // Draw countdown timers with graphical representation
        this.drawCountdown(g, 1, "Weapon 1", weapon1Reload, weapon1Total, true);
        this.drawCountdown(g, 2, "Weapon 2", weapon2Reload, weapon2Total, true);
    }
    
    /**
     * Draws the HUD with text-only countdown timers.
     * 
     * @param g Graphics2D context
     * @param weapon1Reload Remaining reload time for weapon 1 (seconds)
     * @param weapon2Reload Remaining reload time for weapon 2 (seconds)
     */
    public void drawWeaponReloadsTextOnly(Graphics2D g, double weapon1Reload, double weapon2Reload) {
        // Set font
        g.setFont(this.font);
        
        // Draw countdown timers in text mode only
        this.drawCountdown(g, 1, "Weapon 1", weapon1Reload, 0, false);
        this.drawCountdown(g, 2, "Weapon 2", weapon2Reload, 0, false);
    }
}
