package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import EnigmaMachineException.MachineNotExistsException;
import EnigmaMachineException.SettingsNotInitializedException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;


public class EncryptDecryptDetailsController {

    @FXML private TextField encryptDecryptTextBox;
    @FXML private Button encryptDecryptButton;
    @FXML private Button ResetMachineStateButton;
    @FXML private Label encryptDecryptTextLabel;
    @FXML private ScrollPane encryptDecryptWordsScrollPane;
    @FXML private Button fullWordButton;
    @FXML private Button letterByLetterButton;
    @FXML private TextField decodedWordsTextArea;
    @FXML private Label encryptDecryptTextLabel1;
    private boolean fullWordDecoding = true;
    private EncryptDecryptGridController encryptDecryptGridController;

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }

    @FXML public void initialize() {
        encryptDecryptTextBox.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!fullWordDecoding && newValue != null && newValue.length() > 0) {
                    encryptDecryptGridController.decodeWord(encryptDecryptTextBox.getText());
                    clearDecodingTextArea();
                }
            }
        });
    }
    private void setScrollPaneColor(){
        encryptDecryptWordsScrollPane.setStyle("-fx-background-color: transparent;");
    }
    @FXML void onDecryptionButtonClicked(ActionEvent event) throws MachineNotExistsException, CloneNotSupportedException, SettingsNotInitializedException {
            if(fullWordDecoding) {
                encryptDecryptGridController.decodeWord(encryptDecryptTextBox.getText());
                encryptDecryptTextBox.clear();
            }
            else{
                //TODO: chen - add statistics change only when pushes button.
                decodedWordsTextArea.clear();
            }
            encryptDecryptGridController.onFinishInput();
            encryptDecryptTextBox.clear();
}
    @FXML void onLetterByLetterButtonPressed(ActionEvent event) {
        encryptDecryptGridController.setKeyboardButtonsEnabled();
        encryptDecryptButton.setText("Done");
        encryptDecryptTextBox.clear();
        decodedWordsTextArea.clear();
        fullWordDecoding = false;
    }
    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) {
        encryptDecryptGridController.resetMachineState();
    }
    @FXML private void onFullWordButtonPressed(ActionEvent event){
        encryptDecryptGridController.setKeyboardButtonsDisabled();
        encryptDecryptButton.setText("Process");
        encryptDecryptTextBox.clear();
        decodedWordsTextArea.clear();
        fullWordDecoding = true;
    }


    public void setDecodedWord(String decodeWord) {
        if(fullWordDecoding) {
            decodedWordsTextArea.setText(decodeWord);
        }
        else{
            decodedWordsTextArea.setText(decodedWordsTextArea.getText() + decodeWord);
        }
    }

    public void clearDecodingTextArea() {
        encryptDecryptTextBox.clear();
    }
}
