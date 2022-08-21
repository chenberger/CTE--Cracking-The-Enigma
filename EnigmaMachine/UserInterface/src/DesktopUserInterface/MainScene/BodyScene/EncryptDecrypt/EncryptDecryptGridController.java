package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class EncryptDecryptGridController {
    @FXML
    private GridPane EncryptDecryptGrid;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
