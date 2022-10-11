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

    private final String agentName;
    private String allieName;
    private Long numberOfWorkingThreads = 4L;
    //private TasksManager tasksManager;
    private Long tasksPullingInterval = 50L;

    public Agent(String agentName) {
        this.agentName = agentName;
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

    public void removeAgentFromAllie() {
        allieName = "";
    }
}

