package Engine.AlliesManager;

import Engine.AgentsManager.Agent;
import BruteForce.DecryptionManager;
import BruteForce.DifficultyLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Allie {
    private String Name;
    private final List<Agent> agents;
    private DecryptionManager decryptionManager;
    private long taskSize;

    private String battleName;
    private boolean isReady;
    private boolean contestOnline;

    private DifficultyLevel level;
    public Allie(String name){
        this.Name = name;
        this.agents = new ArrayList<>();
        this.decryptionManager = new DecryptionManager();
        this.taskSize = 1000;
        this.battleName = "";
        this.isReady = false;
        this.contestOnline = false;
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
    public List<String> getAgentsNames(){
        return agents.stream().map(Agent::getAgentName).collect(Collectors.toList());
    }
    public void setBattleField(String battleFieldName) {
        this.battleName = battleFieldName;
    }
    public String getBattleName() {
        return battleName;
    }
    public long getTaskSize() {
        return taskSize;
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void removeTeamFromBattle() {
        battleName = "";
    }

    public void removeAgentsFromAllie() {
        for(Agent agent : agents){
            agent.removeAgentFromAllie();
        }
        agents.clear();
    }

    public void startContest() {
        //TODO: start contest
    }
}
