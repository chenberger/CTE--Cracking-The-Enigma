package DesktopUserInterface.MainScene.BodyScene;

import DesktopUserInterface.MainScene.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class EncryptDecryptTabController {
    @FXML
    private Tab EncryptDecryptTab;
    private MainController mainController;
    @FXML
    void OnEncryptDecryptTabSelected(ActionEvent event) {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
