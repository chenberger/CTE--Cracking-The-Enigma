package AllieMainScenePane.Body.DashBoardTabPane.ContestData;

public class OnLineContestsTable {
    private String battleName;
    private String boatName;
    private String contestStatus;
    private String difficulty;
    private String teamsRegisteredAndNeeded;

    public OnLineContestsTable(String battleName, String boatName, String contestStatus, String difficulty, String teamsRegisteredAndNeeded) {
        this.battleName = battleName;
        this.boatName = boatName;
        this.contestStatus = contestStatus;
        this.difficulty = difficulty;
        this.teamsRegisteredAndNeeded = teamsRegisteredAndNeeded;
    }

    public String getBattleName() {
        return battleName;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getContestStatus() {
        return contestStatus;
    }

    public void setContestStatus(String contestStatus) {
        this.contestStatus = contestStatus;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTeamsRegisteredAndNeeded() {
        return teamsRegisteredAndNeeded;
    }

    public void setTeamsRegisteredAndNeeded(String teamsRegisteredAndNeeded) {
        this.teamsRegisteredAndNeeded = teamsRegisteredAndNeeded;
    }
}
