package DTO;

public class AgentCandidatesInformationToTable {
    private String candidateString;
    private Long numberOfTask;
    private String configurationOfTask;

    public AgentCandidatesInformationToTable(String candidateString, Long numberOfTask, String configurationOfTask) {
        this.candidateString = candidateString;
        this.numberOfTask = numberOfTask;
        this.configurationOfTask = configurationOfTask;
    }
    // Getters and Setters
    public String getCandidateString() {
        return candidateString;
    }
    public Long getNumberOfTask() {
        return numberOfTask;
    }
    public String getConfigurationOfTask() {
        return configurationOfTask;
    }
    public void setCandidateString(String candidateString) {
        this.candidateString = candidateString;
    }
    public void setNumberOfTask(Long numberOfTask) {
        this.numberOfTask = numberOfTask;
    }
    public void setConfigurationOfTask(String configurationOfTask) {
        this.configurationOfTask = configurationOfTask;
    }
    // end of Getters and Setters
}
