package AllieMainScenePane.Body.DashBoardTabPane;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.DashBoardTabPane.ContestData.ContestDataPaneController;
import DTO.OnLineContestsTable;
import AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData.TeamAgentsDataPaneController;
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

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_OPS_SERVLET;
import static AlliesServletsPaths.AlliesServletsPaths.REGISTER_TO_BATTLE_SERVLET;
import static Constants.ServletConstants.USER_NAME;
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
        String finalUrl = HttpUrl.parse(REGISTER_TO_BATTLE_SERVLET)
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

    public void onRegisterToBattleButtonClicked(ActionEvent actionEvent) {
        String finalUrl = HttpUrl.parse(REGISTER_TO_BATTLE_SERVLET)
                .newBuilder()
                .addQueryParameter(USER_NAME, "chen")
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
                    registerToBattleButton.setDisable(true);
                    setTaskSizeButton.setDisable(false);
                    String body = response.body().string();
                    OnLineContestsTable chosenContest = onLineContestFromJson(body);
                    allieMainScenePaneController.setChosenContest(chosenContest);
                }
                else{
                    new ErrorDialog(new Exception("Error registering to battle"), "Error");
                }
            }
        });
    }

    private OnLineContestsTable onLineContestFromJson(String jsonChosenContest) {
        JsonObject jsonObject = JsonParser.parseString(jsonChosenContest).getAsJsonObject();
        String battleName = jsonObject.get("battleName").getAsString();
        String boatName = jsonObject.get("boatName").getAsString();
        String contestStatus = jsonObject.get("contestStatus").getAsString();
        String difficulty = jsonObject.get("difficulty").getAsString();
        String teamsRegisteredAndNeeded = jsonObject.get("teamsRegisteredAndNeeded").getAsString();
        return  new OnLineContestsTable(battleName, boatName, contestStatus, difficulty, teamsRegisteredAndNeeded);

    }
}
