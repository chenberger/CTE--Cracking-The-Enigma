package CompetitionPane;

import Api.UpdateHttpLine;
import CandidatesPane.UBoatCandidatesPaneController;
import CurrentCodeConfigurationPane.CurrentCodeConfigurationController;
import CodeCalibrationPane.CodeCalibrationController;
import DTO.AlliesToTable;
import DTO.MachineDetails;
import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.AlliesManager.Allie;
import Engine.UBoatManager.UBoat;
import EnigmaMachine.Settings.SettingsFormat;
import MainScene.MainUBoatScenePaneController;
import UBoatServletsPaths.UBoatsServletsPaths;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static UBoatServletsPaths.UBoatsServletsPaths.GET_MACHINE_CONFIG_SERVLET;
import static Utils.Constants.*;

public class UBoatCompetitionPaneController implements Closeable {

    String UBoatName;
    private Timer timer;
    private TimerTask alliesInBattleRefresher;
    private final BooleanProperty autoUpdate;
    private final IntegerProperty totalUsers;
    private UpdateHttpLine.HttpStatusUpdate httpStatusUpdate;
    private MainUBoatScenePaneController mainUBoatScenePaneController;
    @FXML private Button logOutButton;
    @FXML private Button readyButton;
    @FXML private GridPane codeCalibration;
    @FXML private CodeCalibrationController codeCalibrationController;
    @FXML private VBox currentCodeConfigurationPane;
    @FXML private CurrentCodeConfigurationController currentCodeConfigurationPaneController;
    @FXML private AnchorPane uBoatCandidatesPane;
    @FXML private UBoatCandidatesPaneController uBoatCandidatesPaneController;
    @FXML private GridPane UBoatCompetitionPane;
    @FXML private GridPane encryptDecryptActionsGrid;
    @FXML private EncryptDecryptActionsPane.EncryptDecryptActionsGridController encryptDecryptActionsGridController;

    @FXML private TableView<List<String>> teamsTableView;
    @FXML private javafx.scene.control.TableColumn<List<String>, String> teamNameCol;
    @FXML private javafx.scene.control.TableColumn<List<String>, String> numOfAgentsCol;
    @FXML private javafx.scene.control.TableColumn<List<String>, String> taskSizeCol;
    private SimpleBooleanProperty isReady;

    @FXML public void initialize(){
        if(encryptDecryptActionsGridController != null){
            encryptDecryptActionsGridController.setUBoatCompetitionPaneController(this);
        }
        if(currentCodeConfigurationPaneController != null){
            currentCodeConfigurationPaneController.setUBoatCompetitionPaneController(this);
        }
        if(uBoatCandidatesPaneController != null){
            uBoatCandidatesPaneController.setUBoatCompetitionPaneController(this);
        }
        if(codeCalibrationController != null){
            codeCalibrationController.setUBoatCompetitionPaneController(this);
        }
        readyButton.disableProperty().bind(isReady.not());
    }
    public UBoatCompetitionPaneController() {
        autoUpdate = new SimpleBooleanProperty();
        totalUsers = new SimpleIntegerProperty();
        this.isReady = new SimpleBooleanProperty(false);
        UBoatName = "";

    }

    private void setHttpStatusUpdate(UpdateHttpLine.HttpStatusUpdate httpStatusUpdate) {
        this.httpStatusUpdate = httpStatusUpdate;
    }

    public void setMainUBoatScenePaneController(MainUBoatScenePaneController mainUBoatScenePaneController) {
        this.mainUBoatScenePaneController = mainUBoatScenePaneController;
    }

