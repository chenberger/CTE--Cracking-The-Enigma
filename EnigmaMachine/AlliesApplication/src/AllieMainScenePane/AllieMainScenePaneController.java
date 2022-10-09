package AllieMainScenePane;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import DTO.OnLineContestsTable;
import AllieMainScenePane.Body.DashBoardTabPane.DashboardTabPaneController;
import DesktopUserInterface.MainScene.ErrorDialog;
import Utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static Utils.Constants.GET_USER_NAME_SERVLET;

public class AllieMainScenePaneController {
    @FXML private Label allieHeaderLabel;
    @FXML private AnchorPane dashboardTabPane;
    @FXML private DashboardTabPaneController dashboardTabPaneController;
    @FXML private AnchorPane contestsTabPane;
    @FXML private ContestTabPaneController contestsTabPaneController;

    public void initialize() {
        if(dashboardTabPaneController != null) {
            dashboardTabPaneController.setAllieMainScenePaneController(this);
        }
        if(contestsTabPaneController != null) {
            contestsTabPaneController.setAllieMainScenePaneController(this);
        }
        setAllyName();
    }

    private void setAllyName() {
        String finalUrl = HttpUrl.parse(GET_USER_NAME_SERVLET).
                newBuilder().
                addQueryParameter("action", "getAllyName").
                build().
                toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(new Exception("Failed to get user name"), "Failed to get user name");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String userName = response.body().string();
                allieHeaderLabel.setText("Ally - " + userName);
            }
        });
    }

    public void setActive() {
        dashboardTabPaneController.setActive();
        contestsTabPaneController.setActive();

    }

    @FXML public void onDashboardButtonClicked(ActionEvent actionEvent) {
        dashboardTabPane.setVisible(true);
        contestsTabPane.setVisible(false);

    }

    @FXML public void onContestButtonClicked(ActionEvent actionEvent) {
        dashboardTabPane.setVisible(false);
        contestsTabPane.setVisible(true);
    }

    public void close() {
        Platform.exit();
    }

    public void setChosenContest(OnLineContestsTable chosenContest) {
        contestsTabPaneController.setChosenContest(chosenContest);
    }
}
