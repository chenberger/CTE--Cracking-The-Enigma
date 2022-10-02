package DTO;

import java.util.List;

public class AlliesToTable {
    private List<String> teams;
    private List<Integer> numberOfAgentsForEachAllie;
    private List<Long> TasksSize;

    private String boatName;

    public AlliesToTable(List<String> teams, List<Integer> numberOfAgentsForEachAllie, List<Long> TasksSize, String boatName) {
        this.teams = teams;
        this.numberOfAgentsForEachAllie = numberOfAgentsForEachAllie;
        this.TasksSize = TasksSize;
        this.boatName = boatName;
    }
    //region getters and setters
    public List<String> getTeams() {
        return teams;
    }
    public void setTeams(List<String> teams) {
        this.teams = teams;
    }
    public List<Integer> getNumberOfAgentsForEachAllie() {
        return numberOfAgentsForEachAllie;
    }
    public void setNumberOfAgentsForEachAllie(List<Integer> numberOfAgentsForEachAllie) {
        this.numberOfAgentsForEachAllie = numberOfAgentsForEachAllie;
    }
    public List<Long> getTasksSize() {
        return TasksSize;
    }
    public void setTasksSize(List<Long> tasksSize) {
        TasksSize = tasksSize;
    }
    public String getBoatName() {
        return boatName;
    }
    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }
    //endregion
}
