package UBoatMachinePane;

import CodeCalibrationPane.CodeCalibrationController;
import MachineDetailsPane.MachineDetailsController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class UBoatMachinePaneController {
    @FXML
    private AnchorPane UBoatMachinePane;
    @FXML private GridPane machineDetailsPane;
    @FXML private MachineDetailsController machineDetailsPaneController;
    @FXML private GridPane codeCalibrationPane;
    @FXML private CodeCalibrationController codeCalibrationPaneController;

    public void initialize() {
        if (machineDetailsPaneController != null) {
            machineDetailsPaneController.setUBoatMachinePaneController(this);
        }
        if (codeCalibrationPaneController != null) {
            codeCalibrationPaneController.setUBoatMachinePaneController(this);
        }
    }

}
