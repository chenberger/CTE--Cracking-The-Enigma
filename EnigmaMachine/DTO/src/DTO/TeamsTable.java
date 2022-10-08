package DTO;

public class TeamsTable {

    private String teamName;
    private Integer numOfAgents;
    private Long taskSize;


    public TeamsTable(String teamName, Integer numOfAgents, Long taskSize) {
        this.teamName = teamName;
        this.numOfAgents = numOfAgents;
        this.taskSize = taskSize;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getNumOfAgents() {
        return numOfAgents;
    }

    public void setNumOfAgents(Integer numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    public Long getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(Long taskSize) {
        this.taskSize = taskSize;
    }
}
