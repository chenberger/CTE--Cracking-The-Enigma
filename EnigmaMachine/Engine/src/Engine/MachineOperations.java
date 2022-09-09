package Engine;

import DTO.BruteForceTask;
import DTO.MachineDetails;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.UIAdapter;
import EnigmaMachineException.*;

import java.io.IOException;

public interface MachineOperations {
    void setSettingsAutomatically() throws Exception;

    void startBruteForceDeciphering(BruteForceTask bruteForceTask, UIAdapter uiAdapter, Runnable onFinish) throws CloneNotSupportedException, DecryptionMessegeNotInitializedException;

    void startBruteForceDeciphering() throws CloneNotSupportedException;

    void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException, SettingsNotInitializedException;
    MachineDetails displaySpecifications() throws Exception;
    String analyzeHistoryAndStatistics() throws Exception;
    String processInput(String inputToProcess) throws Exception;
    void saveStateMachineToFile(String path) throws IOException, MachineNotExistsException;

    void loadStateMachineFromFile(String path) throws IOException, ClassNotFoundException;
}
