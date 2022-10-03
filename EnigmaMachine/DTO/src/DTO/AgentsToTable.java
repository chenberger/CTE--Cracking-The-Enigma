package DTO;

import java.util.List;

public class AgentsToTable {
    List<String> agents;
    List<Long> threadsForEachAgent;
    List<Long> tasksTakenOnceForEachAgent;

    public AgentsToTable(List<String> agents, List<Long> threadsForEachAgent, List<Long> tasksTakenOnceForEachAgent) {
        this.agents = agents;
        this.threadsForEachAgent = threadsForEachAgent;
        this.tasksTakenOnceForEachAgent = tasksTakenOnceForEachAgent;
    }
    //region getters and setters
    public List<String> getAgents() {
        return agents;
    }
    public void setAgents(List<String> agents) {
        this.agents = agents;
    }
    public List<Long> getThreadsForEachAgent() {
        return threadsForEachAgent;
    }
    public void setThreadsForEachAgent(List<Long> threadsForEachAgent) {
        this.threadsForEachAgent = threadsForEachAgent;
    }
    public List<Long> getTasksTakenOnceForEachAgent() {
        return tasksTakenOnceForEachAgent;
    }
    public void setTasksTakenOnceForEachAgent(List<Long> tasksTakenOnceForEachAgent) {
        this.tasksTakenOnceForEachAgent = tasksTakenOnceForEachAgent;
    }
    //endregion
}
