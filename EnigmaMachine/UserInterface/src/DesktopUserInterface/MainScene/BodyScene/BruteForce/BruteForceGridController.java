package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.BodyScene.Machine.CurrentCodeConfigurationController;
import DesktopUserInterface.MainScene.Common.Utils;
import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import EnigmaMachineException.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Set;

public class BruteForceGridController {
    @FXML private GridPane DMStatistics;
    @FXML private DMStatisticsController DMStatisticsController;
    @FXML private GridPane BruteForceGrid;
    @FXML private GridPane candidatesAndProgressGrid;
    @FXML private GridPane dMOperationalGrid;
    @FXML private GridPane encryptDecryptActionsGrid;
    @FXML private EncryptDecryptActionsGridController encryptDecryptActionsGridController;
    @FXML private VBox CurrentCodeConfigurationGrid;
    @FXML private CurrentCodeConfigurationController CurrentCodeConfigurationGridController;
    @FXML private GridPane decryptionManager;
    @FXML private DecryptionManagerController decryptionManagerController;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.mainController.addCurrentCodeConfigurationController(CurrentCodeConfigurationGridController);
    }

    private void registerToEvents() {
        enigmaMachineEngine.dictionaryChangedHandler.add(encryptDecryptActionsGridController::setDictionary);
        enigmaMachineEngine.maxAgentsAmountChangedHandler.add(decryptionManagerController::setMaxAmountOfAgents);
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
        registerToEvents();
    }

    public  void initialize(){
        if (encryptDecryptActionsGridController != null){
            encryptDecryptActionsGridController.setBruteForceGridController(this);
        }

        if(decryptionManagerController != null) {
            decryptionManagerController.setBruteForceGridController(this);
        }

        if(DMStatisticsController != null) {
            DMStatisticsController.setBruteForceGridController(this);
        }
    }

    public Set<String> getDictionary() {
        return enigmaMachineEngine.getDictionary();
    }

    public void resetMachineState() {
        try{
               enigmaMachineEngine.resetSettings();
        }
        catch (ReflectorSettingsException | RotorsInUseSettingsException | SettingsFormatException | CloneNotSupportedException |
               SettingsNotInitializedException | StartingPositionsOfTheRotorException | PluginBoardSettingsException  | MachineNotExistsException ex) {
            new ErrorDialog(ex,"Unable to reset machine state.");
        }
    }

    public void decodeFromDictionary(String textToDecode) {
        String processedString;

        try {
            Utils.checkIfMachineExistsAndInitialized(enigmaMachineEngine);
            processedString = enigmaMachineEngine.processInputsFromDictionary(textToDecode);
            encryptDecryptActionsGridController.setProcessedString(processedString);
            //TODO erez: in that case of dict procces dont update the statistics !
        } catch (Exception ex) {
            new ErrorDialog(ex,"Failed to process words from the dictionary");
        }
    }

    public void startBruteForce(BruteForceUIAdapter bruteForceUiAdapter, Runnable onFinish) throws IllegalArgumentException, DecryptionMessegeNotInitializedException, CloneNotSupportedException {
        BruteForceTask bruteForceTask = decryptionManagerController.getBruteForceTask();

        if(bruteForceTask != null) {
            try {
                enigmaMachineEngine.startBruteForceDeciphering(bruteForceTask, bruteForceUiAdapter, onFinish);
            }
            catch (IllegalArgumentException ex) {
                new ErrorDialog(ex, "Failed to start brute force analyzer");
            }
        }
        else {
            throw  new IllegalArgumentException("Failed to start the brute force deciphering, The decryption settings not initialized");
        }
    }

    public void bindTaskToUIComponents(Task<Boolean> tasksManager, Runnable onFinish) {
        DMStatisticsController.bindTaskToUIComponents(tasksManager, onFinish);
    }
}
