package AllieMainScenePane.Body.ContestTabPane.AgentsProgressData;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import DTO.AgentProgressDataToTable;
import DTO.AlliesTasksProgressToLabels;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;

public class AgentsProgressDataController implements Closeable {
    Timer timer;
    AgentsProgressDataRefresher agentsProgressDataRefresher;

    private ContestTabPaneController contestTabPaneController;
    @FXML
    private TableView<?> agentProgressDataTableView;

    @FXML
    private TableColumn<?, ?> agentNameCol;

    @FXML
    private TableColumn<?, ?> tasksPulledCol;

    @FXML
    private TableColumn<?, ?> totalCandidatesCol;

    @FXML
    private Label totalTasksOfAllyLabel;

    @FXML
    private Label tasksProducedLabel;

    @FXML
    private Label TasksCompletedLabel;
    public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
        this.contestTabPaneController = contestTabPaneController;
    }
    public void startListRefreshing(){
        agentsProgressDataRefresher = new AgentsProgressDataRefresher(
                this::updateAgentsProgressDataTable,
                this::updateAlliesTasksProgressToLabels);
        timer = new Timer();
        timer.schedule(agentsProgressDataRefresher, 0, 100);
    }
    private void updateAgentsProgressDataTable(List<AgentProgressDataToTable> agentProgressDataToTableList){
        Platform.runLater(()->{

        });
    }
    private void updateAlliesTasksProgressToLabels(AlliesTasksProgressToLabels alliesTasksProgressToLabels){
        Platform.runLater(()->{
            totalTasksOfAllyLabel.setText(alliesTasksProgressToLabels.getTotalTasks().toString());
            tasksProducedLabel.setText(alliesTasksProgressToLabels.getTasksPulled().toString());
            TasksCompletedLabel.setText(alliesTasksProgressToLabels.getTasksDone().toString());
        });
    }

    @Override
    public void close() {

    }
}
