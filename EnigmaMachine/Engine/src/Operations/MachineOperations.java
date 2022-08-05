package Operations;

import EnigmaMachine.SettingsFormat;
import TDO.MachineDetails;

public interface MachineOperations {
    public void setSettingsAutomatically() throws Exception;
    public void resetSettings();
    public MachineDetails displaySpecifications() throws Exception;
    public void analyzeHistoryAndStatistics();
    public String processInput(String inputToProcess);
}
