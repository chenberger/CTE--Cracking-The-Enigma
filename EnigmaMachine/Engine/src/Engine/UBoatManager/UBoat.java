package Engine.UBoatManager;

import BruteForce.DifficultyLevel;
import Engine.BattleField;
import Engine.EngineManager;
import Engine.StatisticsAndHistory.StatisticsAndHistoryAnalyzer;
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

    public UBoat(String name, CTEBattlefield battleField, EnigmaMachine enigmaMachine) {
        this.name = name;
        this.battleField = new BattleField(battleField.getBattleName(), battleField.getLevel(), battleField.getAllies());
        //this.enigmaMachine = enigmaMachine;
        this.isReady = false;
        //this.statisticsAndHistoryAnalyzer = new StatisticsAndHistoryAnalyzer();
        this.engineManager = new EngineManager(enigmaMachine);
    }
    //region getters and setters
    public String getName() {
        return name;
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
