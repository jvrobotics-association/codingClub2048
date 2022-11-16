import processing.core.PApplet;

public class Piece {
    public int value = 2;

    public Piece() {}

     public Piece add(Piece other) {
        this.value += other.value;
        return this;
    }

    public void draw(PApplet p, int x, int y) {
        p.push();
        p.noStroke();
        p.fill(255, 255, 255);
        p.textAlign(p.CENTER, p.CENTER);
        float numberOfDigits = String.valueOf(value).length();
        p.textSize((70f/5)/((numberOfDigits +2)/ 5)+30);
        p.text(value, x, y);
        p.pop();
    }
}
