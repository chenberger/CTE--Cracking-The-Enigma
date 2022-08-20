package DesktopUserInterface.MainScene.BodyScene;

import DesktopUserInterface.MainScene.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MachineTabController {
    @FXML
    private Tab MachineTab;
    private MainController mainController;
    @FXML
    void OnMachineTabSelected(ActionEvent event) {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
