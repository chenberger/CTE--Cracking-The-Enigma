package DesktopUserInterface.MainScene.Common;

import Engine.EngineManager;
import EnigmaMachineException.MachineNotExistsException;
import EnigmaMachineException.SettingsNotInitializedException;

public class Utils {
    public static void checkIfMachineExistsAndInitialized(EngineManager enigmaMachineEngine) throws MachineNotExistsException, SettingsNotInitializedException {
        if (!enigmaMachineEngine.isMachineExists()) {
            throw new MachineNotExistsException();
        }
        else if(!enigmaMachineEngine.isMachineSettingInitialized()) {
            throw new SettingsNotInitializedException();
        }
    }
}
