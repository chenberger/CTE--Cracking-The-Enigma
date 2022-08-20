package DesktopUserInterface.MainScene.TopScene;

import DesktopUserInterface.MainScene.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TopBorderPaneController {
    @FXML
    private Button LoadMachineButton;
    @FXML
    private Label CurrentFilePathTextField;
    @FXML
    private Label EnigmaMachineHeadlineTextField;
    private MainController mainController;

    @FXML
    void OnLoadMachineButtonClicked(ActionEvent event) {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
