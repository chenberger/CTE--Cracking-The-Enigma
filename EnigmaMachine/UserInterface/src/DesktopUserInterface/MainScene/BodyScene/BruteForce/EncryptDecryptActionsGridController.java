package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.Common.AutoCompleteBox;
import Engine.Dictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EncryptDecryptActionsGridController {
    private BruteForceGridController bruteForceGridController;
    @FXML private Button addWordFromDictButton;
    @FXML private TextField encryptedDecryptedWordText;
    @FXML private TextField processedWordText;
    @FXML private Button encryptDecryptButton;
    @FXML private Button ResetMachineStateButton;
    @FXML private Label encryptDecryptTextLabel;
    @FXML private ComboBox<String> dictionarySearchComboBox;
    private AutoCompleteBox<String> autoCompleteDictionaryBox;

    @FXML public void initialize() {
        autoCompleteDictionaryBox = new AutoCompleteBox<String>(dictionarySearchComboBox);
        dictionarySearchComboBox.setDisable(true);
        addWordFromDictButton.setDisable(true);
    }

    public void setBruteForceGridController(BruteForceGridController bruteForceGridController) {
        this.bruteForceGridController = bruteForceGridController;
    }

    @FXML private void onDecryptionButtonClicked(ActionEvent event) {
        bruteForceGridController.decodeFromDictionary(encryptedDecryptedWordText.getText());
    }
    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) {
            bruteForceGridController.resetMachineState();
    }

    public void setProcessedString(String processedString) {
        processedWordText.setText(processedString);
    }

    @FXML private void onaAdWordFromDictButtonClicked(ActionEvent event) {
        encryptedDecryptedWordText.setText(encryptedDecryptedWordText.getText() + dictionarySearchComboBox.getEditor().getText());
    }

    public void setDictionary(Object source, Dictionary dictionary) {
        this.autoCompleteDictionaryBox.setData(dictionary.getDictionary());
        dictionarySearchComboBox.setDisable(false);
        addWordFromDictButton.setDisable(false);
    }
}
