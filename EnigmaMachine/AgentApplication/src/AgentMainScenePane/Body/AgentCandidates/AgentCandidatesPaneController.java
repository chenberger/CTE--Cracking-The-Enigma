package AgentMainScenePane.Body.AgentCandidates;

import AgentMainScenePane.AgentMainScenePaneController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.Closeable;
import java.util.Timer;

public class AgentCandidatesPaneController implements Closeable {
    private AgentsCandidatesRefresher agentCandidatesRefresher;
    private Timer timer;
    @FXML
    private TableView<?> candidatesTableView;

    @FXML
    private TableColumn<?, ?> candidatesCol;

    @FXML
    private TableColumn<?, ?> numberOfTaskCol;

    @FXML
    private TableColumn<?, ?> codeOfTaskCol;
    private AgentMainScenePaneController agentMainSceneController;

    public void setAgentMainSceneController(AgentMainScenePaneController agentMainSceneController) {
        this.agentMainSceneController = agentMainSceneController;
    }

    public void startRefreshing() {
    }
    @Override public void close() {
    }
}
