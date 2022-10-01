package DTO;

public class AlliesToTable {
    private String teamName;
    private Integer numberOfAgents;
    private Integer TaskSize;

    private String boatName;

    AlliesToTable(String teamName, Integer numberOfAgents, Integer TaskSize, String boatName) {
        this.teamName = teamName;
        this.numberOfAgents = numberOfAgents;
        this.TaskSize = TaskSize;
        this.boatName = boatName;
    }
    //region getters and setters
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public Integer getNumberOfAgents() {
        return numberOfAgents;
    }
    public void setNumberOfAgents(Integer numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }
    public Integer getTaskSize() {
        return TaskSize;
    }
    public void setTaskSize(Integer taskSize) {
        TaskSize = taskSize;
    }
    public String getBoatName() {
        return boatName;
    }
    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }
    //endregion
}
