package Operations;

public interface OperationsMachine {
    public void automaticSettingsInitialize(AutomaticSettingsInitializer automaticSettingsInitializer);
    public void manualSettingsInitialize(ManualSettingsInitializer manualSettingsInitializer);
    public void setMachineDetails();
    public void viewMachineDetails(MachineDetailsViewer machineDetailsViewer);
    public void analyzeMachineHistoryAndStatistics();
    public void processInput();
}
