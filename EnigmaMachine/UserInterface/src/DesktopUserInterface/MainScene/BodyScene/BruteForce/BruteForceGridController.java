package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class BruteForceGridController {
    @FXML
    private GridPane BruteForceGrid;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
}
