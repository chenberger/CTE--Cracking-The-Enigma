package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.ErrorDialog;
import EnigmaMachineException.DecryptionMessegeNotInitializedException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

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
    private SimpleLongProperty averageTaskTimeProperty;
    private SimpleLongProperty missionTotalTimeProperty;
    private SimpleLongProperty processedTasksProperty;
    private SimpleLongProperty totalTasksProperty;
    private SimpleStringProperty taskProgressProperty;
    private HashMap<Integer, AgentTaskController> tasksControllerMapping;

    public DMStatisticsController() {
        isStartButtonClicked = new SimpleBooleanProperty(false);
        missionTotalTimeProperty = new SimpleLongProperty(0);
        averageTaskTimeProperty = new SimpleLongProperty(0);
        processedTasksProperty = new SimpleLongProperty(0);
        totalTasksProperty = new SimpleLongProperty(0);
        taskProgressProperty = new SimpleStringProperty("");
        tasksControllerMapping = new HashMap<>();
    }

    @FXML public void initialize() {
        pauseResumeButton.disableProperty().bind(isStartButtonClicked.not());
        missionTotalTimeLabel.textProperty().bind(Bindings.concat("Decoding mission total time: ", Bindings.format("%,d ms", missionTotalTimeProperty)));
        averageTaskTimeLabel.textProperty().bind(Bindings.concat("Average task time per agent: ", Bindings.format("%,d ms", averageTaskTimeProperty)));
        processedTasksLabel.textProperty().bind(Bindings.concat("Processed tasks:  ", Bindings.format("%,d", processedTasksProperty)));
        totalTasksLabel.textProperty().bind(Bindings.concat("Total tasks: ", Bindings.format("%,d", totalTasksProperty)));
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

    public BruteForceUIAdapter createUIAdapter() {
        return new BruteForceUIAdapter(
                (agentTaskData) -> {
                    AgentTaskController agentTaskController = tasksControllerMapping.get(agentTaskData.getTaskId());
                    if (agentTaskController != null) {
                        agentTaskController.setCandidateMessege(agentTaskData.getDecryptionCandidateFormat().toString());
                    }
                    else {
                        createAgentTask(agentTaskData.getAgentId(), agentTaskData.getTaskId());
                    }
                },
                (processedAgentTasksAmount) -> {
                    processedTasksProperty.set(processedAgentTasksAmount);
                },
                (totalTasks) -> {
                    totalTasksProperty.set(totalTasks);
                },
                (missionTotalTime) -> {
                    missionTotalTimeProperty.set(missionTotalTime);
                },
                (averageTaskTime) -> {
                    averageTaskTimeProperty.set(averageTaskTime);
                },
                (agentTaskData) -> {
                    AgentTaskController agentTaskController = tasksControllerMapping.get(agentTaskData.getTaskId());
                    if (agentTaskController != null) {
                        agentTaskController.setTotalTime(agentTaskData.getTotalTaskTime());
                    }
                    else {
                        //TODO erez: fix bug of task time
                    }
                });
    }

    private void createAgentTask(String agentId, Integer taskId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AgentTask.fxml"));
            Parent load = fxmlLoader.load();

            AgentTaskController agentTaskController = fxmlLoader.getController();
            agentTaskController.setId(agentId);
            agentTaskController.setTotalTime(0L);

            flowPaneCandidates.getChildren().add(load);
            tasksControllerMapping.put(taskId, agentTaskController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task progress bar
        taskProgressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        taskProgressLabel.textProperty().bind(
                Bindings.concat("Task progress: ", Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %")));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.processedTasksLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

}
