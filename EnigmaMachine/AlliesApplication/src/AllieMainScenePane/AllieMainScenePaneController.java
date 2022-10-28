package AllieMainScenePane;

import AllieLoginPane.AllieLoginPaneController;
import AllieMainScenePane.Body.ChatTabPane.chatroom.ChatRoomMainController;
import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.ContestData.IllegibleContestAmountChosenException;
import AllieMainScenePane.Body.DashBoardTabPane.DashboardTabPaneController;
import DTO.OnLineContestsTable;
import DesktopUserInterface.MainScene.ErrorDialog;
import Utils.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_OPS_SERVLET;
import static AlliesServletsPaths.AlliesServletsPaths.REGISTER_TO_BATTLE_SERVLET;
import static Constants.ServletConstants.USER_NAME;
import static UBoatServletsPaths.UBoatsServletsPaths.U_BOAT_LOGOUT_SERVLET;
import static Utils.Constants.*;

public class AllieMainScenePaneController {
    private final String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/AllieLoginPane/AllieLoginPane.fxml";
    @FXML
    private ScrollPane allyMainScenePane;
    private AllieLoginPaneController allyLoginPaneController;
    @FXML private Label allieHeaderLabel;
    @FXML private AnchorPane dashboardTabPane;
    @FXML private DashboardTabPaneController dashboardTabPaneController;
    @FXML private AnchorPane contestsTabPane;
    @FXML private ContestTabPaneController contestsTabPaneController;
    @FXML private Button registerToBattleButton;
    @FXML private Button readyToContestButton;
    @FXML private Button logOutButton;
    @FXML private Button setTaskSizeButton;
    @FXML private TextField taskSizeTextField;
    @FXML private BorderPane chatRoomPane;
    @FXML private ChatRoomMainController chatRoomPaneController;
    //private SimpleBooleanProperty isContestSet;
    private SimpleBooleanProperty isTaskSizeSet;
    private SimpleBooleanProperty isOneAgentAtLeastRegistered;
    private SimpleBooleanProperty isReadyButtonClicked;

    public AllieMainScenePaneController() {
        isTaskSizeSet = new SimpleBooleanProperty(false);
        //isContestSet = new SimpleBooleanProperty(false);
        isOneAgentAtLeastRegistered = new SimpleBooleanProperty(false);
        isReadyButtonClicked = new SimpleBooleanProperty(false);
    }

