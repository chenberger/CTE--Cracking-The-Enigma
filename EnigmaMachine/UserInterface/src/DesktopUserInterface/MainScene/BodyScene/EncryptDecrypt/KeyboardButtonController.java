package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
    private SimpleStringProperty buttonClickedProperty;

    public KeyboardButtonController() {
        buttonClickedProperty = new SimpleStringProperty("");
    }


    @FXML void OnKeyboardButtonClicked(ActionEvent event) {
        encryptDecryptGridController.keyBoardButtonClicked(keyboardCharacter);
        buttonClickedProperty.set(keyboardButton.getText());
    }

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }

    public void setKeyboardCharacter(Character keyboardCharacter) {
        this.keyboardCharacter = keyboardCharacter;
        keyboardButton.setDisable(true);
        keyboardButton.setText(keyboardCharacter.toString());
    }

    public void turnOnBulbButton() {
        Circle circle = new Circle(keyboardButton.getLayoutX(), keyboardButton.getLayoutY(), keyboardButton.getWidth() / 2);
        FillTransition fillTransition = new FillTransition(Duration.millis(3000), circle, Color.KHAKI, (Color)keyboardButton.getBackground().getFills().get(0).getFill());
        fillTransition.setCycleCount(1);

        fillTransition.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                keyboardButton.setBackground(new Background(new BackgroundFill(circle.getFill(), new CornerRadii(keyboardButton.getWidth()), Insets.EMPTY)));
                return t;
            }
        });

        fillTransition.play();
    }

    public void setDisableBind(SimpleBooleanProperty fullWordDecodingProperty) {
        keyboardButton.disableProperty().bind(fullWordDecodingProperty);
    }

    public SimpleStringProperty getButtonClickedProperty() {
        return buttonClickedProperty;
    }
}
