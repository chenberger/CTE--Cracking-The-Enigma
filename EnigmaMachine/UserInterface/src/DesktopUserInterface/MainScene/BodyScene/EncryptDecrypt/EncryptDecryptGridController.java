package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;

import EnigmaMachineException.MachineNotExistsException;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import sun.security.krb5.EncryptedData;

import java.awt.*;

public class EncryptDecryptGridController {
    @FXML
    private GridPane EncryptDecryptGrid;
    @FXML
    private GridPane EncryptDecryptDetails;
    @FXML
    private ScrollPane machineStatistics;

    private MachineStatisticsController machineStatisticsController;
    private EncryptDecryptDetailsController encryptDecryptDetailsController;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;

    public void decodeWord(String text) throws MachineNotExistsException {
        String decodeWord = enigmaMachineEngine.processInput(text);
        encryptDecryptDetailsController.setDecodedWord(decodeWord);
        machineStatisticsController.setDecodeWord(decodeWord);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
    public void initialize() {
        if(encryptDecryptDetailsController != null) {
            encryptDecryptDetailsController.setEncryptDecryptGridController(this);
        }
        if(machineStatisticsController != null) {
            machineStatisticsController.setEncryptDecryptGridController(this);
        }
    }


}
