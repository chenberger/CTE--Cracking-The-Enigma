package Engine;

import BruteForce.DifficultyLevel;
import Engine.AlliesManager.Allie;
import Jaxb.Schema.Generated.CTEBattlefield;

import java.util.ArrayList;
import java.util.List;

public class BattleField {
    /* this class is the battle field class, it contains the battle field name, the level of the battle field and the number of allies in the battle field */
    private String battleFieldName;
    private String processedMessage;
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
    }
    public BattleField(){
        this.battleFieldName = "";
        this.level = DifficultyLevel.EASY;
        this.numberOfAlliesToStartBattle = 0;
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
            case "EASY":
                return DifficultyLevel.EASY;
            case "MEDIUM":
                return DifficultyLevel.MEDIUM;
            case "HARD":
                return DifficultyLevel.HARD;
            default:
                return DifficultyLevel.EASY;
        }
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

}
