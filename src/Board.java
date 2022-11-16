import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board {

    public List<List<Tile>> tiles = new ArrayList<>();

    public List<List<Tile>> copyTiles = new ArrayList<>();

    public int score = 0;

    public Color tileColor = new Color(74, 48, 4);

    public Random random = new Random();

    public boolean isGameOver = false;

    public int displayStepSize = 150;

    public void draw(PApplet p) {
        // draws all the tiles
        for (int y = 0; y < tiles.size(); y++) {
            for (int x = 0; x < tiles.get(y).size(); x++) {
                Tile tile = tiles.get(y).get(x);
                tile.draw(p, x * displayStepSize, y * displayStepSize);
            }
        }
        p.push();
        if (isGameOver) {
            p.push();
            p.fill(74, 48, 4, 255 * 0.8f);
            p.rect(0, 0, p.width, p.height);
            p.fill(255, 255, 255);
            p.textSize(80f);
            p.textAlign(p.CENTER, p.CENTER);
            p.text("GAME OVER", p.width / 2f, p.height / 2f-35);
            p.text(loadHighscore(p), p.width / 2f, p.height / 2f+35);
            p.pop();
        }
        p.noStroke();
        p.fill(isGameOver ? 255 : 0);
        p.text("Score: " + String.valueOf(score), 5, p.height - 5);
        if (!isGameOver) {
            p.text(loadHighscore(p), 5, p.height - 55);
        }
        p.pop();
    }

    public String loadHighscore(PApplet p) {
        return p.loadStrings("data.txt")[0];
    }

    public void saveHighscore(PApplet p, int score) {
        p.saveStrings("data.txt", new String[]{"Highscore: " + String.valueOf(score)});
    }

    public Board() {
        // creates all the tiles
        for (int y = 0; y < 4; y++) {
            tiles.add(new ArrayList<>());
            copyTiles.add(new ArrayList<>());
            for (int x = 0; x < 4; x++) {
                tiles.get(y).add(new Tile(tileColor));
                copyTiles.get(y).add(new Tile(tileColor));
            }
        }

        // create two pieces on the board
        createPieceOnBoard();
        createPieceOnBoard();

    }

    public void checkGameOver(PApplet p) {
        for (int y = 0; y < tiles.size(); y++) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                if (tiles.get(y).get(x).piece == null) {
                    return; // not game over
                }
            }
        }
        // if got to here, all tiles are filled
        for (int y = 0; y < tiles.size(); y++) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                if (y != tiles.size() - 1) {
                    Piece current = tiles.get(y).get(x).piece;
                    Piece neighbor = tiles.get(y + 1).get(x).piece;
                    if (current.value == neighbor.value) {
                        return; // not game over
                    }
                }
                if (x != tiles.get(0).size() - 1) {
                    Piece current = tiles.get(y).get(x).piece;
                    Piece neighbor = tiles.get(y).get(x + 1).piece;
                    if (current.value == neighbor.value) {
                        return; // not game over
                    }
                }
            }
        }
        // if made it this far, game is over
        gameOver(p);
    }

    public void gameOver(PApplet p) {
        isGameOver = true;
        int highscore = Integer.parseInt(loadHighscore(p).split(" ")[1]);
        if (highscore < score) {
            saveHighscore(p, score);
        }
    }

    public void createCopy() {
        for (int y = 0; y < tiles.size(); y++) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                Tile tile = tiles.get(y).get(x);
                copyTiles.get(y).set(x, new Tile(tile.color));
                Piece piece = new Piece();
                if (tile.piece != null) {
                    piece.value = tile.piece.value;
                    copyTiles.get(y).get(x).piece = piece;
                }
            }
        }
    }

    public void checkMoveMade() {
        for (int y = 0; y < tiles.size(); y++) {
            for (int x = 0; x < tiles.get(y).size(); x++) {
                Piece current = tiles.get(y).get(x).piece;
                Piece copy = copyTiles.get(y).get(x).piece;

                if (current == null && copy == null) {
                    // do nothing
                } else if (current == null || copy == null) {
                    createPieceOnBoard();
                    return;
                } else if (current.value != copy.value) {
                    createPieceOnBoard();
                    return;
                }
            }
        }
    }

    public void createPieceOnBoard() {
        // find empty tiles
        List<Tile> emptyTiles = new ArrayList<>();
        for (int y = 0; y < tiles.size(); y++) {
            for (int x = 0; x < tiles.get(y).size(); x++) {
                Tile tile = tiles.get(y).get(x);
                if (tile.piece == null) {
                    emptyTiles.add(tile);
                }
            }
        }

        // select random empty tile
        int randomIndex = random.nextInt(emptyTiles.size());
        Tile selectedTile = emptyTiles.get(randomIndex);

        // create new piece
        selectedTile.piece = new Piece();
        if (random.nextInt(10) == 0) {
            selectedTile.piece.value = 4;
        }
    }

    public void slidePieces(Direction dir) {
        if (dir == Direction.DOWN) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                List<Tile> column = getColumn(x);

                List<Piece> pieces = new ArrayList<>();

                for (Tile tile : column) {
                    if (tile.piece != null) {
                        pieces.add(tile.piece);
                    }
                }

                // place pieces back in reverse order from bottom
                for (int y = tiles.size() - 1; y >= 0; y--) {
                    if (pieces.size() != 0) {
                        tiles.get(y).get(x).piece = pieces.get(pieces.size() - 1);
                        pieces.remove(pieces.size() - 1);
                    } else {
                        tiles.get(y).get(x).piece = null;
                    }
                }
            }
        } else if (dir == Direction.UP) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                List<Tile> column = getColumn(x);

                List<Piece> pieces = new ArrayList<>();

                for (Tile tile : column) {
                    if (tile.piece != null) {
                        pieces.add(tile.piece);
                    }
                }

                // place pieces back in order from top
                for (int y = 0; y < tiles.size(); y++) {
                    if (pieces.size() != 0) {
                        tiles.get(y).get(x).piece = pieces.get(0);
                        pieces.remove(0);
                    } else {
                        tiles.get(y).get(x).piece = null;
                    }
                }
            }
        } else if (dir == Direction.RIGHT) {
            for (int y = 0; y < tiles.size(); y++) {
                List<Tile> row = tiles.get(y);

                List<Piece> pieces = new ArrayList<>();

                for (Tile tile : row) {
                    if (tile.piece != null) {
                        pieces.add(tile.piece);
                    }
                }

                // place pieces back in order from top
                for (int x = tiles.get(y).size() - 1; x >= 0; x--) {
                    if (pieces.size() != 0) {
                        tiles.get(y).get(x).piece = pieces.get(pieces.size() - 1);
                        pieces.remove(pieces.size() - 1);
                    } else {
                        tiles.get(y).get(x).piece = null;
                    }
                }
            }
        } else if (dir == Direction.LEFT) {
            for (int y = 0; y < tiles.size(); y++) {
                List<Tile> row = tiles.get(y);

                List<Piece> pieces = new ArrayList<>();

                for (Tile tile : row) {
                    if (tile.piece != null) {
                        pieces.add(tile.piece);
                    }
                }

                // place pieces back in order from top
                for (int x = 0; x <= tiles.get(y).size() - 1; x++) {
                    if (pieces.size() != 0) {
                        tiles.get(y).get(x).piece = pieces.get(0);
                        pieces.remove(0);
                    } else {
                        tiles.get(y).get(x).piece = null;
                    }
                }
            }
        }
    }

    public void compressPieces(Direction dir) {
        if (dir == Direction.DOWN) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                List<Tile> column = getColumn(x);

                for (int i = column.size() - 2; i >= 0; i--) {
                    Piece current = column.get(i).piece;
                    Piece next = column.get(i + 1).piece;
                    if (current != null && next != null) {
                        if (current.value == next.value) {
                            current.add(next);
                            score += current.value;
                            tiles.get(i + 1).get(x).piece = null;
                        }
                    }
                }
            }
        } else if (dir == Direction.UP) {
            for (int x = 0; x < tiles.get(0).size(); x++) {
                List<Tile> column = getColumn(x);

                for (int i = 1; i <= column.size() - 1; i++) {
                    Piece current = column.get(i).piece;
                    Piece next = column.get(i - 1).piece;
                    if (current != null && next != null) {
                        if (current.value == next.value) {
                            current.add(next);
                            score += current.value;
                            tiles.get(i - 1).get(x).piece = null;
                        }
                    }
                }
            }
        } else if (dir == Direction.RIGHT) {
            for (int y = 0; y < tiles.size(); y++) {
                List<Tile> row = tiles.get(y);

                for (int i = row.size() - 2; i >= 0; i--) {
                    Piece current = row.get(i).piece;
                    Piece next = row.get(i + 1).piece;
                    if (current != null && next != null) {
                        if (current.value == next.value) {
                            current.add(next);
                            score += current.value;
                            tiles.get(y).get(i + 1).piece = null;
                        }
                    }
                }
            }
        } else if (dir == Direction.LEFT) {
            for (int y = 0; y < tiles.size(); y++) {
                List<Tile> row = tiles.get(y);

                for (int i = 1; i <= row.size() - 1; i++) {
                    Piece current = row.get(i).piece;
                    Piece next = row.get(i - 1).piece;
                    if (current != null && next != null) {
                        if (current.value == next.value) {
                            current.add(next);
                            score += current.value;
                            tiles.get(y).get(i - 1).piece = null;
                        }
                    }
                }
            }
        }
    }

    public List<Tile> getColumn(int x) {
        List<Tile> column = new ArrayList<>();
        for (int y = 0; y < tiles.size(); y++) {
            column.add(tiles.get(y).get(x));
        }
        return column;
    }

}
