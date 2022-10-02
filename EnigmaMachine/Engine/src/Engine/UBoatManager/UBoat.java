package Engine.UBoatManager;

import Engine.BattleField;
import Engine.Dictionary;
import Engine.EngineManager;
import EnigmaMachine.EnigmaMachine;
import Jaxb.Schema.Generated.CTEBattlefield;

import java.util.List;

public class UBoat {
    private String name;
    private BattleField battleField;
    //private final EnigmaMachine enigmaMachine;
    //private final StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer;
    private final EngineManager engineManager;
    private boolean isReady;

    public UBoat(String name, CTEBattlefield battleField, EnigmaMachine enigmaMachine, Dictionary dictionary) {
        this.name = name;
        this.battleField = new BattleField(battleField.getBattleName(), battleField.getLevel(), battleField.getAllies());
        //this.enigmaMachine = enigmaMachine;
        this.isReady = false;
        //this.statisticsAndHistoryAnalyzer = new StatisticsAndHistoryAnalyzer();
        this.engineManager = new EngineManager(enigmaMachine);
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
    //public EnigmaMachine getEnigmaMachine() {
    //    return enigmaMachine;
    //}

    public EngineManager getEngineManager() {
        return engineManager;
    }

    public List<String> getTeams() {
        return battleField.getTeams();
    }
}
