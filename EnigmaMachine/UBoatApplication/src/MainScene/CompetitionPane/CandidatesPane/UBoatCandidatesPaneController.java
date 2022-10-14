package MainScene.CompetitionPane.CandidatesPane;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import DTO.AllyCandidatesTable;
import DTO.TeamsTable;
import MainScene.CompetitionPane.UBoatCompetitionPaneController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;

public class UBoatCandidatesPaneController implements Closeable {
    Timer timer;
    private UBoatCandidatesTableRefresher uBoatCandidatesTableRefresher;
    @FXML
    private TableView<AllyCandidatesTable> uBoatCandidatesTable;

    @FXML
    private TableColumn<AllyCandidatesTable, String> candidateStringCol;

    @FXML
    private TableColumn<AllyCandidatesTable, String> teamNameCol;

    @FXML
    private TableColumn<AllyCandidatesTable, String> codeConfigurationCol;
    private UBoatCompetitionPaneController uBoatCompetitionPaneController;
    //TODO : Make sure that the dependency that i added(this client on the allies client) does not create circular dependency.
    //private ContestTabPaneController contestTabPaneController;

    public void setUBoatCompetitionPaneController(UBoatCompetitionPaneController uBoatCompetitionPaneController) {
        this.uBoatCompetitionPaneController = uBoatCompetitionPaneController;
    }
    @FXML public void initialize(){
        initializeTableColumns();
    }

    private void initializeTableColumns() {
        candidateStringCol.setCellValueFactory(new PropertyValueFactory<AllyCandidatesTable, String>("candidateString"));
        teamNameCol.setCellValueFactory(new PropertyValueFactory<AllyCandidatesTable, String>("teamName"));
        codeConfigurationCol.setCellValueFactory(new PropertyValueFactory<AllyCandidatesTable, String>("codeConfiguration"));
    }

    public void startListRefreshing(){
        uBoatCandidatesTableRefresher = new UBoatCandidatesTableRefresher(this::updateCandidatesTable);
        timer = new Timer();
        timer.schedule(uBoatCandidatesTableRefresher, 0, 100);
    }
    private void updateCandidatesTable(List<AllyCandidatesTable> allyCandidatesTableList){

        Platform.runLater(()->{
            uBoatCandidatesTable.getItems().clear();

            for (AllyCandidatesTable allyCandidatesTable : allyCandidatesTableList) {
                AllyCandidatesTable candidateToAdd = new AllyCandidatesTable(allyCandidatesTable.getCandidateString(), allyCandidatesTable.getTeamName(), allyCandidatesTable.getCodeConfiguration());
                ObservableList<AllyCandidatesTable> tableData = uBoatCandidatesTable.getItems();
                tableData.add(candidateToAdd);
                uBoatCandidatesTable.setItems(tableData);
            }
        });
    }

    @Override
    public void close()  {
        //TODO : Close the timer task that refreshes the candidates table.
        if(uBoatCandidatesTableRefresher != null){
            uBoatCandidatesTableRefresher.cancel();
            timer.cancel();
        }
    }

   // public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
    //    this.contestTabPaneController = contestTabPaneController;
   // }
}
