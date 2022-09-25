package MainScene;

import Engine.EngineManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MainUBoatScenePaneController {

    @FXML private GridPane UBoatLoginPane;
    @FXML private LoginPane.UBoatLoginPaneController UBoatLoginPaneController;

    public static void setEnigmaEngine(EngineManager engineManager) {

    }

    public void initialize() {
        if (UBoatLoginPane != null) {
            UBoatLoginPaneController.setMainUBoatScenePaneController(this);
        }
    }
}
