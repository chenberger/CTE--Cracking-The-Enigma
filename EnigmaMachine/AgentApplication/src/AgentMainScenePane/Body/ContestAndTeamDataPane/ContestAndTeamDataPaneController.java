package AgentMainScenePane.Body.ContestAndTeamDataPane;

import AgentMainScenePane.AgentMainScenePaneController;
import DTO.AgentContestAndTeamData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.Closeable;
import java.util.Timer;

import static Utils.Constants.REFRESH_RATE;

public class ContestAndTeamDataPaneController implements Closeable {
    private final String baseCurrentTeamLabelText = "Team: ";
    private final String baseCurrentBattleLabelText = "Battle: ";
    private final String baseContestStatusLabelText = "Contest Status: ";

    private ContestAndTeamDataRefresher contestAndTeamDataRefresher;
    private Timer timer;
    @FXML
    private Label currentBattleLabel;

    @FXML
    private Label contestStatusLabel;

    @FXML
    private Label teamLabel;
    private AgentMainScenePaneController agentMainSceneController;

    public void setAgentMainSceneController(AgentMainScenePaneController agentMainScenePaneController) {
        this.agentMainSceneController = agentMainScenePaneController;
    }
    private void updateContestAndTeamData(AgentContestAndTeamData agentContestAndTeamData) {
        Platform.runLater(() -> {
            currentBattleLabel.setText(baseContestStatusLabelText + " " + agentContestAndTeamData.getContestStatus());
            contestStatusLabel.setText(baseCurrentBattleLabelText + " " +  agentContestAndTeamData.getBattleName());
            teamLabel.setText(baseCurrentTeamLabelText + " " + agentContestAndTeamData.getTeamName());
        });
    }

    public void startRefreshing() {
        contestAndTeamDataRefresher = new ContestAndTeamDataRefresher(this::updateContestAndTeamData);
        timer = new Timer();
        timer.schedule(contestAndTeamDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @Override
    public void close() {
        if(contestAndTeamDataRefresher != null) {
            contestAndTeamDataRefresher.cancel();
        }
        timer.cancel();
    }
}
