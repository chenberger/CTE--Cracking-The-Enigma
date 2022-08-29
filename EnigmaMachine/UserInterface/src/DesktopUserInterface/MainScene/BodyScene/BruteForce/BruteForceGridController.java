package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.BodyScene.Machine.CurrentCodeConfigurationController;
import DesktopUserInterface.MainScene.Common.Utils;
import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import EnigmaMachineException.*;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.Set;

public class BruteForceGridController {
    @FXML private GridPane BruteForceGrid;
    @FXML private GridPane candidatesAndProgressGrid;
    @FXML private CandidatesAndProgressGridController candidatesAndProgressController;
    @FXML private GridPane dMOperationalGrid;
    @FXML private DMOperationalGridController dmOperationalGridController;
    @FXML private GridPane encryptDecryptActionsGrid;
    @FXML private EncryptDecryptActionsGridController encryptDecryptActionsGridController;
    @FXML private GridPane CurrentCodeConfigurationGrid;
    @FXML private CurrentCodeConfigurationController CurrentCodeConfigurationGridController;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        this.mainController.addCurrentCodeConfigurationController(CurrentCodeConfigurationGridController);
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
    public  void initialize(){
        if(candidatesAndProgressController != null){
            candidatesAndProgressController.setBruteForceGridController(this);
        }
        if(dmOperationalGridController != null){
            dmOperationalGridController.setBruteForceGridController(this);
        }
        if (encryptDecryptActionsGridController != null){
            encryptDecryptActionsGridController.setBruteForceGridController(this);
        }
    }
    private void registerToEvents(){
        //TODO:chen/erez - register here when needed
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
            //TODO erez: in that case of procces dont update the statistics !
        } catch (Exception ex) {
            new ErrorDialog(ex,"Failed to process words from the dictionary");
        }
    }
}
