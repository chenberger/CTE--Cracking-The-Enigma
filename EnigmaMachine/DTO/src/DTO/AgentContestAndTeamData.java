package DTO;

public class AgentContestAndTeamData {
    private String teamName;
    private String battleName;
    private String ContestStatus;

    public AgentContestAndTeamData(String teamName, String battleName, String ContestStatus) {
        this.teamName = teamName;
        this.battleName = battleName;
        this.ContestStatus = ContestStatus;
    }
    // Getters and Setters
    public String getTeamName() {
        return teamName;
    }
    public String getBattleName() {
        return battleName;
    }
    public String getContestStatus() {
        return ContestStatus;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }
    public void setContestStatus(String ContestStatus) {
        this.ContestStatus = ContestStatus;
    }
    // end of Getters and Setters
}
