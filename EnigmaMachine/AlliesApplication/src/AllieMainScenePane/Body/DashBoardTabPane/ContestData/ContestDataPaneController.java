package AllieMainScenePane.Body.DashBoardTabPane.ContestData;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.DashBoardTabPane.DashboardTabPaneController;
import DTO.OnLineContestsDataToTable;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ContestDataPaneController {
    private AllieMainScenePaneController allieMainScenePaneController;
    private DashboardTabPaneController dashboardTabPaneController;
    @FXML private TableView<OnLineContestsDataToTable> contestsTable;
    @FXML private TableColumn<OnLineContestsDataToTable, String> battleNameCol;
    @FXML private TableColumn<OnLineContestsDataToTable, String> boatNameCol;
    @FXML private TableColumn<OnLineContestsDataToTable, String> contestStatusCol;
    @FXML private TableColumn<OnLineContestsDataToTable, String> difficultyCol;
    @FXML private TableColumn<OnLineContestsDataToTable, String> teamsRegisteredAndNeededCol;



    public void setDashboardTabPaneController(DashboardTabPaneController dashboardTabPaneController) {
        this.dashboardTabPaneController = dashboardTabPaneController;
    }

    public void onRowOfTableMouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() > 0) {
            
        }
    }


}
