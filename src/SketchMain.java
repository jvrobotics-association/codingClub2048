import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.awt.*;

public class SketchMain extends PApplet {

    public Board board = new Board();
    public Button resetButton = new Button(
            new Color(74, 48, 4), // background color
            new PVector(0, 0), // position
            new PVector(150, 70), // width and height
            new Color(255, 255, 255), // text color
            60f, // font size
            "Reset"); // display text

    public void settings() {
        size(600, 650);
        resetButton.pos = new PVector(width - 160, height - 80);
    }

    public void draw() {
        background(217, 199, 169);
        board.draw(this);
        resetButton.draw(this);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (resetButton.checkClick(this)) {
            board = new Board();
        }
    }

    public void makeMove(Direction dir) {
        board.createCopy();
        board.slidePieces(dir);
        board.compressPieces(dir);
        board.slidePieces(dir);
        board.checkMoveMade();
        board.checkGameOver(this);
    }

    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP) {
                makeMove(Direction.UP);
            } else if (keyCode == DOWN) {
                makeMove(Direction.DOWN);
            } else if (keyCode == RIGHT) {
                makeMove(Direction.RIGHT);
            } else if (keyCode == LEFT) {
                makeMove(Direction.LEFT);
            }
        } else if (keyCode == 69){
            board.gameOver(this);
        }
    }
}
