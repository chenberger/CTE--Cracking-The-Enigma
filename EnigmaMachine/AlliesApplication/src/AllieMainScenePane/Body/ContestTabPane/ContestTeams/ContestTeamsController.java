package AllieMainScenePane.Body.ContestTabPane.ContestTeams;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import DTO.AlliesToTable;
import DTO.TeamsTable;
import MainScene.CompetitionPane.AlliesInBattleRefresher;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;

import static Utils.Constants.REFRESH_RATE;

public class ContestTeamsController implements Closeable {

    private ContestTabPaneController contestTabPaneController;
    private final IntegerProperty totalUsers;
    private ContestTeamsRefresher contestTeamsRefresher;
    private SimpleBooleanProperty autoUpdate;
    private Timer timer;
    @FXML
    private TableView<TeamsTable> contestTeamsTable;
    @FXML
    private TableColumn<TeamsTable, String> teamNameCol;
    @FXML
    private TableColumn<TeamsTable, Integer> numOfAgentsCol;
    @FXML
    private TableColumn<TeamsTable, Long> taskSizeCol;
    public ContestTeamsController() {
        totalUsers = new SimpleIntegerProperty();
        autoUpdate = new SimpleBooleanProperty(false);
    }
    public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
        this.contestTabPaneController = contestTabPaneController;
    }
    @FXML public void initialize(){
        initializeTable();
    }
    private void initializeTable(){
        teamNameCol.setCellValueFactory(new PropertyValueFactory<TeamsTable, String>("teamName"));
        numOfAgentsCol.setCellValueFactory(new PropertyValueFactory<TeamsTable, Integer>("numOfAgents"));
        taskSizeCol.setCellValueFactory(new PropertyValueFactory<TeamsTable, Long>("taskSize"));
    }
    private void updateTeamsTable(AlliesToTable alliesToTable) {

        Platform.runLater(() -> {

            clearContestTeamsTable();
            String currentUBoatName = alliesToTable.getBoatName();
            List<String> teams = alliesToTable.getTeams();
            List<Integer> numOfAgentsList = alliesToTable.getNumberOfAgentsForEachAllie();
            List<Long> taskSize = alliesToTable.getTasksSize();

            for (int i = 0; i < teams.size(); i++) {
                TeamsTable teamToAdd = new TeamsTable(teams.get(i), numOfAgentsList.get(i), taskSize.get(i));
                ObservableList<TeamsTable> tableData = contestTeamsTable.getItems();
                tableData.add(teamToAdd);
                contestTeamsTable.setItems(tableData);
            }
        });

    }

    private void clearContestTeamsTable() {
        ObservableList<TeamsTable> tableData = contestTeamsTable.getItems();
        tableData.clear();
    }
    public void startListRefresher() {
        contestTeamsRefresher = new ContestTeamsRefresher(
                autoUpdate,
                this::updateTeamsTable);
        timer = new Timer();
        timer.schedule(contestTeamsRefresher, REFRESH_RATE, REFRESH_RATE);
    }


    @Override
    public void close() {
        contestTeamsTable.getItems().clear();
        totalUsers.set(0);
        if (contestTeamsRefresher != null && timer != null) {
            contestTeamsRefresher.cancel();
            timer.cancel();
        }
        contestTabPaneController.close();
    }

    public void setShouldUpdateToTrue() {
        autoUpdate.set(true);
    }
}
