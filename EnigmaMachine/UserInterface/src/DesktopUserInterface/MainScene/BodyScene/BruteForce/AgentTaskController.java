package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.Common.SkinType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentTaskController {
    @FXML private GridPane bruteForceGrid;

    @FXML private Label agentIdLabel;

    @FXML private Label taskTotalTimeLabel;

    @FXML private TextArea candidatesArea;
    private Map<SkinType, String> skinPaths;

    @FXML public void initialize() {
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "AgentTaskSkin" + skinIndex++ + ".css");
        }
    }

    public void setCandidateMessege(String candidates) {
        if(candidatesArea.getText() == null || Objects.equals(candidatesArea.getText(), "")) {
            candidatesArea.setText(candidates);
        }
        else{
            candidatesArea.setText(candidatesArea.getText() + System.lineSeparator() + candidates);
        }
    }

    public void setAgentId(String id) {
        agentIdLabel.setText("Agent id: " + id);
    }

    public void setTotalTime(Long time) {
        taskTotalTimeLabel.setText("Task total time: " + time.toString() + " ns");
    }

    public void setSkin(SkinType skinType) {
        bruteForceGrid.getStylesheets().clear();
        bruteForceGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }
}
