package MainScene.CompetitionPane;

import Api.UpdateHttpLine;
import DTO.AlliesToTable;
import DTO.ContestWinnerInformation;
import DTO.TeamsTable;
import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.AlliesManager.Allie;
import Engine.UBoatManager.UBoat;
import MainScene.CompetitionPane.CandidatesPane.UBoatCandidatesPaneController;
import MainScene.CompetitionPane.EncryptDecryptActionsPane.EncryptDecryptActionsGridController;
import MainScene.MainUBoatScenePaneController;
import MainScene.UBoatMachinePane.CurrentCodeConfigurationPane.CurrentCodeConfigurationController;
import UBoatServletsPaths.UBoatsServletsPaths;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static UBoatServletsPaths.UBoatsServletsPaths.GET_MACHINE_CONFIG_SERVLET;
import static Utils.Constants.*;

public class UBoatCompetitionPaneController implements Closeable {
    private String UBoatName;
    private Timer timer;
    private TimerTask alliesInBattleRefresher;
    private final BooleanProperty autoUpdate;
    private final IntegerProperty totalUsers;
    private UpdateHttpLine.HttpStatusUpdate httpStatusUpdate;
    private MainUBoatScenePaneController mainUBoatScenePaneController;
    @FXML private Button readyButton;
    @FXML private GridPane uBoatCandidatesPane;
    @FXML private UBoatCandidatesPaneController uBoatCandidatesPaneController;
    @FXML private GridPane UBoatCompetitionPane;
    @FXML private GridPane encryptDecryptActionsGrid;
    @FXML private EncryptDecryptActionsGridController encryptDecryptActionsGridController;

    @FXML private TableView<TeamsTable> teamsTableView;
    @FXML private TableColumn<TeamsTable, String> teamNameCol;
    @FXML private TableColumn<TeamsTable, Integer> numOfAgentsCol;
    @FXML private TableColumn<TeamsTable, Long> taskSizeCol;
    @FXML private VBox currentCodeConfiguration;
    @FXML private CurrentCodeConfigurationController currentCodeConfigurationController;
    private SimpleBooleanProperty isProcessedWordExist;
    private SimpleBooleanProperty isReadyToBattle;
    private SimpleBooleanProperty areTeamsInBattle;
    private boolean isWordProcessed;

    @FXML public void initialize(){
        if(encryptDecryptActionsGridController != null){
            encryptDecryptActionsGridController.setUBoatCompetitionPaneController(this);
        }
        if(uBoatCandidatesPaneController != null){
            uBoatCandidatesPaneController.setUBoatCompetitionPaneController(this);
        }

        readyButton.disableProperty().bind(isProcessedWordExist.not().or(isReadyToBattle));
        initializeCompetitionTable();
    }

    private void initializeCompetitionTable() {
        teamNameCol.setCellValueFactory(new PropertyValueFactory<TeamsTable, String>("teamName"));
        numOfAgentsCol.setCellValueFactory(new PropertyValueFactory<TeamsTable, Integer>("numOfAgents"));
        taskSizeCol.setCellValueFactory(new PropertyValueFactory<TeamsTable, Long>("taskSize"));
    }

    public UBoatCompetitionPaneController() {
        autoUpdate = new SimpleBooleanProperty();
        totalUsers = new SimpleIntegerProperty();
        this.isProcessedWordExist = new SimpleBooleanProperty(false);
        UBoatName = "";
        this.areTeamsInBattle = new SimpleBooleanProperty(false);
        this.isReadyToBattle = new SimpleBooleanProperty(false);
        isWordProcessed = false;
    }

    private void setHttpStatusUpdate(UpdateHttpLine.HttpStatusUpdate httpStatusUpdate) {
        this.httpStatusUpdate = httpStatusUpdate;
    }

