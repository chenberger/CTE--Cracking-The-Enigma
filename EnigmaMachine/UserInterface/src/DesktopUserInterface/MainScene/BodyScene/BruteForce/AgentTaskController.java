package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class AgentTaskController {
    @FXML private GridPane bruteForceGrid;

    @FXML private Label agentIdLabel;

    @FXML private Label taskTotalTimeLabel;

    @FXML private TextArea candidatesArea;

    public void setCandidateMessege(String candidates) {
        candidatesArea.setText(candidates);
    }

    public void setAgentId(String id) {
        agentIdLabel.setText("Agent id: " + id);
    }

    public void setTotalTime(Long time) {
        taskTotalTimeLabel.setText("Task total time: " + time.toString());
    }
}
