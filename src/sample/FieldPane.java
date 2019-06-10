package sample;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.Random;

public class FieldPane extends Pane {
    private static final int[][] neighborPoints = {
            {-1, -1},
            {-1, 0},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
    };

    private int fieldSizeX;
    private int fieldSizeY;
    private int countMines;
    private double cellSize;
    private Cell[][] field;
    private boolean gameOver = false;
    private int cellsMined = 0;
    private int cellsLeft;

    public FieldPane(int sx, int sy, int n) {
        fieldSizeX = sx;
        fieldSizeY = sy;
        countMines = n;

        cellSize = Math.min(
                Main.windowWidth / (fieldSizeX + 0.5),
                (Main.windowHeight - 150) / (fieldSizeY * 0.75)
        );
        field = new Cell[sy][sx];
        cellsLeft = sy * sx;

        addCells();
        setMines();
    }

    private void addCells() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = new Cell(i, j, cellSize);
                int ii = i;
                int jj = j;
                field[i][j].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (!gameOver) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            open(ii, jj);
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {
                            mark(ii, jj);
                        }
                    }
                });

                getChildren().add(field[i][j]);
            }
        }
    }

    private void unmark(int i, int j) {
        Node mark = field[i][j].getMark();
        if (mark != null) {
            getChildren().remove(mark);
            field[i][j].unmark();
        }
    }

    private void open(int i, int j) {
        try {
            field[i][j].open();
        }
        catch (Exception e) {
            return;
        }

        unmark(i, j);

        cellsLeft--;
        getChildren().add(field[i][j].getLabel());
        String text = null;
        if (cellsLeft == cellsMined) {
            gameOver = true;
            text = "YOU WIN!";
        }
        else if (field[i][j].empty()) {
            for (int[] offset : neighborPoints) {
                int rightOffset = 0;
                if (offset[0] != 0) {
                    rightOffset = i % 2;
                }
                open(i + offset[0], j + offset[1] + rightOffset);
            }
        }
        else if (field[i][j].bomb()) {
            gameOver = true;
            text = "GAME OVER!";
        }

        if (gameOver && cellsLeft > 0) {
            gameOver(text);
        }
    }

    private void gameOver(String text) {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (!field[i][j].visible()) {
                    field[i][j].open();
                    unmark(i, j);
                    getChildren().add(field[i][j].getLabel());
                    cellsLeft--;
                }
            }
        }

        Label label = new Label(text);
        label.setTranslateX((Main.windowWidth - label.getWidth()) / 2 - 35);
        label.setTranslateY(Main.windowHeight - 100);
        label.setScaleX(3);
        label.setScaleY(3);
        label.setTextFill(Color.RED);
        getChildren().add(label);

        Button repeatBtn = new Button("Повторить");
        repeatBtn.setTranslateX(Main.windowWidth / 2 - 100);
        repeatBtn.setTranslateY(Main.windowHeight - 50);
        repeatBtn.setScaleX(2);
        repeatBtn.setScaleY(2);
        repeatBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            ((GamePane) getParent()).restart();
        });
        getChildren().add(repeatBtn);

        Button exitBtn = new Button("Выйти");
        exitBtn.setTranslateX(Main.windowWidth / 2 + 50);
        exitBtn.setTranslateY(Main.windowHeight - 50);
        exitBtn.setScaleX(2);
        exitBtn.setScaleY(2);
        exitBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            ((GamePane) getParent()).exit();
        });
        getChildren().add(exitBtn);
    }

    private void mark(int i, int j) {
        Node mark = field[i][j].getMark();
        if (mark == null) {
            if (!field[i][j].visible()) {
                field[i][j].setMark();
                getChildren().add(field[i][j].getMark());
            }
        }
        else {
            unmark(i, j);
        }
    }

    private void setMines() {
        Random random = new Random();
        for (int i = 0; i < countMines; i++) {
            int y = random.nextInt(fieldSizeY);
            int x = random.nextInt(fieldSizeX);
            field[y][x].setValue(Cell.BOMB);
        }
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (!field[i][j].bomb()) {
                    int bombsAround = 0;
                    for (int[] offset : neighborPoints) {
                        try {
                            int rightOffset = 0;
                            if (offset[0] != 0) {
                                rightOffset = i % 2;
                            }
                            if (field[i + offset[0]][j + offset[1] + rightOffset].bomb()) {
                                bombsAround++;
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {}
                    }
                    field[i][j].setValue(bombsAround);
                }
                else {
                    cellsMined++;
                }
            }
        }
    }
}
