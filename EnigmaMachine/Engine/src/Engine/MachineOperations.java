package Engine;

import DTO.BruteForceTask;
import DTO.MachineDetails;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import EnigmaMachineException.*;

import java.io.IOException;

public interface MachineOperations {
    void setSettingsAutomatically() throws Exception;

    void startBruteForceDeciphering(BruteForceTask bruteForceTask, BruteForceUIAdapter bruteForceUiAdapter, Runnable onFinish) throws Exception;


    void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException, SettingsNotInitializedException;
    MachineDetails displaySpecifications() throws Exception;
    String analyzeHistoryAndStatistics() throws Exception;
    String processInput(String inputToProcess, Boolean processFromDictionary) throws Exception;
    void saveStateMachineToFile(String path) throws IOException, MachineNotExistsException;

    void loadStateMachineFromFile(String path) throws IOException, ClassNotFoundException;
}
