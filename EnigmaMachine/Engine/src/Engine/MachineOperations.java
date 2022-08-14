package Engine;

import EnigmaMachineException.*;
import DTO.MachineDetails;

import java.io.IOException;

public interface MachineOperations {
    void setSettingsAutomatically() throws Exception;
    void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException, SettingsNotInitializedException;
    MachineDetails displaySpecifications() throws Exception;
    String analyzeHistoryAndStatistics() throws Exception;
    String processInput(String inputToProcess) throws Exception;


    void saveStateMachineToFile(String path) throws IOException, MachineNotExistsException;

    void loadStateMachineFromFile(String path) throws IOException, ClassNotFoundException;
}
