package AllieMainScenePane.Body.ContestTabPane;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.ContestTabPane.AgentsProgressData.AgentsProgressDataController;
import AllieMainScenePane.Body.ContestTabPane.ContestTeams.ContestTeamsController;
import AllieMainScenePane.Body.ContestTabPane.CurrentContest.CurrentContestDataPaneController;
import DTO.OnLineContestsTable;
import DesktopUserInterface.MainScene.ErrorDialog;
import Utils.HttpClientUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static UBoatServletsPaths.UBoatsServletsPaths.U_BOATS_LIST_SERVLET;

public class ContestTabPaneController {

    @FXML private AnchorPane currentContestDataPane;
    @FXML private CurrentContestDataPaneController currentContestDataPaneController;
    //@FXML private AnchorPane candidatesInformationPane;
    //@FXML private UBoatCandidatesPaneController candidatesInformationPaneController;
    @FXML private AnchorPane agentsProgressAndDataPane;
    @FXML private AgentsProgressDataController agentsProgressAndDataPaneController;
    @FXML private AnchorPane contestTeamsPane;
    @FXML private ContestTeamsController contestTeamsPaneController;
    private AllieMainScenePaneController allieMainScenePaneController;
    public void initialize(){
        if(currentContestDataPaneController != null) {
          currentContestDataPaneController.setContestTabPaneController(this);
        }
        //if (candidatesInformationPaneController != null) {
        //    candidatesInformationPaneController.setContestTabPaneController(this);
        //}
        if(agentsProgressAndDataPaneController != null) {
            agentsProgressAndDataPaneController.setContestTabPaneController(this);
        }
        if (contestTeamsPaneController != null) {
            contestTeamsPaneController.setContestTabPaneController(this);
        }

    }
    public void setAllieMainScenePaneController(AllieMainScenePaneController allieMainScenePaneController) {
        this.allieMainScenePaneController = allieMainScenePaneController;
    }

    public void close() {
        if(currentContestDataPaneController != null) {
            currentContestDataPaneController.close();
        }

        allieMainScenePaneController.close();
    }

    public void setActive() {
        contestTeamsPaneController.startListRefresher();
        currentContestDataPaneController.startListRefresher();
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
}
