package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import BruteForce.DifficultyLevel;
import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.Common.SkinType;
import DesktopUserInterface.MainScene.ErrorDialog;
import EnigmaMachineException.DecryptionManagerSettingsException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DecryptionManagerController {

    @FXML private GridPane bruteForceGrid;

    @FXML private Label agentsAmountLabel;
    @FXML private Label succsesSetLabel;
    @FXML private Slider agentsAmountSlider;
    @FXML private ComboBox<String> difficultyLevelComboBox;
    @FXML private TextField taskSizeTextField;

    @FXML private Button startStopButton;

    @FXML private Button pauseResumeButton;
    @FXML private Button setButton;
    @FXML private GridPane dmManagerGrid;

    private BruteForceTask bruteForceTask;
    private BruteForceGridController bruteForceGridController;
    private SimpleStringProperty agentsAmountProperty;
    private Map<SkinType, String> skinPaths;

    public DecryptionManagerController() {
        this.agentsAmountProperty = new SimpleStringProperty("");
    }

    @FXML public void initialize() {
        agentsAmountLabel.textProperty().bind(agentsAmountProperty);
        difficultyLevelComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(DifficultyLevel.values()).map(Enum::name).collect(Collectors.toList())));
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "DMOperationalSkin" + skinIndex++ + ".css");
        }
    }

    @FXML void onDifficultyLevelComboBoxChosen(ActionEvent event) {

    }

    @FXML private void onSetButtonClicked(ActionEvent event) {
        DecryptionManagerSettingsException decryptionManagerSettingsException = new DecryptionManagerSettingsException();
        BruteForceTask bruteForceTask = new BruteForceTask();

        validateTaskSize(decryptionManagerSettingsException, bruteForceTask);
        validateDifficultyLevel(decryptionManagerSettingsException, bruteForceTask);
        validateAgentsAmount(decryptionManagerSettingsException, bruteForceTask);

        if(decryptionManagerSettingsException.isExceptionNeedToBeThrown()) {
            succsesSetLabel.setText("");
            new ErrorDialog(decryptionManagerSettingsException, "Failed to set the decryption settings");
        }
        else {
            succsesSetLabel.setText("Success to initialize settings");
            this.bruteForceTask = bruteForceTask;
        }
    }

    private void validateAgentsAmount(DecryptionManagerSettingsException decryptionManagerSettingsException, BruteForceTask bruteForceTask) {

            Double agentsAmountDouble = Double.parseDouble(agentsAmountLabel.getText());
            Integer agentsAmount = agentsAmountDouble.intValue();

            if(agentsAmount < 1) {
                decryptionManagerSettingsException.addIllegalAgentsAmount(agentsAmount, 1, agentsAmountSlider.getMax());
            }
            else {
                bruteForceTask.setAmountOfAgents(agentsAmount);
            }
    }

    private void validateDifficultyLevel(DecryptionManagerSettingsException decryptionManagerSettingsException, BruteForceTask bruteForceTask) {
        if(difficultyLevelComboBox.getValue() == null) {
            decryptionManagerSettingsException.addIgelEmptyDifficultyLevel();
        }
        else {
            bruteForceTask.setDifficultTaskLevel(DifficultyLevel.valueOf(difficultyLevelComboBox.getValue()));
        }
    }

    private void validateTaskSize(DecryptionManagerSettingsException decryptionManagerSettingsException, BruteForceTask bruteForceTask) {
        try {
            Integer taskSize = Integer.parseInt(taskSizeTextField.getText());

            if(taskSize <= 0) {
                decryptionManagerSettingsException.addIllegalTaskSize(taskSize);
            }
            else {
                bruteForceTask.setTaskSize(taskSize);
            }
        }
        catch(NumberFormatException ex) {
            decryptionManagerSettingsException.addFailedParseTaskSizeToInt(taskSizeTextField.getText());
        }
    }

    public void setBruteForceGridController(BruteForceGridController bruteForceGridController) {
        this.bruteForceGridController = bruteForceGridController;
    }

    @FXML public void agentsAmountSliderChanged(MouseEvent mouseEvent) {
        agentsAmountProperty.set(String.valueOf(agentsAmountSlider.getValue()));
    }

    public void setMaxAmountOfAgents(Object o, Integer maxAmountOfAgents) {
        agentsAmountSlider.setMax(maxAmountOfAgents);
        agentsAmountProperty.set(String.valueOf(agentsAmountSlider.getValue()));
    }

    public BruteForceTask getBruteForceTask() {
        return bruteForceTask;
    }

    public void setSkin(SkinType skinType) {
        dmManagerGrid.getStylesheets().clear();
        dmManagerGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }
}
