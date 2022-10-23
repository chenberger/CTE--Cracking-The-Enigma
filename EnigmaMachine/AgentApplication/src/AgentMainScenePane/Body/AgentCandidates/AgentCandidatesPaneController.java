package AgentMainScenePane.Body.AgentCandidates;

import AgentMainScenePane.AgentMainScenePaneController;
import DTO.AgentCandidatesInformation;
import DTO.DataToAgentApplicationTableView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;

public class AgentCandidatesPaneController implements Closeable {
    private AgentsCandidatesRefresher agentCandidatesRefresher;
    private Timer timer;
    @FXML
    private TableView<DataToAgentApplicationTableView> candidatesTableView;

    @FXML
    private TableColumn<DataToAgentApplicationTableView, String> candidatesCol;

    @FXML
    private TableColumn<DataToAgentApplicationTableView, String> numberOfTaskCol;

    @FXML
    private TableColumn<DataToAgentApplicationTableView, String> codeOfTaskCol;
    private AgentMainScenePaneController agentMainSceneController;

    public void setAgentMainSceneController(AgentMainScenePaneController agentMainSceneController) {
        this.agentMainSceneController = agentMainSceneController;
    }
    @FXML public void initialize(){
        initializeTableColumns();
    }

    private void initializeTableColumns() {
        candidatesCol.setCellValueFactory(new PropertyValueFactory<DataToAgentApplicationTableView, String>("candidateString"));
        numberOfTaskCol.setCellValueFactory(new PropertyValueFactory<DataToAgentApplicationTableView, String>("numberOfTask"));
        codeOfTaskCol.setCellValueFactory(new PropertyValueFactory<DataToAgentApplicationTableView, String>("configurationOfTask"));
    }

    public void startRefreshing() {
        agentCandidatesRefresher = new AgentsCandidatesRefresher(this::updateCandidatesTable);
        timer = new Timer();
        timer.schedule(agentCandidatesRefresher, 0, 100);
    }
    @Override public void close() {
        if (agentCandidatesRefresher != null) {
            agentCandidatesRefresher.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    public void updateCandidatesTable(List<DataToAgentApplicationTableView> agentCandidatesInformationList) {
        Platform.runLater(()->{
            candidatesTableView.getItems().clear();
            for (DataToAgentApplicationTableView agentCandidatesInformation : agentCandidatesInformationList) {
                candidatesTableView.getItems().add(new DataToAgentApplicationTableView(agentCandidatesInformation.getCandidateString(),agentCandidatesInformation.getNumberOfTask(),agentCandidatesInformation.getConfigurationOfTask()));
            }
        });
    }
}
