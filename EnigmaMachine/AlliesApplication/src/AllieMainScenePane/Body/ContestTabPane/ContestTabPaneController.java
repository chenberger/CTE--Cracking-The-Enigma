package AllieMainScenePane.Body.ContestTabPane;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.ContestTabPane.AgentsProgressData.AgentsProgressDataController;
import AllieMainScenePane.Body.ContestTabPane.ContestTeams.ContestTeamsController;
import AllieMainScenePane.Body.ContestTabPane.CurrentContest.CurrentContestDataPaneController;
import AllieMainScenePane.Body.ContestTabPane.TeamCandidates.TeamCandidatesController;
import DTO.OnLineContestsTable;
import DesktopUserInterface.MainScene.ErrorDialog;
import Utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;

import static UBoatServletsPaths.UBoatsServletsPaths.U_BOATS_LIST_SERVLET;

public class ContestTabPaneController implements Closeable {
    private boolean isWinnerFound = false;
    private Timer timer;
    private LookForWinnerRefresher lookForWinnerRefresher;
    @FXML private AnchorPane teamCandidates;
    @FXML private TeamCandidatesController teamCandidatesController;

    @FXML private AnchorPane currentContestDataPane;
    @FXML private CurrentContestDataPaneController currentContestDataPaneController;

    @FXML private AnchorPane agentsProgressDataPane;
    @FXML private AgentsProgressDataController agentsProgressDataPaneController;
    @FXML private AnchorPane contestTeamsPane;
    @FXML private ContestTeamsController contestTeamsPaneController;
    private AllieMainScenePaneController allieMainScenePaneController;
    public void initialize(){
        if(currentContestDataPaneController != null) {
          currentContestDataPaneController.setContestTabPaneController(this);
        }
        if(agentsProgressDataPaneController != null) {
            agentsProgressDataPaneController.setContestTabPaneController(this);
        }
        if (contestTeamsPaneController != null) {
            contestTeamsPaneController.setContestTabPaneController(this);
        }
        if(teamCandidatesController != null) {
            teamCandidatesController.setContestTabPaneController(this);
        }

    }
    public void setAllieMainScenePaneController(AllieMainScenePaneController allieMainScenePaneController) {
        this.allieMainScenePaneController = allieMainScenePaneController;
    }

    public void closeActivity() {
        if(currentContestDataPaneController != null) {
            currentContestDataPaneController.close();
        }

        allieMainScenePaneController.close();
    }

    public void setActive() {
        contestTeamsPaneController.startListRefresher();
        currentContestDataPaneController.startListRefresher();
        agentsProgressDataPaneController.startListRefreshing();
    }

    public void setChosenContest(OnLineContestsTable chosenContest) {
        String finalUrl = HttpUrl.parse(U_BOATS_LIST_SERVLET).
                newBuilder().
                addQueryParameter("action", "getUBoatName").
                build().
                toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(new Exception("Something went wrong: " + e.getMessage()), "Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != 200) {
                    new ErrorDialog(new Exception("Something went wrong: " + response.message()), "Error");
                }
                else {
                    String uBoatName = response.body().string();
                    currentContestDataPaneController.setChosenContest(chosenContest, uBoatName);
                    contestTeamsPaneController.setShouldUpdateToTrue();
                    //candidatesInformationPaneController.setChosenContest(chosenContest, uBoatName);

                }
            }
        });
    }

    public void startRefresh() {
        teamCandidatesController.startListRefreshing();
            LookForWinnerRefresher lookForWinnerRefresher = new LookForWinnerRefresher(this::notifyIfWinnerFound);
            timer = new Timer();
            timer.schedule(lookForWinnerRefresher, 0, 100);
    }
    @Override
    public void close(){
        if(!isWinnerFound) {
            if (timer != null) {
                timer.cancel();
            }
            if (lookForWinnerRefresher != null) {
                lookForWinnerRefresher.cancel();
            }
            if (currentContestDataPaneController != null) {
                currentContestDataPaneController.close();
            }
            if (agentsProgressDataPaneController != null) {
                agentsProgressDataPaneController.close();
            }
            if (contestTeamsPaneController != null) {
                contestTeamsPaneController.close();
            }
            if (teamCandidatesController != null) {
                teamCandidatesController.close();
            }
        }
    }
    private void notifyIfWinnerFound(String winnerMessage) {
        if(!isWinnerFound) {
            isWinnerFound = true;
            Platform.runLater(() -> {
                if (winnerMessage != null) {
                    new ErrorDialog(new Exception(winnerMessage), "The contest is over");
                }
            });
            close();
            allieMainScenePaneController.resetControllers();
        }
    }
}
