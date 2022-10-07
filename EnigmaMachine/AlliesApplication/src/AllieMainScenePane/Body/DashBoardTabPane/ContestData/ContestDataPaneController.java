package AllieMainScenePane.Body.DashBoardTabPane.ContestData;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.DashBoardTabPane.DashboardTabPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData.AgentsTable;
import DTO.OnLineContestsDataToTable;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

import static Utils.Constants.REFRESH_RATE;

public class ContestDataPaneController implements Closeable {
    private AllieMainScenePaneController allieMainScenePaneController;
    private DashboardTabPaneController dashboardTabPaneController;
    private SimpleBooleanProperty autoUpdate = new SimpleBooleanProperty();
    private ContestsDataRefresher contestsDataRefresher;
    @FXML private TableView<OnLineContestsTable> contestsTable;
    @FXML private TableColumn<OnLineContestsTable, String> battleNameCol;
    @FXML private TableColumn<OnLineContestsTable, String> boatNameCol;
    @FXML private TableColumn<OnLineContestsTable, String> contestStatusCol;
    @FXML private TableColumn<OnLineContestsTable, String> difficultyCol;
    @FXML private TableColumn<OnLineContestsTable, String> teamsRegisteredAndNeededCol;
    private Timer timer;


    @FXML public void initialize(){
        initializeContestsTable();
    }

    private void initializeContestsTable() {
        battleNameCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("Battle Name"));
        boatNameCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("U-Boat Name"));
        contestStatusCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("Status"));
        difficultyCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("Difficulty"));
        teamsRegisteredAndNeededCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("Teams Registered/Needed"));
    }

    public void setDashboardTabPaneController(DashboardTabPaneController dashboardTabPaneController) {
        this.dashboardTabPaneController = dashboardTabPaneController;
    }

    public void onRowOfTableMouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() > 0) {
            //TODO: get the name of the u boat from the chose row of the table
        }
    }

    public void startListRefresher() {
        contestsDataRefresher = new ContestsDataRefresher(
                autoUpdate,
                this::updateContestsTable);
        timer = new Timer();
        timer.schedule(contestsDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateContestsTable(OnLineContestsDataToTable onLineContestsDataToTable) {
        Platform.runLater(() -> {
            clearContestsTable();

            List<String> battleNames = onLineContestsDataToTable.getBattleNames();
            List<String> boatNames = onLineContestsDataToTable.getUBoatNames();
            Map<String,String> contestStatuses = onLineContestsDataToTable.getContestsStatus();
            List<String> difficulties = onLineContestsDataToTable.getDifficultyLevels().stream().map(Enum::toString).collect(Collectors.toList());
            Map<String,Integer> teamsRegisteredToEachBattle = onLineContestsDataToTable.getNumberOfTeamsRegisteredToEachContest();
            List<Integer> numberOfTeamsNeededToEachContest = onLineContestsDataToTable.getNumberOfTeamsNeededToEachContest();

            List<String> teamsRegisteredAndNeeded = setTeamsRegisteredAndNeeded(teamsRegisteredToEachBattle, numberOfTeamsNeededToEachContest, battleNames);
            for (int i = 0; i < boatNames.size(); i++) {
                OnLineContestsTable contestToAdd = new OnLineContestsTable(
                        battleNames.get(i),
                        boatNames.get(i),
                        contestStatuses.get(boatNames.get(i)),
                        difficulties.get(i),
                        teamsRegisteredAndNeeded.get(i));
                ObservableList<OnLineContestsTable> tableData = contestsTable.getItems();
                tableData.add(contestToAdd);
                contestsTable.setItems(tableData);
            }
        });
    }

    private List<String> setTeamsRegisteredAndNeeded(Map<String, Integer> teamsRegisteredToEachBattle, List<Integer> numberOfTeamsNeededToEachContest, List<String> battleNames) {
        return teamsRegisteredToEachBattle.entrySet().stream()
                .map(entry -> entry.getValue() + "/" + numberOfTeamsNeededToEachContest.get(battleNames.indexOf(entry.getKey())))
                .collect(Collectors.toList());
    }

    private void clearContestsTable() {
        contestsTable.getItems().clear();
    }

    @Override
    public void close() {
        contestsTable.getItems().clear();
        if (contestsDataRefresher != null && timer != null) {
            contestsDataRefresher.cancel();
            timer.cancel();
        }
    }
}
