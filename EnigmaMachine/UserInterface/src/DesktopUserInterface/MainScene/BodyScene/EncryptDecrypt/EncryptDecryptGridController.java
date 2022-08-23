package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;

import EnigmaMachineException.*;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class EncryptDecryptGridController {
    @FXML private GridPane encryptDecryptGrid;

    @FXML private ScrollPane decodeScroller;
    @FXML private ScrollPane statisticScrollPane;
    @FXML private GridPane EncryptDecryptDetails;
    @FXML private GridPane machineStatistic;
    @FXML private MachineStatisticsGridPaneController machineStatisticController;
    @FXML private EncryptDecryptDetailsController EncryptDecryptDetailsController;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;

    public void decodeWord(String text) throws MachineNotExistsException, SettingsNotInitializedException {
        if (!enigmaMachineEngine.isMachineExists()) {
            throw new MachineNotExistsException();
        }
        else if(!enigmaMachineEngine.isMachineSettingInitialized()) {
            throw new SettingsNotInitializedException();
        }
        try {
            String decodeWord = enigmaMachineEngine.processInput(text.toUpperCase());
            EncryptDecryptDetailsController.setDecodedWord(decodeWord);
            machineStatisticController.showCurrentStatistics(enigmaMachineEngine.getCurrentStatisticsAndHistory().toString());
        }
        catch (MachineNotExistsException | IllegalArgumentException ex){
            new ErrorDialog(ex,"Unable to decode.");
        }

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
    public void initialize() {
        if(EncryptDecryptDetailsController != null) {
            EncryptDecryptDetailsController.setEncryptDecryptGridController(this);
        }
        if(machineStatisticController != null) {
            machineStatisticController.setEncryptDecryptGridController(this);
        }
    }

    public void resetMachineState() throws ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, SettingsNotInitializedException, MachineNotExistsException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException {
        try{
            enigmaMachineEngine.resetSettings();
            //TODO chen: update current code configuration
        }
        catch (ReflectorSettingsException | RotorsInUseSettingsException | SettingsFormatException |
                SettingsNotInitializedException | StartingPositionsOfTheRotorException | PluginBoardSettingsException ex) {
            new ErrorDialog(ex,"Unable to reset machine state.");
        }
    }
}
