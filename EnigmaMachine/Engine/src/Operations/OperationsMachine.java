package Operations;

import TDO.MachineDetails;

public interface OperationsMachine {
    public void automaticSettingsInitialize();
    public void manualSettingsInitialize();
    public void setMachineDetails();
    public MachineDetails getMachineDetails() throws Exception;
    public void analyzeMachineHistoryAndStatistics();
    public void processInput();
}
