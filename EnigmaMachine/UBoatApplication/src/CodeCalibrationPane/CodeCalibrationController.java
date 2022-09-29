package CodeCalibrationPane;

import CompetitionPane.UBoatCompetitionPaneController;
import DesktopUserInterface.MainScene.BodyScene.Machine.MachineGridController;
import DesktopUserInterface.MainScene.BodyScene.Machine.ManuallyCodeInitializerScene;
import DesktopUserInterface.MainScene.Common.SkinType;
import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.EngineManager;
import EnigmaMachine.Settings.Sector;
import EnigmaMachineException.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeCalibrationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private Button setRandomCodeButton;
    @FXML private Button setManuallyCodeButton;
    private MachineGridController machineGridController;
    private ManuallyCodeInitializerScene manuallyCodeInitializerScene;
    private EngineManager engineManager;
    private Map<SkinType, String> skinPaths;
    private SkinType currentSkinType;
    private UBoatCompetitionPaneController uBoatCompetitionPaneController;

    @FXML public void initialize() {
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "CodeCalibrationSkin" + skinIndex++ + ".css");
        }

        currentSkinType = SkinType.CLASSIC;
    }

    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    @FXML void OnSetManuallyCodeButtonClicked(ActionEvent event) throws IOException {
        manuallyCodeInitializerScene= new ManuallyCodeInitializerScene();

        try {
            manuallyCodeInitializerScene.show(engineManager.displaySpecifications(), this, currentSkinType);
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

    public void setSkin(SkinType skinType) {
        currentSkinType = skinType;
        CodeCalibrationGrid.getStylesheets().clear();
        CodeCalibrationGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }

    public void setUBoatCompetitionPaneController(UBoatCompetitionPaneController uBoatCompetitionPaneController) {
        this.uBoatCompetitionPaneController = uBoatCompetitionPaneController;
    }
}
