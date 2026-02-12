package engine.view.hud.impl;

// region Imports
import java.awt.Color;

import engine.view.hud.core.DataHUD;
// endregion

/**
 * Instrumentation HUD for displaying profiling metrics.
 * 
 * Shows rendering, domain (physics/events), and spatial grid performance
 * metrics
 * as ms/frame normalized values.
 */
public class RenderHUD extends DataHUD {

    // region Constructors
    public RenderHUD() {
        super(
                new Color(0, 200, 100, 255), // Title color (green)
                Color.GRAY, // Highlight color
                new Color(255, 255, 255, 125), // Label color
                new Color(255, 255, 255, 255), // Data color
                425, 12, 15);

        this.addItems();
    }
    // endregion

    // *** PUBLIC ***

    public void draw(java.awt.Graphics2D g, Object... values) {
        super.draw(g, values);
    }

    // *** PRIVATE ***

    private void addItems() {
        this.addTitle("RENDER (ms/frame)");

        // Render timing breakdown
        this.addTextItem("BG Draw");
        this.addTextItem("Translate");
        this.addTextItem("Statics Draw");
        this.addTextItem("Dynamic Draw");
        this.addTextItem("HUD Draw");
        this.addTextItem("Show Frame");
        this.addTextItem("TOTAL Draw");
        this.addTextItem("Update Phase");
        this.addTextItem("TOTAL Frame");

        this.prepareHud();
    }

}
