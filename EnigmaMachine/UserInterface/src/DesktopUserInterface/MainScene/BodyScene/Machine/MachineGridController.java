package DesktopUserInterface.MainScene.BodyScene.Machine;

import DesktopUserInterface.MainScene.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MachineGridController {
    @FXML
    private GridPane machineGrid;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
