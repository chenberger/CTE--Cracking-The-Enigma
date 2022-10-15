package DTO;

public class AgentsProgressAndDataTable {
    private String agentName;
    private String tasksPulledAndDone;
    private Integer TotalCandidates;

    public AgentsProgressAndDataTable(String agentName, String tasksPulledAndDone, Integer TotalCandidates) {
        this.agentName = agentName;
        this.tasksPulledAndDone = tasksPulledAndDone;
        this.TotalCandidates = TotalCandidates;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getTasksPulledAndDone() {
        return tasksPulledAndDone;
    }
    public void setTasksPulledAndDone(String tasksPulledAndDone) {
        this.tasksPulledAndDone = tasksPulledAndDone;
    }
    public Integer getTotalCandidates() {
        return TotalCandidates;
    }
    public void setTotalCandidates(Integer totalCandidates) {
        TotalCandidates = totalCandidates;
    }
}
