package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {
    private Consumer<AgentTaskData> introduceNewAgentTask;
    private Consumer<AgentTaskData> updateExistingAgentTask;
    private Consumer<Integer> totalProcessedAgentTasks;
    private Consumer<Integer> totalTasks;
    private Consumer<Integer> missionTotalTime;
    private Consumer<Integer> averageTaskTime;

    public UIAdapter(Consumer<AgentTaskData> introduceNewAgentTask, Consumer<AgentTaskData> updateExistingAgentTask, Consumer<Integer> totalProcessedAgentTasks, Consumer<Integer> totalTasks, Consumer<Integer> missionTotalTime, Consumer<Integer> averageTaskTime) {
        this.introduceNewAgentTask = introduceNewAgentTask;
        this.updateExistingAgentTask = updateExistingAgentTask;
        this.totalProcessedAgentTasks = totalProcessedAgentTasks;
        this.totalTasks = totalTasks;
        this.missionTotalTime = missionTotalTime;
        this.averageTaskTime = averageTaskTime;
    }

    public void updateTotalAgentsTasks(Integer totalTasks) {
        Platform.runLater(
                () -> {
                    this.totalTasks.accept(totalTasks);
                }
        );
    }

    public void updateMissionTotalTime(Integer missionTotalTime) {
        Platform.runLater(
                () -> {
                    this.missionTotalTime.accept(missionTotalTime);
                }
        );
    }

    public void updateAverageTaskTime(Integer averageTaskTime) {
        Platform.runLater(
                () -> {
                    this.averageTaskTime.accept(averageTaskTime);
                }
        );
    }

    public void updateTotalProcessedAgentTasks(int delta) {
        Platform.runLater(
                () -> totalProcessedAgentTasks.accept(delta)
        );
    }

    public void addNewAgentTask(AgentTaskData agentTaskData) {
        Platform.runLater(
                () -> {
                    introduceNewAgentTask.accept(agentTaskData);
                }
        );
    }
}
