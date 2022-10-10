package DTO;

import java.util.List;

public class TeamNameColumn {
    private String teamName;

    public TeamNameColumn(String teamName) {
        this.teamName = teamName;
    }
    //region Getters and Setters
    public String getTeamNames() {
        return teamName;
    }
    public void setTeamNames(String teamNames) {
        this.teamName = teamNames;
    }
}
