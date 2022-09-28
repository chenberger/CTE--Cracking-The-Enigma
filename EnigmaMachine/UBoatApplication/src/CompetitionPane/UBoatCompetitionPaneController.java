package CompetitionPane;

import MainScene.MainUBoatScenePaneController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class UBoatCompetitionPaneController {
    private MainUBoatScenePaneController mainUBoatScenePaneController;
    @FXML private GridPane UBoatCompetitionPane;

    public void setMainUBoatScenePaneController(MainUBoatScenePaneController mainUBoatScenePaneController) {
        this.mainUBoatScenePaneController = mainUBoatScenePaneController;
    }

}
