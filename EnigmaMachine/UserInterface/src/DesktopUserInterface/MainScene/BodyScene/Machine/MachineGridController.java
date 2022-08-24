package DesktopUserInterface.MainScene.BodyScene.Machine;

import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import EnigmaMachineException.*;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class MachineGridController {
    @FXML private GridPane machineGrid;
    @FXML private ScrollPane machineDetailsGrid;
    @FXML private MachineDetailsController machineDetailsGridController;
    @FXML private CodeCalibrationController codeCalibrationGridController;
    @FXML private ScrollPane currentCodeConfigurationGrid;
    @FXML private CurrentCodeConfigurationController currentCodeConfigurationGridController;
    @FXML private ScrollPane codeCalibrationGrid;
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
             currentCodeConfigurationGridController.setMachineController(this);
         }

         if(machineDetailsGrid != null) {
            machineDetailsGrid.setStyle("-fx-background-color:transparent;");
         }
     }
     public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.mainController.addCurrentCodeConfigurationController(currentCodeConfigurationGridController);
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
         this.enigmaMachineEngine = enigmaMachineEngine;
         this.codeCalibrationGridController.setEngineManager(enigmaMachineEngine);
    }

    public void setMachineDetails() {
         try {
             machineDetailsGridController.machineDetailsChanged(enigmaMachineEngine.displaySpecifications());
         } catch (MachineNotExistsException | CloneNotSupportedException ex) {
             new ErrorDialog(ex, "Failed to get the machine details");
         }
    }

    public void setAutomaticCodeConfiguration() {
         try {
             enigmaMachineEngine.setSettingsAutomatically();
             machineDetailsGridController.machineDetailsChanged(enigmaMachineEngine.displaySpecifications());
             mainController.currentCodeConfigurationChanged();

             //TODO erez: update machine details and current code configuration
         }
         catch (ReflectorSettingsException | RotorsInUseSettingsException | SettingsFormatException |
                  SettingsNotInitializedException | StartingPositionsOfTheRotorException | PluginBoardSettingsException |
                  CloneNotSupportedException | MachineNotExistsException ex) {
             new ErrorDialog(ex, "Failed to initialized the code configuration automatically");
         }
    }

    public boolean isMachineExists() {
         return enigmaMachineEngine.isMachineExists();
    }
}