    private void updateAlliesList(List<UBoat> usersNames) {
      //Platform.runLater(() -> {
      //    ObservableList<String> items = teamNameCol.getCellEditor().
      //    items.clear();
      //    items.addAll(usersNames);
      //    totalUsers.set(usersNames.size());
      //});
    }
    private void updateTeamsTable(AlliesToTable alliesToTable) {
        //new  ErrorDialog(new Exception("updateTeamsTable"), "updateTeamsTable");
        Platform.runLater(() -> {
           //ObservableList<List<String>> items = teamsTableView.getItems();
           //items.clear();
           //items.addAll(alliesToTable.getTeams());
           //totalUsers.set(alliesToTable.getTeams().size());

        clearTeamsTable();

        String currentUBoatName = alliesToTable.getBoatName();
        List<String> teams = alliesToTable.getTeams();
        List<Integer> numOfAgentsList = alliesToTable.getNumberOfAgentsForEachAllie();
        List<Long> taskSize = alliesToTable.getTasksSize();

        javafx.scene.control.TableColumn<List<String>, String> teamsCol = new javafx.scene.control.TableColumn<>("Team Name");
        javafx.scene.control.TableColumn<List<String>, String> numOfAgentsCol = new javafx.scene.control.TableColumn<>("Number Of Agents");
        javafx.scene.control.TableColumn<List<String>, String> taskSizeCol = new javafx.scene.control.TableColumn<>("Task Size");
        //TODO chen: check if the 3 rows below are needed
        teamsCol.setCellValueFactory(new PropertyValueFactory<>("Team Name"));
        numOfAgentsCol.setCellValueFactory(new PropertyValueFactory<>("Number Of Agents"));
        taskSizeCol.setCellValueFactory(new PropertyValueFactory<>("Task Size"));

        ObservableList<List<String>> data = teamsTableView.getItems();
        for (int i = 0; i < teams.size(); i++) {
            List<String> row = new ArrayList<>();
            row.add(teams.get(i));
            row.add(numOfAgentsList.get(i).toString());
            row.add(taskSize.get(i).toString());
            data.add(row);
        }

        teamsTableView.setItems(data);
        //teamsTableView.refresh();
        });
    }
    public void getCurrentMachineConfiguration(){
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "get_current_configuration")
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(e, "Failed to get machine configuration");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                if (response.isSuccessful()) {
                    String currentMachineConfiguration =GSON_INSTANCE.fromJson(responseStr, String.class);
                    Platform.runLater(() -> {
                        currentCodeConfigurationPaneController.setCodeConfiguration(currentMachineConfiguration);
                        //currentCodeConfigurationPaneController.updateMachineConfiguration();
                    });
                } else {
                    new ErrorDialog(new Exception(responseStr), "Failed to get machine configuration");
                }
            }
        });
    }

    private void getCurrentUBoat() {
        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.U_BOATS_LIST_SERVLET)
                .newBuilder()
                .addQueryParameter("action", "getUBoatName")
                .build()
                .toString();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        new ErrorDialog(e,"Unable to Find UBoat Name"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            new ErrorDialog(new Exception("Something went wrong. "), responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        String responseBody = null;
                        try {
                            responseBody = new Gson().fromJson(response.body().string(), String.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        UBoatName = responseBody;
                    });

                }
            }
        });
    }

    private void clearTeamsTable() {
        teamsTableView.getItems().clear();
    }


    private List<String> getNumOfAgentsFromAllAllies(List<Allie> alliesInBattle) {
        List<String> numOfAgentsList = new ArrayList<>();
        for (Allie allie : alliesInBattle) {
            numOfAgentsList.add(String.valueOf(allie.getAgents().size()));
        }
        return numOfAgentsList;
    }

    public void startListRefresher() {
        alliesInBattleRefresher = new AlliesInBattleRefresher(
                autoUpdate,
                this::updateTeamsTable);
        timer = new Timer();
        timer.schedule(alliesInBattleRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    //private void addRowToTeams() {
    //    teamNameCol.setCellValueFactory(new PropertyValueFactory<>("Team Name"));
    //    numOfAgentsCol.setCellValueFactory(new PropertyValueFactory<>("Number Of Agents"));
    //    taskSizeCol.setCellValueFactory(new PropertyValueFactory<>("Task Size"));
//
    //    System.out.println("addRowToTeams");
    //    ObservableList<List<String>> data = teamsTableView.getItems();
    //    List<String> row = new ArrayList<>();
//
    //    row.add("Team Name2");
    //    row.add("Number Of Agents2");
    //    row.add("Task Size2");
    //    data.add(row);
    //    teamsTableView.setItems(data);
    //    teamsTableView.refresh();
    //}

    @Override
    public void close() {
        teamsTableView.getItems().clear();
        totalUsers.set(0);
        if (alliesInBattleRefresher != null && timer != null) {
            alliesInBattleRefresher.cancel();
            timer.cancel();
        }
    }

    public void setActive() {
        startListRefresher();
    }

    public void setNewConfiguration(String currentCodeConfiguration) {
        currentCodeConfigurationPaneController.setCodeConfiguration(currentCodeConfiguration);
    }

    public void setDictionary() {
        encryptDecryptActionsGridController.setDictionarySearchComboBox();
    }


    @FXML public void onReadyButtonClicked(ActionEvent actionEvent) {
    }

    @FXML public void onLogOutButtonClicked(ActionEvent actionEvent) {
    }
}
