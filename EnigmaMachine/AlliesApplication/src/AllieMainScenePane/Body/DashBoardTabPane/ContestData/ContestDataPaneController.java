package AllieMainScenePane.Body.DashBoardTabPane.ContestData;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.DashboardTabPaneController;
import DTO.OnLineContestsDataToTable;
import DTO.OnLineContestsTable;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

import static Utils.Constants.REFRESH_RATE;

public class ContestDataPaneController implements Closeable {
    private int numberOfContests;
    private AllieMainScenePaneController allieMainScenePaneController;
    private DashboardTabPaneController dashboardTabPaneController;
    private ContestTabPaneController contestTabPaneController;
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
       battleNameCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("battleName"));
       boatNameCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("boatName"));
       contestStatusCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("contestStatus"));
       difficultyCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("difficulty"));
       teamsRegisteredAndNeededCol.setCellValueFactory(new PropertyValueFactory<OnLineContestsTable, String>("teamsRegisteredAndNeeded"));
    }

    public void setDashboardTabPaneController(DashboardTabPaneController dashboardTabPaneController) {
        this.dashboardTabPaneController = dashboardTabPaneController;
    }
    public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
        this.contestTabPaneController = contestTabPaneController;
    }

    public void startListRefresher() {
        numberOfContests = 0;
        contestsDataRefresher = new ContestsDataRefresher(
                autoUpdate,
                this::updateContestsTable);
        timer = new Timer();
        timer.schedule(contestsDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateContestsTable(OnLineContestsDataToTable onLineContestsDataToTable) {
        boolean isNumberOfRegisteredTeamsChanged = false;
        Platform.runLater(() -> {
            if(onLineContestsDataToTable.getBattleNames().size() != numberOfContests || numberOfRegisteredTeamsChanged(onLineContestsDataToTable,contestsTable.getItems())){
                clearContestsTable();

                List<String> battleNames = onLineContestsDataToTable.getBattleNames();
                List<String> boatNames = onLineContestsDataToTable.getUBoatNames();
                Map<String, String> contestStatuses = onLineContestsDataToTable.getContestsStatus();
                List<String> difficulties = onLineContestsDataToTable.getDifficultyLevels().stream().map(Enum::toString).collect(Collectors.toList());
                Map<String, Integer> teamsRegisteredToEachBattle = onLineContestsDataToTable.getNumberOfTeamsRegisteredToEachContest();
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
                    numberOfContests = battleNames.size();
                }
            }
        });
    }

    private boolean numberOfRegisteredTeamsChanged(OnLineContestsDataToTable onLineContestsDataToTable, ObservableList<OnLineContestsTable> items) {

        for(String battleName : onLineContestsDataToTable.getBattleNames()){
            for (int j = 0; j <onLineContestsDataToTable.getBattleNames().size(); j++) {
                if (items.get(j).getBattleName().equals(battleName)) {
                    if (!items.get(j).getTeamsRegisteredAndNeeded().equals(onLineContestsDataToTable.getNumberOfTeamsRegisteredToEachContest().get(battleName) + "/" + onLineContestsDataToTable.getNumberOfTeamsNeededToEachContest().get(j))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    private List<String> setTeamsRegisteredAndNeeded(Map<String, Integer> teamsRegisteredToEachBattle, List<Integer> numberOfTeamsNeededToEachContest, List<String> battleNames) {
        List<String> teamsRegisteredAndNeeded = new ArrayList<>();
        //TODO: fix this so i can show the number registered and needed
        for (int i = 0; i < battleNames.size(); i++) {
            String battleName = battleNames.get(i);
            Integer numberOfTeamsRegistered = Integer.valueOf(teamsRegisteredToEachBattle.get(battleName) == null ? "0" : teamsRegisteredToEachBattle.get(battleName).toString());
            int numberOfTeamsNeeded = numberOfTeamsNeededToEachContest.get(i);
            teamsRegisteredAndNeeded.add(numberOfTeamsRegistered + "/" + numberOfTeamsNeeded);
        }
        return teamsRegisteredAndNeeded;
    }

    private void clearContestsTable() {
        contestsTable.getItems().clear();
    }

    @Override
    public void close() {
        //contestsTable.getItems().clear();
        if (contestsDataRefresher != null && timer != null) {
            contestsDataRefresher.cancel();
            timer.cancel();
        }
    }

    public OnLineContestsTable getSelectedContest() throws IllegibleContestAmountChosenException {
        ObservableList<OnLineContestsTable> selectedContests = contestsTable.getSelectionModel().getSelectedItems();
        if(selectedContests.size() != 1) {
            throw new IllegibleContestAmountChosenException(selectedContests.size());
        }
        else {
            return selectedContests.get(0);
        }
    }
}
