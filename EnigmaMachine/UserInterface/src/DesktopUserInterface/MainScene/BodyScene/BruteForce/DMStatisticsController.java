package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.ErrorDialog;
import EnigmaMachineException.DecryptionMessegeNotInitializedException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class DMStatisticsController {

    @FXML private GridPane machineStatisticsGridPane;

    @FXML private ProgressBar taskProgressBar;

    @FXML private Label totalTasksLabel;

    @FXML private Label machineStatisticsHeader;

    @FXML private Label processedTasksLabel;

    @FXML private Label missionTotalTimeLabel;

    @FXML private Label averageTaskTimeLabel;
    @FXML private Label taskProgressLabel;

    @FXML private Button startStopButton;

    @FXML private Button pauseResumeButton;
    @FXML FlowPane flowPaneCandidates;

    private final String START_LABEL = "Start";
    private final String STOP_LABEL = "Stop";
    private final String PAUSE_LABEL = "Pause";
    private final String RESUME_LABEL = "Resume";
    private BruteForceGridController bruteForceGridController;
    private SimpleBooleanProperty isStartButtonClicked;
    private SimpleIntegerProperty missionTotalTimeProperty;
    private SimpleIntegerProperty averageTaskTimeProperty;
    private SimpleIntegerProperty processedTasksProperty;
    private SimpleIntegerProperty totalTasksProperty;
    private SimpleStringProperty taskProgressProperty;
    private HashMap<Integer, AgentTaskController> tasksControllerMapping;

    public DMStatisticsController() {
        isStartButtonClicked = new SimpleBooleanProperty(false);
        missionTotalTimeProperty = new SimpleIntegerProperty(0);
        averageTaskTimeProperty = new SimpleIntegerProperty(0);
        processedTasksProperty = new SimpleIntegerProperty(0);
        totalTasksProperty = new SimpleIntegerProperty(0);
        taskProgressProperty = new SimpleStringProperty("");
        tasksControllerMapping = new HashMap<>();
    }

    @FXML public void initialize() {
        pauseResumeButton.disableProperty().bind(isStartButtonClicked.not());
        missionTotalTimeLabel.textProperty().bind(Bindings.format("%,d", missionTotalTimeProperty));
        averageTaskTimeLabel.textProperty().bind(Bindings.format("%,d", averageTaskTimeProperty));
        processedTasksLabel.textProperty().bind(Bindings.format("%,d", processedTasksProperty));
        totalTasksLabel.textProperty().bind(Bindings.format("%,d", totalTasksProperty));
    }

    @FXML void onPauseResumeButtonClicked(ActionEvent event) {
        if(Objects.equals(pauseResumeButton.getText(), PAUSE_LABEL)) {
            //TODO erez: implement pause to brute force mission

            pauseResumeButton.setText(RESUME_LABEL);
        }
        else {
            //TODO erez: implement resume tu brute force mission
            pauseResumeButton.setText(PAUSE_LABEL);
        }
    }

    @FXML private void onStartStopButtonClicked(ActionEvent event) {
        if(Objects.equals(startStopButton.getText(), START_LABEL)) {
            try {
                bruteForceGridController.startBruteForce(
                        createUIAdapter(),
                        () -> {
                            startStopButton.setText(START_LABEL);
                            isStartButtonClicked.set(false);
                            pauseResumeButton.setText(PAUSE_LABEL);
                        }
                );
                isStartButtonClicked.set(true);
                startStopButton.setText(STOP_LABEL);
            }
            catch (DecryptionMessegeNotInitializedException | CloneNotSupportedException | IllegalArgumentException ex) {
                new ErrorDialog(ex, "Error: Failed to start brute force decipher mission");
            }
        }
        else {
            startStopButton.setText(START_LABEL);
            isStartButtonClicked.set(false);
            //TODO erez: implement stop brute force
        }

        pauseResumeButton.setText(PAUSE_LABEL);
    }

    public void setBruteForceGridController(BruteForceGridController bruteForceGridController) {
        this.bruteForceGridController = bruteForceGridController;
    }

    public UIAdapter createUIAdapter() {
        return new UIAdapter(
                agentTaskData -> {
                    createAgentTask(agentTaskData.hashCode());
                },
                agentTaskData -> {
                    AgentTaskController agentTaskController = tasksControllerMapping.get(agentTaskData.hashCode());
                    if (agentTaskController != null) {
                        agentTaskController.setCandidateMessege(agentTaskData.getCandidates());
                    }
                },
                (delta) -> {
                    processedTasksProperty.set(processedTasksProperty.get() + delta);
                },
                (totalTasks) -> {
                    totalTasksProperty.set(totalTasks);
                },
                (missionTotalTime) -> {
                    missionTotalTimeProperty.set(missionTotalTime);
                },
                (averageTaskTime) -> {
                    averageTaskTimeProperty.set(averageTaskTime);
                }
        );
    }

    private void createAgentTask(Integer id) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new URL("AgentTask.fxml"));
            Node singleAgentTask = loader.load();

            AgentTaskController agentTaskController = loader.getController();
            agentTaskController.setId(id);
            agentTaskController.setTotalTime(0);

            flowPaneCandidates.getChildren().add(singleAgentTask);
            tasksControllerMapping.put(id, agentTaskController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
