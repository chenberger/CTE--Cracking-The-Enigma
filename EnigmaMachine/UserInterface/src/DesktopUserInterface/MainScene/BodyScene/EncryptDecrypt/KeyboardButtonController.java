package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class KeyboardButtonController {

    @FXML private Button keyboardButton;
    private EncryptDecryptGridController encryptDecryptGridController;
    private Character keyboardCharacter;

    @FXML void OnKeyboardButtonClicked(ActionEvent event) {
        encryptDecryptGridController.keyBoardButtonClicked(keyboardCharacter);
    }

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }

    public void setKeyboardCharacter(Character keyboardCharacter) {
        this.keyboardCharacter = keyboardCharacter;
        keyboardButton.setText(keyboardCharacter.toString());
    }

    public void turnOnBulbButton() {
        Circle circle = new Circle();
        FillTransition fillTransition = new FillTransition(Duration.millis(3000), circle, Color.KHAKI, Color.BLACK);
        fillTransition.setCycleCount(1);

        fillTransition.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                keyboardButton.setBackground(new Background(new BackgroundFill(circle.getFill(), CornerRadii.EMPTY, Insets.EMPTY)));
                return t;
            }
        });

        fillTransition.play();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add("KeyboardButton.css");
        keyboardButton.setId("key-button");
    }

}
