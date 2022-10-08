package DTO;

public class AgentsProgressAndDataTable {
    private String agentName;
    private Long tasksPulled;
    private Integer TotalCandidates;

    public AgentsProgressAndDataTable(String agentName, Long tasksPulled, Integer TotalCandidates) {
        this.agentName = agentName;
        this.tasksPulled = tasksPulled;
        this.TotalCandidates = TotalCandidates;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Long getTasksPulled() {
        return tasksPulled;
    }
    public void setTasksPulled(Long tasksPulled) {
        this.tasksPulled = tasksPulled;
    }
    public Integer getTotalCandidates() {
        return TotalCandidates;
    }
    public void setTotalCandidates(Integer totalCandidates) {
        TotalCandidates = totalCandidates;
    }
}
