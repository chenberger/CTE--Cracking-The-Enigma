package AllieMainScenePane.Body.ContestTabPane.TeamCandidates;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import AllieMainScenePane.Body.ContestTabPane.ContestTeams.ContestTeamsRefresher;
import DTO.AllyCandidatesTable;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;

public class TeamCandidatesController implements Closeable {
    private TeamCandidatesRefresher teamCandidatesRefresher;
    private Timer timer;
    @FXML
    private TableView<AllyCandidatesTable> teamsCandidateTableView;

    @FXML
    private TableColumn<AllyCandidatesTable, String> candidateCol;

    @FXML
    private TableColumn<AllyCandidatesTable, String> teamNameCol;

    @FXML
    private TableColumn<AllyCandidatesTable, String> codeConfigurationCol;
    public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
    }
    @FXML public void initialize(){
        initializeTableColumns();
    }

    private void initializeTableColumns() {
        candidateCol.setCellValueFactory(new PropertyValueFactory<AllyCandidatesTable, String>("candidateString"));
        teamNameCol.setCellValueFactory(new PropertyValueFactory<AllyCandidatesTable, String>("teamName"));
        codeConfigurationCol.setCellValueFactory(new PropertyValueFactory<AllyCandidatesTable, String>("codeConfiguration"));
    }
    private void updateCandidatesTable(List<AllyCandidatesTable> allyCandidatesTableList){
        Platform.runLater(()->{
            teamsCandidateTableView.getItems().clear();

            for (int i = 0; i < allyCandidatesTableList.size(); i++) {
                AllyCandidatesTable candidateToAdd = new AllyCandidatesTable(allyCandidatesTableList.get(i).getCandidateString(), allyCandidatesTableList.get(i).getTeamName(), allyCandidatesTableList.get(i).getCodeConfiguration());
                ObservableList<AllyCandidatesTable> tableData = teamsCandidateTableView.getItems();
                tableData.add(candidateToAdd);
                teamsCandidateTableView.setItems(tableData);
            }
        });
    }
    public void startListRefreshing(){
        teamCandidatesRefresher = new TeamCandidatesRefresher(this::updateCandidatesTable);
        timer = new Timer();
        timer.schedule(teamCandidatesRefresher, 0, 100);
    }

    @Override
    public void close() {
    }
}
