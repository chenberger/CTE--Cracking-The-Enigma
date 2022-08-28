package DesktopUserInterface.MainScene.BodyScene.Machine;

import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.EngineManager;
import EnigmaMachine.Settings.Sector;
import EnigmaMachineException.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class CodeCalibrationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private Button setRandomCodeButton;
    @FXML private Button setManuallyCodeButton;
    private MachineGridController machineGridController;
    private ManuallyCodeInitializerScene manuallyCodeInitializerScene;
    private EngineManager engineManager;

    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    @FXML void OnSetManuallyCodeButtonClicked(ActionEvent event) throws IOException {
        manuallyCodeInitializerScene= new ManuallyCodeInitializerScene();

        try {
            manuallyCodeInitializerScene.show(engineManager.displaySpecifications(), this);
        }
        catch (MachineNotExistsException  | CloneNotSupportedException ex) {
            new ErrorDialog(ex, "Failed to set configuration code manually");
        }
    }

    @FXML void OnSetRandomCodeButtonClicked(ActionEvent event) {
        machineGridController.setAutomaticCodeConfiguration();
    }

    public void codeConfigurationSetted(List<Sector> codeConfigurationSectors) {
        try {
            engineManager.initializeSettings(codeConfigurationSectors);
        }
        catch (MachineNotExistsException  | SettingsFormatException | CloneNotSupportedException  |RotorsInUseSettingsException | SettingsNotInitializedException |
               StartingPositionsOfTheRotorException | ReflectorSettingsException | PluginBoardSettingsException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void codeConfigurationSetSuccessfully() {
        manuallyCodeInitializerScene.close();
    }
}
