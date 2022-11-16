import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public class Button {

    public Color backgroundColor;
    public PVector pos;
    public PVector dim;

    public Color textColor;
    public float textSize;
    public String text;

    public Button(Color backgroundColor, PVector pos, PVector dim, Color textColor, float textSize, String text) {
        this.backgroundColor = backgroundColor;
        this.pos = pos;
        this.dim = dim;
        this.textColor = textColor;
        this.textSize = textSize;
        this.text = text;
    }

    public void draw(PApplet p) {
        p.push();
        p.fill(backgroundColor.getRGB());
        p.rect(pos.x, pos.y, dim.x, dim.y);
        p.fill(textColor.getRGB());
        p.textSize(textSize);
        p.textAlign(p.CENTER, p.CENTER);
        p.text(text, pos.x + dim.x / 2, pos.y+dim.y/2 - textSize/8);
        p.pop();
    }

    public boolean checkClick(PApplet p) {
        return (p.mouseX >= pos.x && p.mouseY >= pos.y) && (p.mouseX <= pos.x + dim.x && p.mouseY <= pos.y + dim.y);
    }

}
