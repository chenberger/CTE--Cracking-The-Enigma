package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.BodyScene.Machine.CurrentCodeConfigurationController;
import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;

import EnigmaMachineException.*;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class EncryptDecryptGridController {
    @FXML private AnchorPane configurationAnchorPane;
    @FXML private GridPane CurrentCodeConfigurationGrid;
    @FXML private CurrentCodeConfigurationController CurrentCodeConfigurationGridController;
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
        }
        catch (MachineNotExistsException | IllegalArgumentException | CloneNotSupportedException ex){
            new ErrorDialog(ex,"Unable to decode.");
        }

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.mainController.addCurrentCodeConfigurationController(CurrentCodeConfigurationGridController);
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;

        registerToEvents();
    }

    private void registerToEvents() {
        enigmaMachineEngine.statisticsAndHistoryHandler.add(machineStatisticController::staticsAndHistoryChanged);
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
    //TODO chen: add keyboard bonus spare room component
}
