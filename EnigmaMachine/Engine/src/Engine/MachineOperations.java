package Engine;

import BruteForce.BruteForceUIAdapter;
import DTO.BruteForceTask;
import EnigmaMachineException.*;
import DTO.MachineDetails;

import java.io.IOException;

public interface MachineOperations {
    void setSettingsAutomatically() throws Exception;

    void startBruteForceDeciphering(BruteForceUIAdapter bruteForceUIAdapter, BruteForceTask bruteForceTask) throws CloneNotSupportedException;

    void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException, SettingsNotInitializedException;
    MachineDetails displaySpecifications() throws Exception;
    String analyzeHistoryAndStatistics() throws Exception;
    String processInput(String inputToProcess) throws Exception;
    void saveStateMachineToFile(String path) throws IOException, MachineNotExistsException;

    void loadStateMachineFromFile(String path) throws IOException, ClassNotFoundException;
}
