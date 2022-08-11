package TDO;

import EnigmaMachine.*;
import Engine.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class MachineDetails {

    private SettingsFormat settingsFormat;
    private final Map<Integer, Rotor> rotors;
    private final List<Rotor> rotorsInUse;
    private final Map<RomanNumber, Reflector> reflectors;
    private final Reflector reflectorInUse;
    private final Set<Character> keyboard;
    private final PluginBoard pluginBoard;
    private int messagesCounter;

    public MachineDetails(Map<Integer, Rotor> rotors, List<Rotor> rotorsInUse, Map<RomanNumber, Reflector> reflectors, Reflector reflectorInUse, Set<Character> keyboard, PluginBoard pluginBoard, int messagesCounter, SettingsFormat settingsFormat) {
        this.rotors = rotors;
        this.rotorsInUse = rotorsInUse;
        this.reflectors = reflectors;
        this.reflectorInUse = reflectorInUse;
        this.keyboard = keyboard;
        this.pluginBoard = pluginBoard;
        this.settingsFormat = settingsFormat;
        this.messagesCounter = messagesCounter;
    }

    public void initializeSettingFormat() throws Exception {
        settingsFormat = new SettingsFormat();
        settingsFormat.addSector(getCurrentIdRotorsInUseSector());
        settingsFormat.addSector(getCurrentInitialRotorPositionRotorsInUseSector());
        settingsFormat.addSector(getCurrentReflectorSector());
        settingsFormat.addSector(getPluginBoardSector());
    }

    public String getMachineSettings() {
        return settingsFormat.toString();
    }

    public List<Pair<Integer, Integer>> getNotchPositionsInRotorsInUse() {
        return rotorsInUse.stream().map(rotor -> new Pair<>(rotor.id(), rotor.notch() + 1)).collect(Collectors.toList());
    }

    public int getAmountCurrentRotorsInUse() {
        return isMachineSettingsInitialized() ? rotorsInUse.size() : 0;
    }

    public int getAmountOfTotalRotors() {
        return rotors.size();
    }

    public int getAmountOfTotalReflectors(){
        return reflectors.size();
    }



    //region Get Sector Methods
    private PluginBoardSector getPluginBoardSector() throws Exception {
        if(pluginBoard.size() <= 0) {
            throw  new Exception("There is no plugged pairs in the plugin board");
        }

        return new PluginBoardSector(pluginBoard.getAllPluggedPairs()
                    .entrySet()
                    .stream()
                    .map(pluggedPair -> new Pair<>(pluggedPair.getKey(), pluggedPair.getValue()))
                    .collect(Collectors.toList()));
    }

    private ReflectorIdSector getCurrentReflectorSector() {
        return new ReflectorIdSector(new ArrayList<RomanNumber>(Arrays.asList(reflectorInUse.id())));
    }

    private RotorIDSector getCurrentIdRotorsInUseSector() {
        return new RotorIDSector(rotorsInUse
                .stream()
                .map(rotor -> rotor.id())
                .collect(Collectors.toList()));
    }

    private StartingRotorPositionSector getCurrentInitialRotorPositionRotorsInUseSector() {
        return new StartingRotorPositionSector(rotorsInUse
                .stream()
                .map(rotor -> rotor.getStartingRightCharToWindow())
                .collect(Collectors.toList()));
    }

    public boolean isMachineSettingsInitialized() {
        return settingsFormat.isSettingsInitialized();

    }
    //endregion

    public SettingsFormat getSettingsFormat() {
        return settingsFormat;
    }

    public int getMessagesCounter() {
        return messagesCounter;
    }
}