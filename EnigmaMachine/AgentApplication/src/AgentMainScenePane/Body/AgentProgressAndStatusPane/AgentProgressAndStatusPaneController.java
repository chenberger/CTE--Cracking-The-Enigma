package AgentMainScenePane.Body.AgentProgressAndStatusPane;

import AgentMainScenePane.AgentMainScenePaneController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.Closeable;
import java.util.Timer;

public class AgentProgressAndStatusPaneController implements Closeable {
    private AgentProgressAndStatusRefresher agentProgressAndStatusRefresher;
    private Timer timer;
    @FXML
    private Label tasksPulledLabel;

    @FXML
    private Label tasksProcessedLabel;

    @FXML
    private Label candidatesFoundLabel;
    private AgentMainScenePaneController agentMainSceneController;

    public void setAgentMainSceneController(AgentMainScenePaneController agentMainScenePaneController) {
        this.agentMainSceneController = agentMainScenePaneController;
    }

    public void startRefreshing() {
    }
    @Override
    public void close() {
    }
}
