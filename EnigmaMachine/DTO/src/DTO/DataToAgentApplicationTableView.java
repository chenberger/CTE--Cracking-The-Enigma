package DTO;

public class DataToAgentApplicationTableView {
    private String candidateString;
    private long numberOfTask;
    private String configurationOfTask;

    public DataToAgentApplicationTableView(String candidateString, long numberOfTask, String configurationOfTask) {
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
}
