package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import EnigmaMachineException.MachineNotExistsException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class EncryptDecryptDetailsController {

    @FXML
    private TextField encryptDecryptTextBox;

    @FXML
    private Button encryptDecryptButton;

    @FXML
    private Button ResetMachineStateButton;

    @FXML
    private Label encryptDecryptTextLabel;

    @FXML
    private ScrollPane encryptDecryptWordsScrollPane;

    @FXML
    private TextField encryptedDecryptedWordText;
    private EncryptDecryptGridController encryptDecryptGridController;

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }
    private void setScrollPaneColor(){
        encryptDecryptWordsScrollPane.setStyle("-fx-background-color: transparent;");
    }
    private void onDecryptionButtonClicked(ActionEvent event) throws MachineNotExistsException {
        encryptDecryptGridController.decodeWord(encryptedDecryptedWordText.getText());
    }


    public void setDecodedWord(String decodeWord) {
        encryptedDecryptedWordText.setText(decodeWord);
    }
}
