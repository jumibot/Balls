package engine.view.hud.impl;

import java.awt.Color;

import engine.view.hud.core.DataHUD;

/**
 * Instrumentation HUD for displaying profiling metrics.
 * 
 * Shows rendering, domain (physics/events), and spatial grid performance metrics
 * as ms/frame normalized values.
 */
public class RenderHUD extends DataHUD {

    // region Constructors
    public RenderHUD() {
        super(
                new Color(0, 200, 100, 255), // Title color (green)
                Color.GRAY, // Highlight color
                new Color(255, 255, 255, 150), // Label color
                new Color(255, 255, 255, 255), // Data color
                400, 12, 15);

        this.addItems();
    }
    // endregion

    private void addItems() {
        this.addTitle("RENDER (ms/frame)");
        
        // Render timing breakdown
        this.addTextItem("Bg Draw");
        this.addTextItem("Statics Draw");
        this.addTextItem("Query Dynamic");
        this.addTextItem("Dynamic Draw");
        this.addTextItem("Total Dynamic");
        this.addTextItem("HUD Draw");
        this.addTextItem("Total Draw");
        this.addTextItem("Update Phase");
        this.addTextItem("Full Frame");
        
        this.prepareHud();
    }

    public void draw(java.awt.Graphics2D g, Object... values) {
        super.draw(g, values);
    }
}

