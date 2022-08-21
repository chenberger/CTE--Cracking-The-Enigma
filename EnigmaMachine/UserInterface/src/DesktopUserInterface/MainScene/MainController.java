package DesktopUserInterface.MainScene;

import DesktopUserInterface.MainScene.TopScene.TopBorderPaneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MainController {
    @FXML public TabPane tabPane;

    @FXML private BorderPane borderPane;
    @FXML private TopBorderPaneController topBorderPaneController;
    @FXML private GridPane topBorderPane;
    @FXML private Tab machineTab;
    @FXML private Tab encryptDecryptTab;
    @FXML private Tab bruteForceTab;



    @FXML
    public void initialize() {
        if(topBorderPaneController != null) {
            topBorderPaneController.setMainController(this);
        }

/*        if(machineTabController != null) {
            machineTabController.setMainController(this);
        }

        if(encryptDecryptTabController != null) {
            encryptDecryptTabController.setMainController(this);
        }

        if(bruteForceTabController != null) {
            bruteForceTabController.setMainController(this);
        }*/
    }

    public void OnMachineTabSelected(Event event) {
    }

    public void OnEncryptDecryptTabSelected(Event event) {
    }

    public void OnBruteForceTabSelected(Event event) {
    }
}
