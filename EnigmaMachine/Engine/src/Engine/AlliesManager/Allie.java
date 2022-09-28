package Engine.AlliesManager;

import BruteForce.Agent;
import BruteForce.DecryptionManager;
import BruteForce.DifficultyLevel;

import java.util.ArrayList;
import java.util.List;

public class Allie {
    private String Name;
    private final List<Agent> agents;
    private DecryptionManager decryptionManager;

    private String battleName;

    private DifficultyLevel level;
    public Allie(String name){
        Name = name;
        agents = new ArrayList<>();
        decryptionManager = new DecryptionManager();
    }
    public void setLevel(String level) {
        this.level = DifficultyLevel.valueOf(level.toUpperCase());
    }
    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }
}
