package sample;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GamePane extends Pane {
    private FieldPane fp;
    private int sx;
    private int sy;
    private int n;
    public GamePane(int sx, int sy, int n) {
        this.sx = sx;
        this.sy = sy;
        this.n = n;
        fp = new FieldPane(sx, sy, n);
        getChildren().add(fp);
    }
    public void restart() {
        getChildren().remove(fp);
        fp = new FieldPane(sx, sy, n);
        getChildren().add(fp);
    }
    public void exit() {
        ((Stage) getScene().getWindow()).close();
    }
}
