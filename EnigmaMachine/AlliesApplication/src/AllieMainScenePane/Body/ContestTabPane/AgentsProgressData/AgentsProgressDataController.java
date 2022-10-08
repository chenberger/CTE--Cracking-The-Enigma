package AllieMainScenePane.Body.ContestTabPane.AgentsProgressData;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import DTO.AgentsProgressAndDataTable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AgentsProgressDataController {
    private ContestTabPaneController contestTabPaneController;
    private TableView<AgentsProgressAndDataTable> agentsProgressAndDataTable;
    private TableColumn<AgentsProgressAndDataTable, String> agentNameCol;
    private TableColumn<AgentsProgressAndDataTable, String> tasksProducedCol;
    private TableColumn<AgentsProgressAndDataTable, String> tasksProcessedCol;
    public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
        this.contestTabPaneController = contestTabPaneController;
    }
}
