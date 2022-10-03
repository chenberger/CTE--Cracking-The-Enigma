package Body.DashBoardTabPane.TeamAgentsData;

import Body.DashBoardTabPane.DashboardTabPaneController;
import CompetitionPane.AlliesInBattleRefresher;
import DTO.AgentsToTable;
import DTO.AlliesToTable;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableView;

import java.io.Closeable;
import java.util.Timer;
import java.util.TimerTask;

import static Utils.Constants.REFRESH_RATE;

public class TeamAgentsDataPaneController implements Closeable {
    private DashboardTabPaneController dashboardTabPaneController;
    private TableView<AlliesToTable> agentsTableView;
    private final IntegerProperty totalAgents = new SimpleIntegerProperty();
    private TimerTask teamAgentsListRefresher;
    private SimpleBooleanProperty autoUpdate = new SimpleBooleanProperty();
    private Timer timer;

    public void startListRefresher() {
        teamAgentsListRefresher = new TeamAgentsListRefresher(
                autoUpdate,
                this::updateAgentsTable);
        timer = new Timer();
        timer.schedule(teamAgentsListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateAgentsTable(AgentsToTable agentsToTable) {
        Platform.runLater(() -> {
            //TODO chen/erez: update agents table
        });
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
