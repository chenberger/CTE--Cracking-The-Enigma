package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import javafx.application.Platform;

import java.util.function.Consumer;

public class BruteForceUIAdapter {
    private Consumer<AgentTaskData> updateExistingAgentTask;
    private Consumer<Long> totalProcessedAgentTasks;
    private Consumer<Long> totalTasks;
    private Consumer<Long> missionTotalTime;
    private Consumer<Long> averageTaskTime;
    private Consumer<AgentTaskData> updateExistingAgentTaskTime;

    public BruteForceUIAdapter(Consumer<AgentTaskData> updateExistingAgentTask, Consumer<Long> totalProcessedAgentTasks, Consumer<Long> totalTasks, Consumer<Long> missionTotalTime, Consumer<Long> averageTaskTime, Consumer<AgentTaskData> updateExistingAgentTaskTime) {
        this.updateExistingAgentTask = updateExistingAgentTask;
        this.totalProcessedAgentTasks = totalProcessedAgentTasks;
        this.totalTasks = totalTasks;
        this.missionTotalTime = missionTotalTime;
        this.averageTaskTime = averageTaskTime;
        this.updateExistingAgentTaskTime = updateExistingAgentTaskTime;
    }

    public void updateTotalAgentsTasks(Long totalTasks) {
        Platform.runLater(
                () -> {
                    this.totalTasks.accept(totalTasks);
                }
        );
    }

    public void updateMissionTotalTime(long missionTotalTime) {
        Platform.runLater(
                () -> {
                    this.missionTotalTime.accept(missionTotalTime);
                }
        );
    }

    public void updateAverageTaskTime(long averageTaskTime) {
        Platform.runLater(
                () -> {
                    this.averageTaskTime.accept(averageTaskTime);
                }
        );
    }

    public void updateTotalProcessedAgentTasks(Long processedAgentTasksAmount) {
        Platform.runLater(
                () -> totalProcessedAgentTasks.accept(processedAgentTasksAmount)
        );
    }

    public void updateExistingAgentTask(AgentTaskData agentTaskData) {
        Platform.runLater(
                () -> {
                    this.updateExistingAgentTask.accept(agentTaskData);
                }
        );
    }

    public void updateExistingAgentTaskTime(AgentTaskData agentTaskData) {
        Platform.runLater(
                () -> {
                    this.updateExistingAgentTaskTime.accept(agentTaskData);
                }
        );
    }
}
