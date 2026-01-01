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
    public final Graphics2D g;
    public Font font = new Font("Arial", Font.PLAIN, 28); // tamaño 24

    public int maxLenLabel = 0;

    public ArrayList<String> labels = new ArrayList<>();

    public Hud(Graphics2D g, Color color, int initRow, int initCol, int interline) {
        this.initRow = initRow;
        this.initCol = initCol;
        this.interline = interline;
        this.color = color;
        this.g = g;
    }

    public void addLine(String label) {
        this.labels.add(label);
        this.maxLenLabel = Math.max(this.maxLenLabel, label.length());
    }

    public void drawHUD(String[] data) {
        Color old = g.getColor();

        g.setFont(this.font);
        g.setColor(Color.YELLOW);

        g.setColor(this.color);
        int row = 1;
        for (String label : this.labels) {
            this.drawHUDLine(row, label, data[row - 1]);
        }
    }

    /**
     * PRIVATE METHODS
     */
    private void drawHUDLine(int row, String line, String data) {
        String text = String.format("%-" + this.maxLenLabel + "s%s", line, data);
        g.drawString(text, this.initCol, this.initRow + (this.interline * row));
    }

}