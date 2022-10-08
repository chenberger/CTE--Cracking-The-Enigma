package AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData;

import AllieMainScenePane.Body.DashBoardTabPane.DashboardTabPaneController;
import DTO.AgentsToTable;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;

import static Utils.Constants.REFRESH_RATE;

public class TeamAgentsDataPaneController implements Closeable {
    private DashboardTabPaneController dashboardTabPaneController;
    @FXML private TableView<AgentsTable> agentsTableView;

    @FXML private TableColumn<AgentsTable, String> agentNameCol;
    @FXML private TableColumn<AgentsTable, Long> numOfThreadsCol;
    @FXML private TableColumn<AgentsTable, Long> tasksPullingInEachTimeCol;
    private final IntegerProperty totalAgents = new SimpleIntegerProperty();
    private TeamAgentsListRefresher teamAgentsListRefresher;
    private SimpleBooleanProperty autoUpdate = new SimpleBooleanProperty();
    private Timer timer;
    @FXML public void initialize(){
        initializeAgentsInTeamTable();
    }

    public void startListRefresher() {
        teamAgentsListRefresher = new TeamAgentsListRefresher(
                autoUpdate,
                this::updateAgentsTable);
        timer = new Timer();
        timer.schedule(teamAgentsListRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    private void initializeAgentsInTeamTable() {
        agentNameCol.setCellValueFactory(new PropertyValueFactory<AgentsTable, String>("Agent Name"));
        numOfThreadsCol.setCellValueFactory(new PropertyValueFactory<AgentsTable, Long>("Number Of Threads"));
        tasksPullingInEachTimeCol.setCellValueFactory(new PropertyValueFactory<AgentsTable, Long>("Task Size"));
    }
    private void updateAgentsTable(AgentsToTable agentsToTable) {
        Platform.runLater(() -> {
            clearAgentsTable();
            List<String> agents = agentsToTable.getAgents();
            List<Long> threadsForEachAgent = agentsToTable.getThreadsForEachAgent();
            List<Long> tasksTakenOnceForEachAgent = agentsToTable.getTasksTakenOnceForEachAgent();

            for (int i = 0; i < agents.size(); i++) {
                AgentsTable agentToAdd = new AgentsTable(agents.get(i), threadsForEachAgent.get(i), tasksTakenOnceForEachAgent.get(i));
                ObservableList<AgentsTable> tableData = agentsTableView.getItems();
                tableData.add(agentToAdd);
                agentsTableView.setItems(tableData);
            }
        });
    }

    private void clearAgentsTable() {
        agentsTableView.getItems().clear();
    }


    @Override
    public void close() {
        agentsTableView.getItems().clear();
        totalAgents.set(0);
        if (teamAgentsListRefresher != null && timer != null) {
            teamAgentsListRefresher.cancel();
            timer.cancel();
        }
    }

    public void setDashboardTabPaneController(DashboardTabPaneController dashboardTabPaneController) {
        this.dashboardTabPaneController = dashboardTabPaneController;
    }
}
