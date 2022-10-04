package AllieMainScenePane.Body.DashBoardTabPane;

import AllieMainScenePane.Body.DashBoardTabPane.ContestData.ContestDataPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData.TeamAgentsDataPaneController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class DashboardTabPaneController {

    @FXML private AnchorPane teamAgentsDataPane;
    @FXML private TeamAgentsDataPaneController teamAgentsDataPaneController;

    @FXML private AnchorPane contestsDataPane;
    @FXML private ContestDataPaneController contestsDataPaneController;

    public void initialize() {
        if(teamAgentsDataPaneController != null) {
            teamAgentsDataPaneController.setDashboardTabPaneController(this);
        }
        if(contestsDataPaneController != null) {
            contestsDataPaneController.setDashboardTabPaneController(this);
        }

    }
}
