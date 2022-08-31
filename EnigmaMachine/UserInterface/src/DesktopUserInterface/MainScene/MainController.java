package DesktopUserInterface.MainScene;

import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceGridController;
import DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt.EncryptDecryptGridController;
import DesktopUserInterface.MainScene.BodyScene.Machine.CurrentCodeConfigurationController;
import DesktopUserInterface.MainScene.BodyScene.Machine.MachineGridController;
import DesktopUserInterface.MainScene.TopScene.TopBorderPaneController;
import Engine.EngineManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML public TabPane tabPane;
    @FXML private BorderPane borderPane;
    @FXML private TopBorderPaneController topBorderPaneController;
    @FXML private GridPane topBorderPane;
    @FXML private GridPane machineGrid;
    @FXML private MachineGridController machineGridController;
    @FXML private ScrollPane encryptDecryptGrid;
    @FXML private EncryptDecryptGridController encryptDecryptGridController;
    @FXML private GridPane bruteForceGrid;
    @FXML private BruteForceGridController bruteForceGridController;
    @FXML private List<CurrentCodeConfigurationController> currentCodeConfigurationGridControllers;

    private final EngineManager enigmaMachineEngine;

    public MainController() {
        this.enigmaMachineEngine = new EngineManager();
        this.currentCodeConfigurationGridControllers = new ArrayList<>();

        registerToEvents();
    }

    private void registerToEvents() {
        enigmaMachineEngine.currentCodeConfigurationHandler.add(this::currentCodeConfigurationChanged);
    }

    @FXML
    public void initialize() {
        if(topBorderPaneController != null) {
            topBorderPaneController.setMainController(this);
            topBorderPaneController.setEngineManager(enigmaMachineEngine);
        }

        if(machineGridController != null) {
            machineGridController.setMainController(this);
            machineGridController.setEngineManager(enigmaMachineEngine);
        }

        if(encryptDecryptGridController != null) {
            encryptDecryptGridController.setMainController(this);
            encryptDecryptGridController.setEngineManager(enigmaMachineEngine);
        }

        if(bruteForceGridController != null) {
            bruteForceGridController.setMainController(this);
            bruteForceGridController.setEngineManager(enigmaMachineEngine);
        }
    }

    public void addCurrentCodeConfigurationController(CurrentCodeConfigurationController currentCodeConfigurationController) {
        currentCodeConfigurationGridControllers.add(currentCodeConfigurationController);
    }

    public void OnMachineTabSelected(Event event) {
    }

    public void OnEncryptDecryptTabSelected(Event event) {
    }

    public void OnBruteForceTabSelected(Event event) {
    }

    public Window getStageWindow() {
        return borderPane.getScene().getWindow();
    }

    public void currentCodeConfigurationChanged(Object source , String currentCodeConfiguration) {
        currentCodeConfigurationGridControllers.forEach(code -> {
            code.setCodeConfiguration(currentCodeConfiguration);
        });
    }
}
