package DesktopUserInterface.MainScene;

import DesktopUserInterface.MainScene.BodyScene.BruteForceTabController;
import DesktopUserInterface.MainScene.BodyScene.EncryptDecryptTabController;
import DesktopUserInterface.MainScene.BodyScene.MachineTabController;
import DesktopUserInterface.MainScene.TopScene.TopBorderPaneController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MainController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TopBorderPaneController topBorderPaneController;
    @FXML
    private GridPane topBorderPane;
    @FXML
    private Tab machineTab;
    @FXML
    private MachineTabController machineTabController;
    @FXML
    private Tab encryptDecryptTab;
    @FXML
    private EncryptDecryptTabController encryptDecryptTabController;
    @FXML
    private Tab bruteForceTab;
    @FXML
    private BruteForceTabController bruteForceTabController;

    @FXML
    public void initialize() {
        if(topBorderPaneController != null) {
            topBorderPaneController.setMainController(this);
        }

        if(machineTabController != null) {
            machineTabController.setMainController(this);
        }

        if(encryptDecryptTabController != null) {
            encryptDecryptTabController.setMainController(this);
        }

        if(bruteForceTabController != null) {
            bruteForceTabController.setMainController(this);
        }
    }
}
