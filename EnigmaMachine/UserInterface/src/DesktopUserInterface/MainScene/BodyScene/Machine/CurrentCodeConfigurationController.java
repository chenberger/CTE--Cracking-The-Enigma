package DesktopUserInterface.MainScene.BodyScene.Machine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CurrentCodeConfigurationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private Label currentCodeConfigurationLabel;
    private MachineGridController machineGridController;

    public void setCodeConfiguration(String currentMachineSettings) {
        currentCodeConfigurationLabel.setText(currentMachineSettings);
    }

    public void setMachineController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }
}
