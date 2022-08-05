package Operations;

import EnigmaMachine.SettingsFormat;
import TDO.MachineDetails;

public interface OperationsMachine {
    public void automaticSettingsInitialize();
    public void manualSettingsInitialize(SettingsFormat settingsFormat) throws Exception;
    public void resetMachineSettings();
    public MachineDetails getMachineDetails() throws Exception;
    public void analyzeMachineHistoryAndStatistics();
    public String processInput(String inputToProcess);
}
