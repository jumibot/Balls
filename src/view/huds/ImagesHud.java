package view.huds;

import java.awt.Color;

public class ImagesHud extends Hud {
    public ImagesHud() {
        super(Color.GRAY, 10, 12, 35);

        this.addLabels();
    }

    private void addLabels() {
        this.addLine("FPS");
        this.addLine("Draw");
        this.addLine("Cache images");
        this.addLine("Cache hits");
        this.addLine("Entities Alive");
        this.addLine("Entities Dead");
    }
}