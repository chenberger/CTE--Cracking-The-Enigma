package DesktopUserInterface.MainScene.BodyScene.Machine;

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
    private ManuallyCodeInitializer manuallyCodeInitializer;

    public void initialize() {
        manuallyCodeInitializer = new ManuallyCodeInitializer();
    }
    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    @FXML void OnSetManuallyCodeButtonClicked(ActionEvent event) throws IOException {
        {
            manuallyCodeInitializer.show();
        };
    }

    @FXML void OnSetRandomCodeButtonClicked(ActionEvent event) {

    }
}
