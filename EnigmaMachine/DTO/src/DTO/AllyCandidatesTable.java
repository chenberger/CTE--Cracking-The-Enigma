package DTO;

public class AllyCandidatesTable {
    private String candidateString;
    private String teamName;
    private String codeConfiguration;

    public AllyCandidatesTable(String candidateString, String teamName, String codeConfiguration) {
        this.candidateString = candidateString;
        this.teamName = teamName;
        this.codeConfiguration = codeConfiguration;
    }
    // getters and setters
    public String getCandidateString() {
        return candidateString;
    }
    public void setCandidateString(String candidateString) {
        this.candidateString = candidateString;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public String getCodeConfiguration() {
        return codeConfiguration;
    }
    public void setCodeConfiguration(String codeConfiguration) {
        this.codeConfiguration = codeConfiguration;
    }
    // end getters and setters
}
