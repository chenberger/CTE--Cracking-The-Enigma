package Engine.AlliesManager;

import BruteForce.Agent;
import BruteForce.DecryptionManager;
import BruteForce.DifficultyLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Allie {
    private String Name;
    private final List<Agent> agents;
    private DecryptionManager decryptionManager;
    private long taskSize;

    private String battleName;

    private DifficultyLevel level;
    public Allie(String name){
        Name = name;
        agents = new ArrayList<>();
        decryptionManager = new DecryptionManager();
        this.taskSize = 0;
    }
    public void setLevel(String level) {
        this.level = DifficultyLevel.valueOf(level.toUpperCase());
    }
    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }
    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public List<String> getAllieFields() {
        List<String> allieFields = new ArrayList<>();
        allieFields.add(Name);
        allieFields.add(String.valueOf(taskSize));
        allieFields.add(String.valueOf(agents.size()));
        return allieFields;
    }

    public String getTeamName() {
        return Name;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public long getTaskSize() {
        return taskSize;
    }
}
