package DTO;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.*;

import java.util.List;

public class TaskToAgent {
    SectorsCodeAsJson sectorsCodeAsJson;
    private long TaskSize;
    //private StartingRotorPositionSector currentStartingRotorsPositions;
    //private EnigmaMachine enigmaMachine;

    private String encryptedMessage;
    private String agentName;

    public TaskToAgent(long taskSize, StartingRotorPositionSector currentStartingRotorsPositions, EnigmaMachine enigmaMachine, String encryptedMessage) throws CloneNotSupportedException {
        TaskSize = taskSize;
        this.agentName = "";
        //this.currentStartingRotorsPositions = currentStartingRotorsPositions;
        this.encryptedMessage = encryptedMessage;
        List<Sector> sectors = enigmaMachine.getCurrentSettingsFormat().getSettingsFormat();
        sectorsCodeAsJson = new SectorsCodeAsJson((RotorIDSector)sectors.get(0), currentStartingRotorsPositions, (ReflectorIdSector)sectors.get(2), (PluginBoardSector) sectors.get(3));
    }
    //setters and getters
    public long getTaskSize() {
        return TaskSize;
    }
    public void setTaskSize(int taskSize) {
        TaskSize = taskSize;
    }
   //public StartingRotorPositionSector getCurrentStartingRotorsPositions() {
   //    return currentStartingRotorsPositions;
   //}
   //public void setCurrentStartingRotorsPositions(StartingRotorPositionSector currentStartingRotorsPositions) {
   //    this.currentStartingRotorsPositions = currentStartingRotorsPositions;
   //}
   //public EnigmaMachine getEnigmaMachine() {
   //    return enigmaMachine;
   //}
   //public void setEnigmaMachine(EnigmaMachine enigmaMachine) {
   //    this.enigmaMachine = enigmaMachine;
   //}
    public String getEncryptedMessage() {
        return encryptedMessage;
    }
    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }
    public SectorsCodeAsJson getSectorsCodeAsJson() {
        return sectorsCodeAsJson;
    }
    public void setSectorsCodeAsJson(SectorsCodeAsJson sectorsCodeAsJson) {
        this.sectorsCodeAsJson = sectorsCodeAsJson;
    }
    public String getAgentName() {
        return agentName;
    }
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    //end of setters and getters
}
