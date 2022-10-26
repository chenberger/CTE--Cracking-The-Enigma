package Engine.AlliesManager;

import Engine.AgentsManager.Agent;
import BruteForce.DecryptionManager;
import BruteForce.DifficultyLevel;
import Engine.EngineManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Allie {
    private String Name;
    private final List<Agent> agents;
    private final List<Agent> agentsParticipatingInDecryption;
    private DecryptionManager decryptionManager;
    private long taskSize;

    private String battleName;
    private boolean isReady;
    private boolean contestOnline;

    private long tasksProduced;
    private long tasksCompleted;
    private long totalNumberOfTasks;

    private DifficultyLevel level;
    public Allie(String name){
        this.Name = name;
        this.agents = new ArrayList<>();
        this.agentsParticipatingInDecryption = new ArrayList<>();
        this.decryptionManager = new DecryptionManager();
        decryptionManager.setAllie(this);
        this.taskSize = 0;
        this.battleName = "";
        this.isReady = true;
        this.contestOnline = false;
        this.tasksCompleted = 0;
        this.tasksProduced = 0;
        this.totalNumberOfTasks = 0;
    }
    public void setLevel(String level) {
        this.level = DifficultyLevel.valueOf(level.toUpperCase());
    }
    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }
    public void setAgentsParticipatingInDecryption(){
        for(Agent agent : agents){
            if(!agentsParticipatingInDecryption.contains(agent)){
                agentsParticipatingInDecryption.add(agent);
            }
        }
    }
    public Long getTasksProduced(){
        return tasksProduced;
    }
    public Long getTasksCompleted(){
        return tasksCompleted;
    }
    public Long getTotalNumberOfTasks(){
        return totalNumberOfTasks;
    }
    public void setTotalNumberOfTasks(long totalNumberOfTasks) {this.totalNumberOfTasks = totalNumberOfTasks;}

    public void increaseTasksCompleted(long tasksCompleted) {
        this.tasksCompleted += tasksCompleted;
    }
    public void updateTasksCompleted(long tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }
    public synchronized void increaseTasksProduced(long tasksProduced) {
        this.tasksProduced = tasksProduced;
    }

    public List<Agent> getAgentsParticipatingInDecryption() {
        return agentsParticipatingInDecryption;
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


    public void startContest(String processedMessage, EngineManager engineManager) {
        //setAgentsParticipatingInDecryption();
        decryptionManager.startDeciphering(processedMessage, taskSize, level, engineManager);
    }

    public DecryptionManager getDecryptionManager() {
        return decryptionManager;
    }

    public void stopContest() {
        decryptionManager.stopDeciphering();
        for(Agent agent : agentsParticipatingInDecryption){
            agent.stopWorking();
        }
        //battleName = "";
        agentsParticipatingInDecryption.clear();

        isReady = false;
        contestOnline = false;

    }

    public Long getTotalTasksCompleted() {
        long totalTasksCompleted = 0;
        for(Agent agent : agents){
            totalTasksCompleted += agent.getNumberOfTasksDone();
        }
        return totalTasksCompleted;
    }

    public void clearAgentsProcessData() {
        for(Agent agent : agentsParticipatingInDecryption){
            agent.clearAgentsBattleData();
        }
    }
}
