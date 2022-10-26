package Engine.UBoatManager;

import Engine.BattleField;
import Engine.Dictionary;
import Engine.EngineManager;
import EnigmaMachine.EnigmaMachine;
import Jaxb.Schema.Generated.CTEBattlefield;

import java.util.List;

public class UBoat {
    private String name;
    private final String CONTEST_NOT_STARTED = "Contest not Started";
    private final String CONTEST_STARTED = "Contest Started";
    private BattleField battleField;
    //private final EnigmaMachine enigmaMachine;
    //private final StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer;
    private final EngineManager engineManager;
    private String currentProcessedMessage;
    private String contestStatus;
    private boolean isReady;
    private boolean contestOnline;

    public UBoat(String name, CTEBattlefield battleField, EnigmaMachine enigmaMachine, Dictionary dictionary) {
        this.name = name;
        this.battleField = new BattleField(battleField.getBattleName(), battleField.getLevel(), battleField.getAllies());
        this.battleField.setDifficultyLevel(battleField.getLevel());
        //this.enigmaMachine = enigmaMachine;
        this.isReady = false;
        //this.statisticsAndHistoryAnalyzer = new StatisticsAndHistoryAnalyzer();
        this.engineManager = new EngineManager(enigmaMachine);
        this.contestStatus = CONTEST_NOT_STARTED;
        this.currentProcessedMessage = "";
        this.contestOnline = false;
        setEngineDictionary(dictionary);
    }
    //region getters and setters
    public String getName() {
        return name;
    }
    public void setEngineDictionary(Dictionary dictionary) {
        Dictionary newDictionary = new Dictionary(dictionary.cloneWords(), dictionary.cloneExcludeChars());
        engineManager.setDictionary(newDictionary);
    }
    public void setName(String name) {
        this.name = name;
    }
    public BattleField getBattleField() {
        return battleField;
    }
    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }
    public void setContestStatusStarted(){
        this.contestStatus = CONTEST_STARTED;
    }
    public void setContestStatusNotStarted(){
        this.contestStatus = CONTEST_NOT_STARTED;
    }
    public void setCurrentProcessedMessage(String message){
        this.currentProcessedMessage = message;
    }
    public String getCurrentProcessedMessage(){
        return this.currentProcessedMessage;
    }
    //public EnigmaMachine getEnigmaMachine() {
    //    return enigmaMachine;
    //}

    public EngineManager getEngineManager() {
        return engineManager;
    }

    public List<String> getTeams() {
        return battleField.getTeams();
    }

    public void removeAlliesFromBattle() {
        battleField.removeAlliesFromBattle();
    }

    public String getContestStatus() {
        return contestStatus;
    }
    public void setReady(boolean ready) {
        isReady = ready;
    }
    public boolean isReady() {
        return isReady;
    }
    public void startContestIfNecessary(){
        if(isReady && battleField.getNumberOfAlliesThatAreReady() == battleField.getNumberOfAlliesToStartBattle()){
            contestOnline = true;
            setContestStatusStarted();
            startContest();
        }
    }
    public void startContest(){
        this.contestOnline = true;
        this.contestStatus = CONTEST_STARTED;
        battleField.startContest(engineManager);
    }
    public Boolean isContestOnline(){
        return this.contestOnline;
    }

    public void setContestOver() {
        this.contestOnline = false;
        this.contestStatus = CONTEST_NOT_STARTED;
        isReady = false;
    }
}
