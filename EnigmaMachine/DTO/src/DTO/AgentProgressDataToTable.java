package DTO;

public class AgentProgressDataToTable {
    private String agentName;
    private int totalCandidates;
    private long tasksPulled;


    public AgentProgressDataToTable(String agentName, int totalCandidates, long tasksPulled) {
        this.agentName = agentName;
        this.totalCandidates = totalCandidates;
        this.tasksPulled = tasksPulled;
    }

    // getters and setters
    public String getAgentName() {
        return agentName;
    }
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    public int getTotalCandidates() {
        return totalCandidates;
    }
    public void setTotalCandidates(int totalCandidates) {
        this.totalCandidates = totalCandidates;
    }
    public long getTasksPulled() {
        return tasksPulled;
    }
    public void setTasksPulled(long tasksPulled) {
        this.tasksPulled = tasksPulled;
    }
    // end getters and setters

}
