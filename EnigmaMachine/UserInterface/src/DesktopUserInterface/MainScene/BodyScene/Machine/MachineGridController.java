package DesktopUserInterface.MainScene.BodyScene.Machine;

import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import EnigmaMachineException.*;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MachineGridController {
    @FXML private GridPane machineGrid;
    @FXML private GridPane machineDetailsGrid;
    @FXML private MachineDetailsController machineDetailsGridController;
    @FXML private CodeCalibrationController codeCalibrationGridController;
    @FXML private VBox currentCodeConfigurationGrid;
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

         if(machineDetailsGrid != null) {
            machineDetailsGrid.setStyle("-fx-background-color:transparent;");
         }
     }

    private void registerToEvents() {
        enigmaMachineEngine.currentCodeConfigurationHandler.add(machineDetailsGridController::currentCodeChanged);
        enigmaMachineEngine.currentCodeConfigurationHandler.add(currentCodeConfigurationGridController::currentCodeChanged);
        enigmaMachineEngine.machineDetailsHandler.add(machineDetailsGridController::machineDetailsChanged);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.mainController.addCurrentCodeConfigurationController(currentCodeConfigurationGridController);
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
         this.enigmaMachineEngine = enigmaMachineEngine;
         this.codeCalibrationGridController.setEngineManager(enigmaMachineEngine);

         registerToEvents();
    }

    public void setAutomaticCodeConfiguration() {
         try {
             enigmaMachineEngine.setSettingsAutomatically();
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
