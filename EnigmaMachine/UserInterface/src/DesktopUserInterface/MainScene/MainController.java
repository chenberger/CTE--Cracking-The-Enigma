package DesktopUserInterface.MainScene;

import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceGridController;
import DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt.EncryptDecryptGridController;
import DesktopUserInterface.MainScene.BodyScene.Machine.MachineGridController;
import DesktopUserInterface.MainScene.TopScene.TopBorderPaneController;
import Engine.EngineManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MainController {
    @FXML public TabPane tabPane;
    @FXML private BorderPane borderPane;
    @FXML private TopBorderPaneController topBorderPaneController;
    @FXML private GridPane topBorderPane;
    @FXML private GridPane machineGrid;
    @FXML private MachineGridController machineGridController;
    @FXML private GridPane encryptDecryptGrid;
    @FXML private EncryptDecryptGridController encryptDecryptGridController;
    @FXML private GridPane bruteForceGrid;
    @FXML private BruteForceGridController bruteForceGridController;

    private final EngineManager enigmaMachineEngine;

    public MainController() {
        this.enigmaMachineEngine = new EngineManager();
    }


    @FXML
    public void initialize() {
        if(topBorderPaneController != null) {
            topBorderPaneController.setMainController(this);
        }

        if(machineGridController != null) {
            machineGridController.setMainController(this);
        }

        if(encryptDecryptGridController != null) {
            encryptDecryptGridController.setMainController(this);
        }

        if(bruteForceGridController != null) {
            bruteForceGridController.setMainController(this);
        }
    }

    public void OnMachineTabSelected(Event event) {
    }

    public void OnEncryptDecryptTabSelected(Event event) {
    }

    public void OnBruteForceTabSelected(Event event) {
    }

}
