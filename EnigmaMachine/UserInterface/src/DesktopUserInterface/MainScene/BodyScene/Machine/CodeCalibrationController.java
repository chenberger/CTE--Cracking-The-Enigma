package DesktopUserInterface.MainScene.BodyScene.Machine;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class CodeCalibrationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private GridPane machineGrid;

    @FXML private Button setRandomCodeButton;

    @FXML private Button setManuallyCodeButton;
    private MachineGridController machineGridController;

    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    @FXML void OnSetManuallyCodeButtonClicked(ActionEvent event) {

    }

    @FXML void OnSetRandomCodeButtonClicked(ActionEvent event) {

    }

}
