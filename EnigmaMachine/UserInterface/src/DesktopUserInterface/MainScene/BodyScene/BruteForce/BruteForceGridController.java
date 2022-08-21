package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class BruteForceGridController {
    @FXML
    private GridPane BruteForceGrid;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
