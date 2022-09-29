package TopBorderPane;

import MainScene.MainUBoatScenePaneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


public class TopBorderPaneController {
    private MainUBoatScenePaneController mainUBoatScenePaneController;
    @FXML private GridPane topBorderPane;
    @FXML
     private Button loadMachineButton;
    @FXML private Label CurrentFilePathTextLabel;

    public void setMainUBoatScenePaneController(MainUBoatScenePaneController mainUBoatScenePaneController) {
        this.mainUBoatScenePaneController = mainUBoatScenePaneController;
    }
    @FXML private void onLoadMachineButtonClicked(ActionEvent event) {
        mainUBoatScenePaneController.loadMachine();
    }

    public void setMachineExists(boolean isMachineExists) {
        loadMachineButton.setDisable(isMachineExists);
    }

    public void setFileUploadedName(String name) {
        CurrentFilePathTextLabel.setText(name);
    }
}
