package DesktopUserInterface.MainScene.TopScene;

import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class TopBorderPaneController {
    @FXML
    private Button LoadMachineButton;
    @FXML
    private Label CurrentFilePathTextField;
    @FXML
    private Label EnigmaMachineHeadlineTextField;
    private MainController mainController;
    private EngineManager engineManager;

    public TopBorderPaneController(){
        engineManager = new EngineManager();
    }

    @FXML
    void OnLoadMachineButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
