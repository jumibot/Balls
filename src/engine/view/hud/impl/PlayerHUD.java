package engine.view.hud.impl;

import java.awt.Color;

import engine.view.hud.core.DataHUD;

public class PlayerHUD extends DataHUD {
    public PlayerHUD() {
        super(
                new Color(255, 140, 0, 255), // Title color
                Color.GRAY, // Highlight color
                new Color(255, 255, 255, 150), // Label color
                new Color(255, 255, 255, 255), // Data color
                20, 12, 15);

        this.addItems();
    }

    private void addItems() {
        int barWidth = 40;

        this.addTitle("PLAYER STATUS");
        this.addSkipValue(); // Entity ID
        this.addSkipValue(); // Player name
        this.addBarItem("Damage", barWidth, false);
        this.addBarItem("Energy", barWidth, false);
        this.addBarItem("Shield", barWidth, false);
        this.addTextItem("Temp ºC");
        this.addTitle("Weapons");
        this.addSkipValue(); // Active weapon
        this.addBarItem("Guns", barWidth, false);
        this.addBarItem("Burst", barWidth, false);
        this.addBarItem("Mines", barWidth, false);
        this.addBarItem("Missiles", barWidth, false);
        this.prepareHud();
    }
}
