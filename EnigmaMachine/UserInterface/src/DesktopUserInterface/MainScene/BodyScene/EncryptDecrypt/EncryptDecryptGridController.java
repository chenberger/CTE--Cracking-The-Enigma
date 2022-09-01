package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.BodyScene.Machine.CurrentCodeConfigurationController;
import DesktopUserInterface.MainScene.Common.Utils;
import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import EnigmaMachineException.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    @FXML private FlowPane keyboardFlowPane;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;
    private Map<Character, KeyboardButtonController> keyboardControllers;

    public void UpdateCurrentConfiguration() {

    }

    public void decodeWord(String text) {
        try {
            Utils.checkIfMachineExistsAndInitialized(enigmaMachineEngine);
        } catch (MachineNotExistsException  | SettingsNotInitializedException ex) {
            new ErrorDialog(ex,"Unable to decode.");
        }

        try {
            String decodeWord = enigmaMachineEngine.processInput(text.toUpperCase());
            EncryptDecryptDetailsController.setDecodedWord(decodeWord);
            KeyboardButtonController test = keyboardControllers.get(decodeWord.charAt(0));

            test.turnOnBulbButton();
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
        enigmaMachineEngine.keyboardChangedHandler.add(this::initializeKeyboard);
    }

    private void initializeKeyboard(Object source, Set<Character> keyboard) {
        keyboard.forEach(this::createKeyboardButton);
    }

    private void createKeyboardButton(Character word) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("KeyboardButton.fxml"));
            Node keyboardAnchorPane = fxmlLoader.load();
            KeyboardButtonController singleKeyboardButtonController = (KeyboardButtonController) fxmlLoader.getController();
            singleKeyboardButtonController.setEncryptDecryptGridController(this);
            singleKeyboardButtonController.setKeyboardCharacter(word);

            keyboardFlowPane.getChildren().add(keyboardAnchorPane);
            keyboardControllers.put(word, singleKeyboardButtonController);

        } catch (IOException ignored) {

        }
    }

    public void initialize() {
        if(EncryptDecryptDetailsController != null) {
            EncryptDecryptDetailsController.setEncryptDecryptGridController(this);
        }
        if(machineStatisticController != null) {
            machineStatisticController.setEncryptDecryptGridController(this);
        }

        keyboardControllers = new HashMap<>();
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


    public void onFinishInput() throws MachineNotExistsException, CloneNotSupportedException {
        try {
            enigmaMachineEngine.insertingInputFinished();
        } catch (MachineNotExistsException | CloneNotSupportedException | IllegalArgumentException ex) {
            new ErrorDialog(ex, "Unable to Process input.");
        }
    }

    public void keyBoardButtonClicked(Character keyboardCharacter) {
        decodeWord(keyboardCharacter.toString());
    }
}
