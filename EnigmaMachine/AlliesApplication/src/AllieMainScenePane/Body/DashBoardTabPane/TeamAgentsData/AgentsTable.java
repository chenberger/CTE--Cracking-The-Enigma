package AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData;

public class AgentsTable {
    private String agentName;
    private Long numOfThreads;
    private Long tasksTakenOnce;

    public AgentsTable(String agentName, Long numOfThreads, Long tasksTakenOnce) {
        this.agentName = agentName;
        this.numOfThreads = numOfThreads;
        this.tasksTakenOnce = tasksTakenOnce;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Long getNumOfThreads() {
        return numOfThreads;
    }

    public void setNumOfThreads(Long numOfThreads) {
        this.numOfThreads = numOfThreads;
    }

    public Long getTasksTakenOnce() {
        return tasksTakenOnce;
    }

    public void setTasksTakenOnce(Long tasksTakenOnce) {
        this.tasksTakenOnce = tasksTakenOnce;
    }
}

