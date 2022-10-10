package AllieMainScenePane.Body.DashBoardTabPane;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.DashBoardTabPane.ContestData.ContestDataPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.ContestData.IllegibleContestAmountChosenException;
import AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData.TeamAgentsDataPaneController;
import DTO.OnLineContestsTable;
import DesktopUserInterface.MainScene.ErrorDialog;
import Utils.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static AlliesServletsPaths.AlliesServletsPaths.REGISTER_TO_BATTLE_SERVLET;
import static Constants.ServletConstants.USER_NAME;
import static Utils.Constants.*;

public class DashboardTabPaneController {

    private AllieMainScenePaneController allieMainScenePaneController;
    @FXML private AnchorPane teamAgentsDataPane;
    @FXML private TeamAgentsDataPaneController teamAgentsDataPaneController;

    @FXML private AnchorPane contestsDataPane;
    @FXML private ContestDataPaneController contestsDataPaneController;

    //TODO: Erez: get the name of the boat from the line the user marked(when he clicked the register button).
    public void initialize() {
        if(teamAgentsDataPaneController != null) {
            teamAgentsDataPaneController.setDashboardTabPaneController(this);
        }
        if(contestsDataPaneController != null) {
            contestsDataPaneController.setDashboardTabPaneController(this);
        }
        setActive();
    }

    public void setAllieMainScenePaneController(AllieMainScenePaneController allieMainScenePaneController) {
        this.allieMainScenePaneController = allieMainScenePaneController;
    }

    public void setActive() {
        teamAgentsDataPaneController.startListRefresher();
        contestsDataPaneController.startListRefresher();

    }


    public String getSelectedContest() throws IllegibleContestAmountChosenException {
        return contestsDataPaneController.getSelectedContest().getBoatName();
    }

    public void close() {
        teamAgentsDataPaneController.close();
        contestsDataPaneController.close();
    }
}
