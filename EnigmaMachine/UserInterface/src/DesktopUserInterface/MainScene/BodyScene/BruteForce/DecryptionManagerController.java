package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import BruteForce.DifficultyLevel;
import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.ErrorDialog;
import EnigmaMachineException.DecryptionManagerSettingsException;
import EnigmaMachineException.DecryptionMessegeNotInitializedException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.Objects;
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
    private BruteForceTask bruteForceTask;

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
        if(Objects.equals(pauseResumeButton.getText(), PAUSE_LABEL)) {
            //TODO erez: implement pause to brute force mission

            pauseResumeButton.setText(RESUME_LABEL);
        }
        else {
            //TODO erez: implement resume tu brute force mission
            pauseResumeButton.setText(PAUSE_LABEL);
        }
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

/*        catch(NumberFormatException ex) {
            //decryptionManagerSettingsException.addFailedParseAgentsAmountToInt(agentsAmountLabel.getText());
        }*/
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

    @FXML private void onStartStopButtonClicked(ActionEvent event) {
        if(Objects.equals(startStopButton.getText(), START_LABEL)) {
            try {
                bruteForceGridController.startBruteForce(bruteForceTask);
                isStartButtonClicked.set(true);
                startStopButton.setText(STOP_LABEL);
            }
            catch (DecryptionMessegeNotInitializedException | CloneNotSupportedException  | IllegalArgumentException ex) {
                new ErrorDialog(ex, "Error: Failed to start brute force decipher mission");
            }
        }
        else {
            startStopButton.setText(START_LABEL);
            isStartButtonClicked.set(false);
            //TODO erez: implement stop brute force
        }

        pauseResumeButton.setText(PAUSE_LABEL);
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
}
