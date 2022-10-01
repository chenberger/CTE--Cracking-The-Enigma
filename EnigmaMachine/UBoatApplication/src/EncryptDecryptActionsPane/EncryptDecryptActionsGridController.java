package EncryptDecryptActionsPane;

import CompetitionPane.UBoatCompetitionPaneController;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceGridController;
import DesktopUserInterface.MainScene.Common.AutoCompleteBox;
import DesktopUserInterface.MainScene.Common.SkinType;
import Engine.Dictionary;
import UBoatServletsPaths.UBoatsServletsPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import okhttp3.HttpUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EncryptDecryptActionsGridController {
    private BruteForceGridController bruteForceGridController;
    private UBoatCompetitionPaneController uBoatCompetitionPaneController;
    @FXML private Button addWordFromDictButton;
    @FXML private TextField encryptedDecryptedWordText;
    @FXML private TextField processedWordText;
    @FXML private Button encryptDecryptButton;
    @FXML private Button ResetMachineStateButton;
    @FXML private Label encryptDecryptTextLabel;
    @FXML private ComboBox<String> dictionarySearchComboBox;
    @FXML private GridPane encryptDecryptActionGrid;
    private AutoCompleteBox<String> autoCompleteDictionaryBox;
    private Map<SkinType, String> skinPaths;

    @FXML public void initialize() {
        autoCompleteDictionaryBox = new AutoCompleteBox<String>(dictionarySearchComboBox);
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "EncryptDecryptActionsGridSkin" + skinIndex++ + ".css");
        }
    }

    //public void setBruteForceGridController(BruteForceGridController bruteForceGridController) {
    //    this.bruteForceGridController = bruteForceGridController;
    //}
    public void setUBoatCompetitionPaneController(UBoatCompetitionPaneController uBoatCompetitionPaneController) {
        this.uBoatCompetitionPaneController = uBoatCompetitionPaneController;
    }

    @FXML private void onDecryptionButtonClicked(ActionEvent event) {
        //bruteForceGridController.decodeFromDictionary(encryptedDecryptedWordText.getText());
        String wordToProcess = encryptedDecryptedWordText.getText();
        if (wordToProcess.isEmpty()) {
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.U_BOAT_LOGIN_SERVLET)
                .newBuilder()
                .addQueryParameter("wordToProcess", wordToProcess)
                .build()
                .toString();
    }
    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) {
            bruteForceGridController.resetMachineState();
    }

    public void setProcessedString(String processedString) {
        processedWordText.setText(processedString);
    }

    @FXML private void onAddWordFromDictButtonClicked(ActionEvent event) {
        encryptedDecryptedWordText.setText(encryptedDecryptedWordText.getText() + dictionarySearchComboBox.getEditor().getText());
        dictionarySearchComboBox.getEditor().setText("");
    }

    public void setDictionary(Object source, Dictionary dictionary) {
        this.autoCompleteDictionaryBox.setData(dictionary.getDictionary());
        dictionarySearchComboBox.setDisable(false);
        addWordFromDictButton.setDisable(false);
    }

    public void setSkin(SkinType skinType) {
        encryptDecryptActionGrid.getStylesheets().clear();
        encryptDecryptActionGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }

    public void clearTexts() {
        encryptedDecryptedWordText.clear();
        processedWordText.clear();
        dictionarySearchComboBox.getEditor().setText("");
    }

    public String getProccesesedString() {
        return processedWordText.getText().equals("") ? null : processedWordText.getText();
    }
}
