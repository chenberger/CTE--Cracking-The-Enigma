package BruteForce;

import DesktopUserInterface.MainScene.BodyScene.BruteForce.AgentTaskData;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;

public class DecryptionCandidateTaskHandler implements Runnable {
    private BruteForceUIAdapter bruteForceUIAdapter;
    private AgentTaskData agentTaskData;

    public DecryptionCandidateTaskHandler(BruteForceUIAdapter bruteForceUIAdapter, AgentTaskData agentTaskData) {
        this.bruteForceUIAdapter = bruteForceUIAdapter;
        this.agentTaskData = agentTaskData;
    }
    @Override
    public void run() {
        bruteForceUIAdapter.updateExistingAgentTask(agentTaskData);
    }
}
