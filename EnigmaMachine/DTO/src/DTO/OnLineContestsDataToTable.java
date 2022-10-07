package DTO;

import BruteForce.DifficultyLevel;

import java.util.List;
import java.util.Map;

public class OnLineContestsDataToTable {
    private List<String> battleNames;
    private List<String> uBoatNames;
    private List<DifficultyLevel> difficultyLevels;
    private Map<String,String> contestsStatus;
    private Map<String,Integer> numberOfTeamsRegisteredToEachContest;
    private List<Integer> numberOfTeamsNeededToEachContest;

    public OnLineContestsDataToTable(List<String> battleNames, List<String> uBoatNames, List<DifficultyLevel> difficultyLevels, Map<String,String> contestsStatus, Map<String,Integer> numberOfTeamsRegisteredToEachContest, List<Integer> numberOfTeamsNeededToEachContest) {
        this.battleNames = battleNames;
        this.uBoatNames = uBoatNames;
        this.difficultyLevels = difficultyLevels;
        this.contestsStatus = contestsStatus;
        this.numberOfTeamsRegisteredToEachContest = numberOfTeamsRegisteredToEachContest;
        this.numberOfTeamsNeededToEachContest = numberOfTeamsNeededToEachContest;
    }
    //region Getters and Setters
    public List<String> getBattleNames() {
        return battleNames;
    }
    public List<String> getuBoatNames() {
        return uBoatNames;
    }
    public List<DifficultyLevel> getDifficultyLevels() {
        return difficultyLevels;
    }
    public Map<String,String> getContestsStatus() {
        return contestsStatus;
    }
    public Map<String,Integer> getNumberOfTeamsRegisteredToEachContest() {
        return numberOfTeamsRegisteredToEachContest;
    }
    public List<Integer> getNumberOfTeamsNeededToEachContest() {
        return numberOfTeamsNeededToEachContest;
    }
    //endregion
    //region adders
    public void addBattleName(String battleName){
        battleNames.add(battleName);
    }
    public void addUBoatName(String uBoatName){
        uBoatNames.add(uBoatName);
    }
    public void addDifficultyLevel(DifficultyLevel difficultyLevel){
        difficultyLevels.add(difficultyLevel);
    }
    public void addNumberOfTeamsNeededToEachContest(int numberOfTeamsNeededToEachContest){
        this.numberOfTeamsNeededToEachContest.add(numberOfTeamsNeededToEachContest);
    }
    public void addContestStatus(String battleName, String contestStatus){
        contestsStatus.put(battleName,contestStatus);
    }
    public void addNumberOfTeamsRegisteredToEachContest(String battleName, int numberOfTeamsRegisteredToEachContest){
        this.numberOfTeamsRegisteredToEachContest.put(battleName,numberOfTeamsRegisteredToEachContest);
    }
    //endregion
    //update region
    public void updateContestStatus(String battleName, String contestStatus){
        contestsStatus.replace(battleName,contestStatus);
    }
    public void updateNumberOfTeamsRegisteredToEachContest(String battleName, int numberOfTeamsRegisteredToEachContest){
        this.numberOfTeamsRegisteredToEachContest.replace(battleName,numberOfTeamsRegisteredToEachContest);
    }
}
