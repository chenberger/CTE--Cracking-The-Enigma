package AgentMainScenePane.Body.AgentProgressAndStatusPane;

import AgentMainScenePane.AgentMainScenePaneController;
import DTO.AgentProgressData;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.Closeable;
import java.util.Timer;

public class AgentProgressAndStatusPaneController implements Closeable {


    private AgentProgressAndStatusRefresher agentProgressAndStatusRefresher;
    private Timer timer;
    @FXML private Label numberOfTasksInQueueLabel;
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
        agentProgressAndStatusRefresher = new AgentProgressAndStatusRefresher(this::updateProgressAndStatus);
        timer = new Timer();
        timer.schedule(agentProgressAndStatusRefresher, 0, 100);
    }
    @Override
    public void close() {
        if(timer != null) {
            timer.cancel();
        }
        if(agentProgressAndStatusRefresher != null) {
            agentProgressAndStatusRefresher.cancel();
        }
    }
    private void updateProgressAndStatus(AgentProgressData agentProgressData) {
        Platform.runLater(() -> {
            tasksProcessedLabel.setText(String.valueOf((agentProgressData.getTasksProcessed())));
            candidatesFoundLabel.setText(String.valueOf(agentProgressData.getCandidatesFound()));
            tasksPulledLabel.setText(String.valueOf(agentProgressData.getTasksPulled()));
            numberOfTasksInQueueLabel.setText(String.valueOf(agentProgressData.getNumberOfTasksInQueue()));
        });
    }

    public void updateTasksCompleted(long tasksCompleted, int numberOfCandidatesFound) {
        Platform.runLater(() -> {
            tasksProcessedLabel.setText(String.valueOf(tasksCompleted));
            candidatesFoundLabel.setText(String.valueOf(numberOfCandidatesFound));
        });
    }

    public void updateTasksPulled(long numberOfTasksPulled) {
        Platform.runLater(() -> {
            tasksPulledLabel.setText(String.valueOf(numberOfTasksPulled));
        });
    }
    public void updateNumberOfTasksInQueue(long numberOfTasksInQueue) {
        Platform.runLater(() -> {
            numberOfTasksInQueueLabel.setText(String.valueOf(numberOfTasksInQueue));
        });
    }
}
