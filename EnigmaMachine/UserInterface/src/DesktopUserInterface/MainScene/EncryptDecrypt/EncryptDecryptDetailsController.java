package DesktopUserInterface.MainScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import EnigmaMachineException.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;


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
    @FXML
    private void onDecryptionButtonClicked(ActionEvent event) {
        try {
            String textToDecode = encryptDecryptTextBox.getText();
            if(!textToDecode.isEmpty()){
            encryptDecryptGridController.decodeWord(textToDecode);
            }
        }
        catch (MachineNotExistsException | IllegalArgumentException|SettingsNotInitializedException ex){
            new ErrorDialog(ex, "");
        }
    }
    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) throws ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, SettingsNotInitializedException, MachineNotExistsException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException {
        encryptDecryptGridController.resetMachineState();
    }
    public void setDecodedWord(String decodeWord) {
        encryptedDecryptedWordText.setText(decodeWord);
    }
}
