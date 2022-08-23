package DesktopUserInterface.MainScene.BodyScene.Machine;

import DTO.MachineDetails;
import EnigmaMachine.RomanNumber;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManuallyCodeController {
    @FXML private Button setCodeButton;
    @FXML private HBox RotorsId;
    @FXML private HBox StartingPositions;
    @FXML private ChoiceBox<RomanNumber> reflectorChoiceBox;
    private CodeCalibrationController codeCalibrationController;
    private MachineDetails machineDetails;
    @FXML private ScrollPane pluginBoardArea;
    @FXML private ScrollPane rotorsIdArea;
    @FXML private ScrollPane startingPositionsArea;
    @FXML private ScrollPane reflectorArea;
    private ScrollPane currentScrollPaneToDisplay;
    @FXML private ChoiceBox<Character> leftPluggedPairChoiceBox;

    @FXML private ChoiceBox<Character> rightPluggedPairChoiceBox;

    @FXML private TextArea pluggedPairsTextArea;

    @FXML private Button addPluginPairButton;

    @FXML void OnPluginBoardButtonClicked(ActionEvent event) {
        currentScrollPaneToDisplay.setVisible(false);
        pluginBoardArea.setVisible(true);
        currentScrollPaneToDisplay = pluginBoardArea;
    }
    @FXML void OnReflectorButtonClicked(ActionEvent event) {
        currentScrollPaneToDisplay.setVisible(false);
        reflectorArea.setVisible(true);
        currentScrollPaneToDisplay = reflectorArea;
    }

    @FXML void OnRotorIdButtonClicked(ActionEvent event) {
        currentScrollPaneToDisplay.setVisible(false);
        rotorsIdArea.setVisible(true);
        currentScrollPaneToDisplay = rotorsIdArea;
    }
    @FXML void OnStartingPositionsButtonClicked(ActionEvent event) {
        currentScrollPaneToDisplay.setVisible(false);
        startingPositionsArea.setVisible(true);
        currentScrollPaneToDisplay = startingPositionsArea;
    }

    @FXML void OnSetCodeButtonClicked(ActionEvent event) {
        //codeCalibrationController.codeConfigurationSettled();
    }

    @FXML
    void OnAddPluginPairButtonClicked(ActionEvent event) {
        pluggedPairsTextArea.appendText("Plugged pair: " + leftPluggedPairChoiceBox.getValue() + ", " + rightPluggedPairChoiceBox.getValue() + System.lineSeparator());
    }

    public void initialize() {
        pluginBoardArea.setVisible(false);
        rotorsIdArea.setVisible(false);
        startingPositionsArea.setVisible(false);
        reflectorArea.setVisible(false);
        currentScrollPaneToDisplay = new ScrollPane();
    }
    public void initializeControls()  {
        VBox rotorIdVBox;
        VBox rotorStartingPositionVBox;
        Label label;
        ChoiceBox<Integer> choiceBox;

        for (int i = 0; i < machineDetails.getAmountCurrentRotorsInUse(); i++) {
            rotorIdVBox = new VBox();
            label = new Label("Rotor id: " + machineDetails.getAllRotorsId().get(i).toString());
            rotorIdVBox.getChildren().add(label);
            choiceBox = new ChoiceBox<Integer>(FXCollections.observableArrayList(machineDetails.getAllRotorsId()));
            choiceBox.setPrefWidth(label.getPrefWidth());
            rotorIdVBox.getChildren().add(choiceBox);
            RotorsId.getChildren().add(rotorIdVBox);

            rotorStartingPositionVBox = new VBox();
            rotorStartingPositionVBox.getChildren().add(new Label("Rotor id: " + machineDetails.getAllRotorsId().get(i).toString()));
            rotorStartingPositionVBox.getChildren().add(new ChoiceBox<Character>(FXCollections.observableArrayList(machineDetails.getKeyboardCharacters())));
            StartingPositions.getChildren().add(rotorStartingPositionVBox);
        }

        reflectorChoiceBox.setItems(FXCollections.observableArrayList(machineDetails.getAllReflectorsId()));
        leftPluggedPairChoiceBox.setItems(FXCollections.observableArrayList(machineDetails.getKeyboardCharacters()));
        rightPluggedPairChoiceBox.setItems(FXCollections.observableArrayList(machineDetails.getKeyboardCharacters()));
    }

    public void setCodeCalibrationController(CodeCalibrationController codeCalibrationController) {
        this.codeCalibrationController = codeCalibrationController;
    }

    public void setMachineDetails(MachineDetails machineDetails) {
        this.machineDetails = machineDetails;
    }
}