    public void setMainUBoatScenePaneController(MainUBoatScenePaneController mainUBoatScenePaneController) {
        this.mainUBoatScenePaneController = mainUBoatScenePaneController;
    }
    public void setNoWordProcessed(){
        this.isWordProcessed = false;
    }
    public void setIsWordProcessed(){
        this.isWordProcessed = true;
    }
   //public void setIsReadyOrNot(){
   //    if(isWordProcessed && areTeamsInBattle){
   //        this.isReady.set(true);
   //    }
   //    else{
   //        this.isReady.set(false);
   //    }
   //}
   //public void setNoTeamsInBattle(){
   //    this.areTeamsInBattle = false;
   //}
   //public void setAreTeamsInBattle(){
   //    this.areTeamsInBattle = true;
   //}

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

        clearTeamsTable();
        String currentUBoatName = alliesToTable.getBoatName();
        List<String> teams = alliesToTable.getTeams();
        List<Integer> numOfAgentsList = alliesToTable.getNumberOfAgentsForEachAllie();
        List<Long> taskSize = alliesToTable.getTasksSize();

        for (int i = 0; i < teams.size(); i++) {
            TeamsTable teamToAdd = new TeamsTable(teams.get(i), numOfAgentsList.get(i), taskSize.get(i));
            ObservableList<TeamsTable> tableData = teamsTableView.getItems();
            tableData.add(teamToAdd);
            teamsTableView.setItems(tableData);
        }
        if(teams.size() > 0){
            this.areTeamsInBattle.set(true);
        }
        else{
            this.areTeamsInBattle.set(false);
        }
        });

    }
    public void getCurrentMachineConfiguration(){
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getCurrentMachineConfig")
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
                        System.out.println("code: " + currentMachineConfiguration);
                        mainUBoatScenePaneController.setCodeConfiguration(currentMachineConfiguration);
                        currentCodeConfigurationController.setCodeConfiguration(currentMachineConfiguration);
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


    @Override
    public void close() {
        //teamsTableView.getItems().clear();
        //totalUsers.set(0);
        if (alliesInBattleRefresher != null && timer != null) {
            alliesInBattleRefresher.cancel();
            timer.cancel();
        }
        mainUBoatScenePaneController.close();
    }

    public void setActive() {
        startListRefresher();
    }

    public void setDictionary() {
        encryptDecryptActionsGridController.setDictionarySearchComboBox();
    }


    @FXML public void onReadyButtonClicked(ActionEvent actionEvent) {

        String finalUrl = HttpUrl.parse(READY_MANAGER_SERVLET)
                .newBuilder()
                .addQueryParameter(TYPE, "uBoat")
                .addQueryParameter(ACTION, "ready")
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(e, "Failed to set ready");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                if (response.isSuccessful()) {
                    uBoatCandidatesPaneController.startListRefreshing();
                    Platform.runLater(() -> {
                        isReadyToBattle.set(true);
                        mainUBoatScenePaneController.setNoneWinnerFound();
                    });
                } else {
                    new ErrorDialog(new Exception(responseStr), "Failed to set ready");
                }
                response.close();

            }
        });
    }

    public void setNewConfiguration(String currentMachineConfiguration) {
        currentCodeConfigurationController.setCodeConfiguration(currentMachineConfiguration);
        mainUBoatScenePaneController.setCodeConfiguration(currentMachineConfiguration);
    }

    public void machineDetailsChanged() {
        mainUBoatScenePaneController.machineDetailsChanged();
    }

    public void setProcessedWordExist(boolean isWordProcessed) {
        isProcessedWordExist.set(isWordProcessed);
    }

    public void notifyIfWordIsFound(ContestWinnerInformation contestWinner) {
        mainUBoatScenePaneController.notifyIfWordIsFound(contestWinner);
    }

    public void stopContest() {
        uBoatCandidatesPaneController.stopContest();
        encryptDecryptActionsGridController.clearTexts();
        isReadyToBattle.set(false);
        isProcessedWordExist.set(false);
    }

    public void incrementMessagesCounter () {
        mainUBoatScenePaneController.incrementMessagesCounter();
    }
}
