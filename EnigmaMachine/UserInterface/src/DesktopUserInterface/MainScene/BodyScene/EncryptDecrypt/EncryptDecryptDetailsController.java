package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import EnigmaMachineException.MachineNotExistsException;
import EnigmaMachineException.SettingsNotInitializedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;


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
    @FXML private Button fullWordButton;
    @FXML private Button letterByLetterButton;

    @FXML
    private TextField encryptedDecryptedWordText;
    @FXML
    private Label encryptDecryptTextLabel1;
    private boolean fullWordDecoding = true;
    private EncryptDecryptGridController encryptDecryptGridController;


    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }
    private void setScrollPaneColor(){
        encryptDecryptWordsScrollPane.setStyle("-fx-background-color: transparent;");
    }
    @FXML private void onDecryptionButtonClicked(ActionEvent event) throws MachineNotExistsException, CloneNotSupportedException, SettingsNotInitializedException {
            if(fullWordDecoding) {
                encryptDecryptGridController.decodeWord(encryptDecryptTextBox.getText());
                encryptDecryptTextBox.clear();
            }
            else{
                //TODO: chen - add statistics change only when pushes button.
                encryptedDecryptedWordText.clear();
            }
            encryptDecryptGridController.onFinishInput();
            encryptDecryptTextBox.clear();
}
    @FXML
    private void onLetterByLetterButtonPressed(ActionEvent event) {
        encryptDecryptButton.setText("Done");
        encryptDecryptTextBox.clear();
        encryptedDecryptedWordText.clear();
        fullWordDecoding = false;
    }
    @FXML private void onLetterEnteredToBox(InputMethodEvent event) {
        if(!fullWordDecoding) {
            encryptDecryptGridController.decodeWord(encryptDecryptTextBox.getText());
            encryptDecryptTextBox.clear();
        }
    }

    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) {
        encryptDecryptGridController.resetMachineState();
    }
    @FXML private void onFullWordButtonPressed(ActionEvent event){
        encryptDecryptButton.setText("Process");
        encryptDecryptTextBox.clear();
        encryptedDecryptedWordText.clear();
        fullWordDecoding = true;
    }


    public void setDecodedWord(String decodeWord) {
        if(fullWordDecoding) {
            encryptedDecryptedWordText.setText(decodeWord);
        }
        else{
            encryptedDecryptedWordText.setText(encryptedDecryptedWordText.getText() + decodeWord);
        }
    }
}
