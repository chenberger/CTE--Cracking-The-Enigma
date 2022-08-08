package Engine;

import EnigmaMachineException.*;
import TDO.MachineDetails;

public interface MachineOperations {
    public void setSettingsAutomatically() throws Exception;
    public void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException;
    public MachineDetails displaySpecifications() throws Exception;
    public String analyzeHistoryAndStatistics() throws Exception;
    public String processInput(String inputToProcess) throws Exception;
}
