package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.BodyScene.Machine.CurrentCodeConfigurationController;
import DesktopUserInterface.MainScene.Common.Utils;
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

    public void UpdateCurrentConfiguration() {

    }

    public void decodeWord(String text) throws MachineNotExistsException, SettingsNotInitializedException {
        Utils.checkIfMachineExistsAndInitialized(enigmaMachineEngine);

        try {
            if(text.length() != 1) {
                throw new IllegalArgumentException("The Length of the text must be 1!!!");
            }
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
    public void resetMachineState() {
        try{
            enigmaMachineEngine.resetSettings();
        }
        catch (ReflectorSettingsException | RotorsInUseSettingsException | SettingsFormatException | MachineNotExistsException |
                SettingsNotInitializedException | StartingPositionsOfTheRotorException | PluginBoardSettingsException  | CloneNotSupportedException ex) {
            new ErrorDialog(ex,"Unable to reset machine state.");
        }
    }
    //TODO chen: add keyboard bonus spare room component
}
