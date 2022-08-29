package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EncryptDecryptActionsGridController {
    private BruteForceGridController bruteForceGridController;
    @FXML private Button selectWordFromDict;
    @FXML private TextField encryptedDecryptedWordText;
    @FXML private TextField encryptDecryptTextBox;

    @FXML private Button encryptDecryptButton;

    @FXML private Button ResetMachineStateButton;

    @FXML private Label encryptDecryptTextLabel;
    private DictionarySearchScene dictionarySearchScene;

    public EncryptDecryptActionsGridController() {
        dictionarySearchScene = new DictionarySearchScene();
    }
    public void setBruteForceGridController(BruteForceGridController bruteForceGridController) {
        this.bruteForceGridController = bruteForceGridController;
    }

    @FXML void onSelectWordFromDictButtonClicked(ActionEvent event) throws IOException {
        dictionarySearchScene.show(bruteForceGridController.getDictionary(), this);

    }

    @FXML
    private void onDecryptionButtonClicked(ActionEvent event) {
        String textToDecode = encryptDecryptTextBox.getText();
        bruteForceGridController.decodeFromDictionary(textToDecode);
    }
    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) {
            bruteForceGridController.resetMachineState();
    }

    public void wordSelectedFromDictionary(String selectedItem) {
        encryptDecryptTextBox.setText(encryptDecryptTextBox.getText() + selectedItem);
        dictionarySearchScene.close();
    }

    public void setProcessedString(String processedString) {
        encryptedDecryptedWordText.setText(processedString);
    }
}