    public void initialize() {
        if(allyLoginPaneController != null) {
            allyLoginPaneController.setAllieMainScenePaneController(this);
        }
        if(dashboardTabPaneController != null) {
            dashboardTabPaneController.setAllieMainScenePaneController(this);
        }
        if(contestsTabPaneController != null) {
            contestsTabPaneController.setAllieMainScenePaneController(this);
        }

        //registerToBattleButton.disableProperty().bind(isContestSet);
        taskSizeTextField.disableProperty().bind(isTaskSizeSet);
        setTaskSizeButton.disableProperty().bind(isTaskSizeSet);
        readyToContestButton.disableProperty().bind(dashboardTabPaneController.getContestSetProperty().not().or(isTaskSizeSet.not()).or(isOneAgentAtLeastRegistered.not()).or(isReadyButtonClicked));
git         setAllyName();
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
                Platform.runLater(() -> allieHeaderLabel.setText("Ally - " + userName));
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
    public void onRegisterToBattleButtonClicked(ActionEvent actionEvent) {
        try {
            String chosenContestName = dashboardTabPaneController.getSelectedContest();
            String finalUrl = HttpUrl.parse(REGISTER_TO_BATTLE_SERVLET)
                    .newBuilder()
                    .addQueryParameter(USER_NAME, chosenContestName)
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    new ErrorDialog(e, "Error");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == 200) {
                        String body = response.body().string();
                        OnLineContestsTable chosenContest = onLineContestFromJson(body);
                        setChosenContest(chosenContest);
                        Platform.runLater(() -> {
                            dashboardTabPaneController.setContest(true);
                            //isContestSet.set(true);
                            contestsTabPaneController.cleanPreviousContestData();
                        });
                    } else {
                        Platform.runLater(() -> {
                            new ErrorDialog(new Exception("Error registering to battle"), "Error");
                        });
                    }
                }
            });
        }

        catch(IllegibleContestAmountChosenException ex) {
            Platform.runLater(() -> {
                new ErrorDialog(ex, "Error: Failed to register to contest");
            });
        }
    }
    public static OnLineContestsTable onLineContestFromJson(String jsonChosenContest) {
        JsonObject jsonObject = JsonParser.parseString(jsonChosenContest).getAsJsonObject();
        String battleName = jsonObject.get("battleName").getAsString();
        String boatName = jsonObject.get("boatName").getAsString();
        String contestStatus = jsonObject.get("contestStatus").getAsString();
        String difficulty = jsonObject.get("difficulty").getAsString();
        String teamsRegisteredAndNeeded = jsonObject.get("teamsRegisteredAndNeeded").getAsString();
        return  new OnLineContestsTable(battleName, boatName, contestStatus, difficulty, teamsRegisteredAndNeeded);
    }

    public String getSelectedContest() throws IllegibleContestAmountChosenException {
        return dashboardTabPaneController.getSelectedContest();
    }
    public void onSetTaskSizeButtonClicked(ActionEvent actionEvent) {
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
                isTaskSizeSet.set(true);
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
                    Platform.runLater(() -> {
                        //setTaskSizeButton.setDisable(true);
                        //readyToContestButton.setDisable(true);
                    });
                }
                else{
                    new ErrorDialog(new Exception("Error setting task size"), "Error");
                    taskSizeTextField.setText("");
                }
            }
        });
    }

    public void close() {
        Platform.exit();
    }

    public void setChosenContest(OnLineContestsTable chosenContest) {
        contestsTabPaneController.setChosenContest(chosenContest);
        dashboardTabPaneController.setContest(true);
        //isContestSet.set(true);
    }

    public void onReadyToContestButtonClicked(ActionEvent actionEvent) {
        String finalUrl = HttpUrl.parse(READY_MANAGER_SERVLET)
                .newBuilder()
                .addQueryParameter(TYPE, "Allies")
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
                    contestsTabPaneController.setNoneWinnerFound();
                    isReadyButtonClicked.set(true);
                    contestsTabPaneController.setIsReadyToBattle();
                    //contestsTabPaneController.startRefresh();


/*                    Platform.runLater(() -> {
                        readyToContestButton.setDisable(true);
                    });*/
                    //String body = response.body().string();
                }
                else{
                    new ErrorDialog(new Exception("Error setting Ready"), "Error");
                }
                response.close();
            }
        });

    }

    public void onLogOutButtonClicked(ActionEvent actionEvent) {
        String finalUrl = HttpUrl.parse(U_BOAT_LOGOUT_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "alliesLogout")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    new ErrorDialog(new Exception("Error logging out"), "Error");
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    contestsTabPaneController.close();
                    dashboardTabPaneController.close();
                    chatRoomPaneController.close();
                    Platform.runLater(()->{
                        contestsTabPaneController.setNotReadyToBattle();
                        new ErrorDialog(new Exception("Logged out successfully"), "Logged out");
                        HttpClientUtil.removeCookiesOf("localhost");
                        loadLoginPage();
                    });
;
                }
                else{
                    new ErrorDialog(new Exception("Error logging out"), "Error");
                }
            }
        });
    }

    private void loadLoginPage() {
        Scene scene = allyMainScenePane.getScene();
        URL url = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            Parent root = fxmlLoader.load();
            allyLoginPaneController = fxmlLoader.getController();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moreThenOneAgentJoined() {
        isOneAgentAtLeastRegistered.set(true);
    }

    public void resetControllers() {
        Platform.runLater(() -> {
            taskSizeTextField.setText("");
            isTaskSizeSet.set(false);
            //isContestSet.set(false);
            dashboardTabPaneController.setContest(false);
            isReadyButtonClicked.set(false);
        });
    }

    public void unsetContest() {
        //isContestSet.set(false);
        dashboardTabPaneController.setContest(false);
        isTaskSizeSet.set(false);
        //taskSizeTextField.setDisable(true);
    }

    public void noAgentsJoined() {
        isOneAgentAtLeastRegistered.set(false);
    }
}
