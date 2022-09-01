package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
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
        FillTransition fillTransition = new FillTransition(Duration.millis(30000), keyboardButton.getShape(), Color.YELLOW, Color.RED);
        fillTransition.play();
    }

}
