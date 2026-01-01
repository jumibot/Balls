package view.huds;

import java.awt.Graphics2D;

public class ImagesHud extends Hud {
    public ImagesHud(Graphics2D g) {
        super(g, null, 65, 12, 35);

        this.addLabels();
    }

    private void addLabels() {
        this.addLine("FPS:");
        this.addLine("Cache images:");
        this.addLine("Cache hits:");
        this.addLine("Entities Alive:");
        this.addLine("Entities Dead:");
    }
}
