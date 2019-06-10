package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MenuPane extends VBox {
    public MenuPane() {
        Label bombCountLbl = new Label("Введите количество бомб");
        Label fieldWidthLbl = new Label("Введите ширину поля");
        Label fieldHeightLbl = new Label("Введите высоту поля");
        TextField bombCountFld = new TextField();
        TextField fieldWidthFld = new TextField();
        TextField fieldHeightFld = new TextField();
        Button startBtn = new Button("Начать");
        getChildren().addAll(
                bombCountLbl,
                bombCountFld,
                fieldWidthLbl,
                fieldWidthFld,
                fieldHeightLbl,
                fieldHeightFld,
                startBtn
        );

        startBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            int sx = Integer.parseInt(fieldWidthFld.getText());
            int sy = Integer.parseInt(fieldHeightFld.getText());
            int n = Integer.parseInt(bombCountFld.getText());
            getScene().setRoot(new GamePane(sx, sy, n));
        });
    }
}
