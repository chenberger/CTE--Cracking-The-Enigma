package Engine.AgentsManager;

import BruteForce.DecryptionCandidateFormat;
import BruteForce.DecryptionCandidateTaskHandler;
import BruteForce.TasksManager;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.AgentTaskData;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Keyboard;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import EnigmaMachineException.WordNotValidInDictionaryException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent {
    private Long tasksPulled;
    private Long tasksDone;
    private Integer totalCandidatesFound;
    private final String agentName;
    private String allieName;
    private Long numberOfWorkingThreads;
    private Long tasksPullingInterval;

    public Agent(String agentName) {
        this.agentName = agentName;
        this.totalCandidatesFound = 0;
        this.numberOfWorkingThreads = 1L;
        this.tasksPullingInterval = 1L;
        this.tasksPulled = 0L;
        this.tasksDone = 0L;
    }
    //region Getters and Setters
    public void setAllieName(String allieName) {
        this.allieName = allieName;
    }
    public String getAllieName() {
        return allieName;
    }
    public String getAgentName() {
        return agentName;
    }
    public long getNumberOfWorkingThreads() {
        return numberOfWorkingThreads;
    }
    public void setNumberOfWorkingThreads(long numberOfWorkingThreads) {
        this.numberOfWorkingThreads = numberOfWorkingThreads;
    }
    public long getTasksPullingInterval() {
        return tasksPullingInterval;
    }
    public void setTasksPullingInterval(long tasksPullingInterval) {
        this.tasksPullingInterval = tasksPullingInterval;
    }
    //endregion
    public List<String> getAgentFields() {
        //TODO chen: add the team the agent in, the number of threads, the number of tasks cloned from server each time.
        List<String> agentFields = new ArrayList<>();
        agentFields.add(agentName);
        agentFields.add(numberOfWorkingThreads.toString());
        agentFields.add(tasksPullingInterval.toString());
        return agentFields;
    }
    public Long getNumberOfTasksDone() {
        return tasksDone;
    }
    public void updateTasksDone(Long tasksDone) {
        this.tasksDone = tasksDone;
    }


    public void removeAgentFromAllie() {
        allieName = "";
    }
    public Long getNumberOfTasksPulled(){
        return tasksPulled;
    }
    public Integer getTotalNumberOfCandidatesFound(){
        return totalCandidatesFound;
    }
    public void addNumberOfTasksPulled(Long tasksPulled){
        this.tasksPulled += tasksPulled;
    }
    public void updateNumberOfTasksPulled(Long tasksPulled){
        this.tasksPulled = tasksPulled;
    }
    public void setTotalNumberOfCandidatesFound(int totalCandidatesFound){
        this.totalCandidatesFound = totalCandidatesFound;
    }

    public void stopWorking() {
        //TODO chen: stop the agent from working when the competition is over.
    }
    public void clearAgentsBattleData(){
        tasksPulled = 0L;
        tasksDone = 0L;
        totalCandidatesFound = 0;
    }
}

