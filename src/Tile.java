import processing.core.PApplet;

import java.awt.*;

public class Tile {

    public Piece piece;

    public Color color;

    public int tileSize = 100;

    public void draw(PApplet p, int x, int y) {
        p.push();
        p.noStroke();
        p.fill(color.getRGB());
        p.rect(x, y, tileSize, tileSize);
        p.pop();
        if (piece != null) {
            piece.draw(p, x + tileSize /2, y + tileSize / 2);
        }
    }

    public Tile(Color color) {
        this.color = color;
    }

}
