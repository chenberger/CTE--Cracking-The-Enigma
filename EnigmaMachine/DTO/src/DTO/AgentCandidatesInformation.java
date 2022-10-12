package DTO;

public class AgentCandidatesInformation {
    private int numberOfCandidates;
    private String teamName;

    private long tasksPulled;
    private long tasksCompleted;
    private String agentName;
    private String candidateString;
    private long numberOfTask;
    private String configurationOfTask;

    public AgentCandidatesInformation(String candidateString, long numberOfTask, String configurationOfTask, String agentName) {
        this.candidateString = candidateString;
        this.numberOfTask = numberOfTask;
        this.configurationOfTask = configurationOfTask;
        this.agentName = agentName;
        this.tasksPulled = tasksPulled;
        this.tasksCompleted = tasksCompleted;
        this.teamName = "";
        numberOfCandidates = 0;
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
    public String getAgentName() {
        return agentName;
    }
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    public long getTasksPulled() {
        return tasksPulled;
    }
    public void setTasksPulled(long tasksPulled) {
        this.tasksPulled = tasksPulled;
    }
    public long getTasksCompleted() {
        return tasksCompleted;
    }
    public void setTasksCompleted(long tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public int getNumberOfCandidates() {
        return numberOfCandidates;
    }
    public void setNumberOfCandidates(int numberOfCandidates) {
        this.numberOfCandidates = numberOfCandidates;
    }
    // end of Getters and Setters
    public void addCandidate(int numberOfCandidates) {
        this.numberOfCandidates += numberOfCandidates;
    }
}
