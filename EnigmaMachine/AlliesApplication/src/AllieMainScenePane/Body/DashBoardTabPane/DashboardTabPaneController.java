package AllieMainScenePane.Body.DashBoardTabPane;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.DashBoardTabPane.ContestData.ContestDataPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData.TeamAgentsDataPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData.TeamAgentsListRefresher;
import DesktopUserInterface.MainScene.ErrorDialog;
import MainScene.CompetitionPane.AlliesInBattleRefresher;
import Utils.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
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

import java.awt.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_OPS_SERVLET;
import static Utils.Constants.*;

public class DashboardTabPaneController {

    private AllieMainScenePaneController allieMainScenePaneController;
    @FXML private AnchorPane teamAgentsDataPane;
    @FXML private TeamAgentsDataPaneController teamAgentsDataPaneController;

    @FXML private AnchorPane contestsDataPane;
    @FXML private ContestDataPaneController contestsDataPaneController;
    @FXML private Button registerToBattleButton;
    @FXML private Button setTaskSizeButton;
    @FXML private TextField taskSizeTextField;

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
    }

    public void onSetTaskSizeButton(ActionEvent actionEvent) {
        if(taskSizeTextField.getText().isEmpty()) {
            return;
        }
        try {
            long taskSize = Long.parseLong(taskSizeTextField.getText());
            if(taskSize < 1) {
                new ErrorDialog(new Exception("Task size must be positive number"),"Error");
            }
            else{
                setAllyTaskSize(taskSize);
            }
        } catch (NumberFormatException e) {
            new ErrorDialog(new Exception("Task size must be a number"), "Error");
            taskSizeTextField.setText("");
        }
    }

    private void setAllyTaskSize(long taskSize) {
        String finalUrl = HttpUrl.parse(ALLIES_OPS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, SET_TASK_SIZE)
                .addQueryParameter(TASK_SIZE, String.valueOf(taskSize))
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(e, "Error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    taskSizeTextField.setText("");
                    setTaskSizeButton.setDisable(true);
                }
                else{
                    new ErrorDialog(new Exception("Error setting task size"), "Error");
                    taskSizeTextField.setText("");
                }
            }
        });
    }

    public void onRegisterToBattleButton(ActionEvent actionEvent) {

    }
}
