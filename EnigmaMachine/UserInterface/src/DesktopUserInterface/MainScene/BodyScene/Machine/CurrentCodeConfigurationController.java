package DesktopUserInterface.MainScene.BodyScene.Machine;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class CurrentCodeConfigurationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private TextArea currentCodeConfigurationTextArea;

    public void setCodeConfiguration(String currentMachineSettings) {
        currentCodeConfigurationTextArea.setText(currentMachineSettings);
    }

    public void currentCodeChanged(Object o, String currentCode) {
        currentCodeConfigurationTextArea.setText(currentCode);
    }
}
