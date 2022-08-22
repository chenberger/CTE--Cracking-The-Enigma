package DesktopUserInterface.MainScene.BodyScene.Machine;

import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MachineGridController {
    @FXML private GridPane machineGrid;
    @FXML private GridPane machineDetailsGrid;
    @FXML private MachineDetailsController machineDetailsGridController;
    @FXML private CodeCalibrationController codeCalibrationGridController;
    @FXML private GridPane currentCodeConfigurationGrid;
    @FXML private CurrentCodeConfigurationController currentCodeConfigurationGridController;
    @FXML private GridPane codeCalibrationGrid;
     private MainController mainController;
     private EngineManager enigmaMachineEngine;

     public void initialize() {
         if(machineDetailsGridController != null) {
             machineDetailsGridController.setMachineGridController(this);
         }

         if(codeCalibrationGridController != null) {
             codeCalibrationGridController.setMachineGridController(this);
         }

         if(currentCodeConfigurationGridController != null) {
             currentCodeConfigurationGridController.setMachineGridController(this);
         }
     }
     public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
         this.enigmaMachineEngine = enigmaMachineEngine;
    }
}
