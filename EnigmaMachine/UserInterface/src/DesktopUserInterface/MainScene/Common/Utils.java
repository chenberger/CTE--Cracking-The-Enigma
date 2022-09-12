package DesktopUserInterface.MainScene.Common;

import Engine.EngineManager;
import EnigmaMachineException.MachineNotExistsException;
import EnigmaMachineException.SettingsNotInitializedException;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static void checkIfMachineExistsAndInitialized(EngineManager enigmaMachineEngine) throws MachineNotExistsException, SettingsNotInitializedException {
        if (!enigmaMachineEngine.isMachineExists()) {
            throw new MachineNotExistsException();
        }
        else if(!enigmaMachineEngine.isMachineSettingInitialized()) {
            throw new SettingsNotInitializedException();
        }
    }

    public static String formatDuration(long millis) {
        long MM = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        return String.format("%02d:%02d", MM, SS);
    }
}
