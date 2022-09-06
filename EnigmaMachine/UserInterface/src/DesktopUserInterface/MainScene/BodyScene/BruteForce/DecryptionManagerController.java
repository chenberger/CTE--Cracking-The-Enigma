package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import BruteForce.DecryptionManager;
import BruteForce.DifficultyLevel;
import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.ErrorDialog;
import EnigmaMachineException.DecryptionManagerSettingsException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
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

    private final String START_LABEL = "Start";
    private final String STOP_LABEL = "Stop";
    private final String PAUSE_LABEL = "Pause";
    private final String RESUME_LABEL = "Resume";

    private BruteForceGridController bruteForceGridController;
    private SimpleStringProperty agentsAmountProperty;
    private SimpleBooleanProperty isStartButtonClicked;

    public DecryptionManagerController() {
        this.agentsAmountProperty = new SimpleStringProperty("");
        this.isStartButtonClicked = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize() {
        agentsAmountLabel.textProperty().bind(agentsAmountProperty);
        pauseResumeButton.disableProperty().bind(isStartButtonClicked.not());
        difficultyLevelComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(DifficultyLevel.values()).map(Enum::name).collect(Collectors.toList())));
    }

    @FXML void onDifficultyLevelComboBoxChosen(ActionEvent event) {

    }

    @FXML void onPauseResumeButtonClicked(ActionEvent event) {
        pauseResumeButton.setText(pauseResumeButton.getText() == PAUSE_LABEL ? RESUME_LABEL : PAUSE_LABEL);
    }

    @FXML void onSetButtonClicked(ActionEvent event) {
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
        }
    }

    private void validateAgentsAmount(DecryptionManagerSettingsException decryptionManagerSettingsException, BruteForceTask bruteForceTask) {
        try {
            Integer agentsAmount = Integer.parseInt(agentsAmountLabel.getText());

            if(agentsAmount < DecryptionManager.getMinAgentsAmount()) {
                decryptionManagerSettingsException.addIllegalAgentsAmount(agentsAmount, DecryptionManager.getMinAgentsAmount(), agentsAmountSlider.getMax());
            }
            else {
                bruteForceTask.setAmountOfAgents(agentsAmount);
            }
        }
        catch(NumberFormatException ex) {
            //decryptionManagerSettingsException.addFailedParseAgentsAmountToInt(agentsAmountLabel.getText());
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

    @FXML void onStartStopButtonClicked(ActionEvent event) {
        isStartButtonClicked.set(startStopButton.getText() == START_LABEL);
        startStopButton.setText(startStopButton.getText() == START_LABEL ? STOP_LABEL : START_LABEL);
    }

    public void setBruteForceGridController(BruteForceGridController bruteForceGridController) {
        this.bruteForceGridController = bruteForceGridController;
    }

    @FXML public void agentsAmountSliderChanged(MouseEvent mouseEvent) {
        agentsAmountProperty.set(String.valueOf(agentsAmountSlider.getValue()));
    }

    public void setMaxAmountOfAgents(Object o, Integer maxAmountOfAgents) {
        agentsAmountSlider.setMax(maxAmountOfAgents);
    }
}
