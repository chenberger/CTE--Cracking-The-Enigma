package DesktopUserInterface.MainScene.BodyScene.Machine;

import DesktopUserInterface.MainScene.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MachineGridController {
    @FXML private GridPane machineGrid;
    @FXML private GridPane machineDetailsGrid;
    @FXML private MachineDetailsController machineDetailsController;
    @FXML private CodeCalibrationController codeCalibrationController;
    @FXML private GridPane currentCodeConfigurationGrid;
    @FXML private CurrentCodeConfigurationController currentCodeConfigurationController;
    @FXML private GridPane codeCalibrationGrid;
     private MainController mainController;

     public void initialize() {
         if(machineDetailsController != null) {
             machineDetailsController.setMachineGridController(this);
         }

         if(codeCalibrationController != null) {
             codeCalibrationController.setMachineGridController(this);
         }

         if(currentCodeConfigurationController != null) {
             currentCodeConfigurationController.setMachineGridController(this);
         }
     }
     public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
