package DesktopUserInterface.MainScene.BodyScene.Machine;

import DesktopUserInterface.MainScene.ErrorDialog;
import EnigmaMachineException.MachineNotExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class CodeCalibrationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private Button setRandomCodeButton;
    @FXML private Button setManuallyCodeButton;
    private MachineGridController machineGridController;
    private ManuallyCodeController manuallyCodeController;

    public void initialize() {
        manuallyCodeController = new ManuallyCodeController();
        manuallyCodeController.setCodeCalibrationController(this);
    }
    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    @FXML void OnSetManuallyCodeButtonClicked(ActionEvent event) throws IOException {
        if(machineGridController.isMachineExists()) {
            manuallyCodeController.show();
        }
        else {
            new ErrorDialog(new MachineNotExistsException(), "Failed to set code configuration manually");
        }
    }

    @FXML void OnSetRandomCodeButtonClicked(ActionEvent event) {
        machineGridController.setAutomaticCodeConfiguration();
    }
}
