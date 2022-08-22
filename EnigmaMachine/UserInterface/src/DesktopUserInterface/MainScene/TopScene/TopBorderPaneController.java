package DesktopUserInterface.MainScene.TopScene;

import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
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
    private EngineManager enigmaMachineEngine;

    @FXML
    void OnLoadMachineButtonClicked(ActionEvent event) {
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
}
