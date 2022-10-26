package Engine;

import BruteForce.DifficultyLevel;
import DTO.AgentCandidatesInformation;
import Engine.AlliesManager.Allie;
import Jaxb.Schema.Generated.CTEBattlefield;

import java.util.ArrayList;
import java.util.List;

public class BattleField {
    /* this class is the battle field class, it contains the battle field name, the level of the battle field and the number of allies in the battle field */
    private String battleFieldName;
    private String winner;

    private List<AgentCandidatesInformation> agentsCandidatesInformation;
    private String processedMessage;
    private String originalMessage;
    private int numberOfTeamsInBattleField;
    private DifficultyLevel level;
    private int numberOfAlliesToStartBattle;
    private int numberOfAlliesThatAreReady;

    private List<Allie> alliesInBattleField;

    public BattleField(String battleFieldName, String level, int allies) {
        this.battleFieldName = battleFieldName;
        this.level = stringToDifficultyLevel(level);
        this.numberOfAlliesToStartBattle = allies;
        this.numberOfTeamsInBattleField = 0;
        this.alliesInBattleField = new ArrayList<>();
        this.processedMessage = "";
        this.numberOfAlliesThatAreReady = 0;
        this.agentsCandidatesInformation = new ArrayList<>();
        this.originalMessage = "";
        winner = "";
    }
    public BattleField(){
        this.battleFieldName = "";
        this.level = DifficultyLevel.EASY;
        this.numberOfAlliesToStartBattle = 0;
    }
    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }
    public String getOriginalMessage() {
        return originalMessage;
    }
    public void addTeamToBattleField(){
        this.numberOfTeamsInBattleField++;
    }
    public void removeTeamFromBattleField(){
        this.numberOfTeamsInBattleField--;
    }
    public int getNumberOfTeamsInBattleField() {
        return numberOfTeamsInBattleField;
    }
    public boolean isBattleFieldFull(){
        return this.numberOfTeamsInBattleField == this.numberOfAlliesToStartBattle;
    }
    private DifficultyLevel stringToDifficultyLevel(String level) {
        switch (level) {
            case "Easy":
                return DifficultyLevel.EASY;
            case "Medium":
                return DifficultyLevel.MEDIUM;
            case "Hard":
                return DifficultyLevel.HARD;
            case "Insane":
                return DifficultyLevel.IMPOSSIBLE;
            default:
                return DifficultyLevel.EASY;
        }
    }
    public void setDifficultyLevel(String level) {
        this.level = stringToDifficultyLevel(level);
    }
    // getters and setters region
    public String getProcessedMessage() {
        return processedMessage;
    }
    public void setProcessedMessage(String processedMessage) {
        this.processedMessage = processedMessage;
    }
    public String getBattleFieldName() {
        return battleFieldName;
    }
    public void setBattleFieldName(String battleFieldName) {
        this.battleFieldName = battleFieldName;
    }
    public DifficultyLevel getLevel() {
        return level;
    }
    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }
    public int getNumberOfAlliesToStartBattle() {
        return numberOfAlliesToStartBattle;
    }
    public void setNumberOfAlliesToStartBattle(int numberOfAlliesToStartBattle) {
        this.numberOfAlliesToStartBattle = numberOfAlliesToStartBattle;
    }
    private void setNumberOfTeamsReadyToZero(){
        this.numberOfAlliesThatAreReady = 0;
    }
    public void addTeamToReady(){
        this.numberOfAlliesThatAreReady++;
    }
    public void setBattleField(CTEBattlefield cteBattlefield) {
        this.battleFieldName = cteBattlefield.getBattleName();
        this.level = stringToDifficultyLevel(cteBattlefield.getLevel());
        this.numberOfAlliesToStartBattle = cteBattlefield.getAllies();
    }

    public Object getBattleName() {
        return this.battleFieldName;
    }

    synchronized public void addTeam(Allie allie) {
        this.alliesInBattleField.add(allie);
        this.numberOfTeamsInBattleField++;
    }

    public List<String> getTeams() {
        List<String> teams = new ArrayList<>();
        for (Allie allie : alliesInBattleField) {
            teams.add(allie.getTeamName());
        }
        return teams;
    }

    public List<Allie> getAlliesInBattle() {
        return alliesInBattleField;
    }

    public void removeAlliesFromBattle() {
        for(Allie allie : alliesInBattleField){
            allie.removeTeamFromBattle();
        }
        this.alliesInBattleField.clear();
        this.numberOfTeamsInBattleField = 0;
    }
    public int getNumberOfAlliesThatAreReady() {
        return numberOfAlliesThatAreReady;
    }
    // end of getters and setters region
    public void startContest(EngineManager engineManager){
        for(Allie allie : alliesInBattleField){
            allie.startContest(processedMessage, engineManager);
        }
    }
    public void addAgentCandidateInformationToList(AgentCandidatesInformation agentCandidatesInformation){
        this.agentsCandidatesInformation.add(agentCandidatesInformation);
    }
    public List<AgentCandidatesInformation> getAgentsCandidatesInformation() {
        return agentsCandidatesInformation;
    }
    public void stopContest(){
        for(Allie allie : alliesInBattleField){
            allie.stopContest();
        }
        alliesInBattleField.clear();
        numberOfTeamsInBattleField = 0;
        numberOfAlliesThatAreReady = 0;
    }

    public String getWinner() {
        return winner;
    }
    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void clearCandidatesInformationList() {
        agentsCandidatesInformation.clear();
    }

    public void removeAllyFromBattle(Allie allie) {
        this.numberOfTeamsInBattleField--;
        alliesInBattleField.remove(allie);
    }
}
