package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import BruteForce.DecryptionCandidateFormat;

public class AgentTaskData {
    private DecryptionCandidateFormat decryptionCandidateFormat;
    private int taskId;
    private long totalTaskTime;
    private String agentId;

    public AgentTaskData(int taskID, String agentId, DecryptionCandidateFormat decryptionCandidateFormat) {
        this.decryptionCandidateFormat = decryptionCandidateFormat;
        this.taskId = taskID;
        this.agentId = agentId;
    }

    public AgentTaskData(int taskID, long totalTaskTime) {
        this.totalTaskTime = totalTaskTime;
        this.taskId = taskID;
    }

    public DecryptionCandidateFormat getDecryptionCandidateFormat() {
        return decryptionCandidateFormat;
    }

    public int getTaskId() {
        return taskId;
    }

    public long getTotalTaskTime() {
        return totalTaskTime;
    }

    public String getAgentId() {
        return agentId;
    }
}
