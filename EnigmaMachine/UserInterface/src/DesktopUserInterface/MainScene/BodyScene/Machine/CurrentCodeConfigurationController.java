package DesktopUserInterface.MainScene.BodyScene.Machine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CurrentCodeConfigurationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private Label currentCodeConfigurationLabel;

    public void setCodeConfiguration(String currentMachineSettings) {
        currentCodeConfigurationLabel.setText(currentMachineSettings);
    }

    public void currentCodeChanged(Object o, String currentCode) {
        currentCodeConfigurationLabel.setText(currentCode);
    }
}
