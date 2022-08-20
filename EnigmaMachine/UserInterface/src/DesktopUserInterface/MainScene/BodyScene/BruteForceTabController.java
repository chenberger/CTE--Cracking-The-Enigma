package DesktopUserInterface.MainScene.BodyScene;

import DesktopUserInterface.MainScene.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class BruteForceTabController {
    @FXML
    private Tab BruteForceTab;
    private MainController mainController;
    @FXML
    void OnBruteForceTabSelected(ActionEvent event) {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
