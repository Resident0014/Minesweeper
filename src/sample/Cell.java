package sample;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Cell extends Polygon {
    public static final Image bomb = new Image(FieldPane.class.getResourceAsStream("/bomb.png"));
    public static final Image flag = new Image(FieldPane.class.getResourceAsStream("/flag.png"));
    public static final int BOMB = 7;
    private boolean visible = false;
    private Node mark = null;
    private Node label = null;
    private int value = 0;
    private double x;
    private double y;
    private double cellSize;

    public Cell(int i, int j, double cellSize) {
        this.cellSize = cellSize;
        this.x = j * cellSize + i % 2 * (cellSize / 2);
        this.y = i * cellSize * 0.75;

        getPoints().addAll(getPolyPoints());
        setFill(Color.grayRgb(192));
        setStroke(Color.BLACK);
    }

    private Double[] getPolyPoints() {
        double xLeft = x;
        double yTop = y;
        double xCenter = xLeft + cellSize / 2;
        double xRight = xLeft + cellSize;
        double yCenter1 = yTop + cellSize * 0.25;
        double yCenter2 = yTop + cellSize * 0.75;
        double yBottom = yTop + cellSize;

        Double[] points = {
                xCenter, yTop,
                xRight, yCenter1,
                xRight, yCenter2,
                xCenter, yBottom,
                xLeft, yCenter2,
                xLeft, yCenter1
        };
        return points;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void open() {
        if (visible) {
            throw new RuntimeException();
        }
        visible = true;
        setFill(Color.WHITE);
    }

    public boolean visible() {
        return visible;
    }

    public Node getLabel() {
        if (label == null) {
            if (value == BOMB) {
                label = new ImageView(bomb);
                y += (cellSize - bomb.getHeight()) / 2 - 1;
                x += (cellSize - bomb.getWidth()) / 2 + 3;
            } else {
                Label lbl = new Label("" + value);
                lbl.setTextFill(Color.BLACK);
                y += (cellSize + lbl.getHeight()) / 2 - 7;
                x += (cellSize + lbl.getWidth()) / 2 - 3;
                lbl.setScaleX(2);
                lbl.setScaleY(2);
                label = lbl;
            }
            label.setTranslateX(x);
            label.setTranslateY(y);
        }
        return label;
    }

    public void setMark() {
        mark = new ImageView(flag);
        mark.setTranslateX(x + (cellSize - flag.getWidth()) / 2);
        mark.setTranslateY(y + (cellSize - flag.getHeight()) / 2);
        mark.setMouseTransparent(true);
    }

    public Node getMark() {
        return mark;
    }

    public boolean empty() {
        return value == 0;
    }

    public boolean bomb() {
        return value == BOMB;
    }

    public void unmark() {
        mark = null;
    }
}
