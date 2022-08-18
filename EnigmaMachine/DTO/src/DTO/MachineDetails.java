package DTO;

import EnigmaMachine.*;
import Engine.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class MachineDetails {

    private SettingsFormat originalSettingsFormat;
    private SettingsFormat currentSettingsFormat;
    private final Map<Integer, Rotor> rotors;
    private final List<Rotor> rotorsInUse;
    private final Map<RomanNumber, Reflector> reflectors;
    private final Reflector reflectorInUse;
    private final Set<Character> keyboard;
    private final PluginBoard pluginBoard;
    private int messagesCounter;

    private EnigmaMachine enigmaMachine;

    public MachineDetails(EnigmaMachine enigmaMachine, int messagesCounter, SettingsFormat originalSettingsFormat, SettingsFormat currentSettingsFormat) {
        this.enigmaMachine = enigmaMachine;
        this.rotors = enigmaMachine.getAllRotors();
        this.rotorsInUse = enigmaMachine.getCurrentRotorsInUse();
        this.reflectors = enigmaMachine.getAllReflectors();
        this.reflectorInUse = enigmaMachine.getCurrentReflectorInUse();
        this.keyboard = enigmaMachine.getKeyboard();
        this.pluginBoard = enigmaMachine.getPluginBoard();
        this.originalSettingsFormat = originalSettingsFormat;
        this.currentSettingsFormat = currentSettingsFormat;
        this.messagesCounter = messagesCounter;
    }

    public void initializeSettingFormat() throws Exception {
        originalSettingsFormat = new SettingsFormat();
        originalSettingsFormat.addSector(getCurrentIdRotorsInUseSector());
        originalSettingsFormat.addSector(getCurrentInitialRotorPositionRotorsInUseSector());
        originalSettingsFormat.addSector(getCurrentReflectorSector());
        originalSettingsFormat.addSector(getPluginBoardSector());
    }

    public String getOriginalMachineSettings() { return originalSettingsFormat.toString(); }
    public String getCurrentMachineSettings() { return currentSettingsFormat.toString(); }

    public int getAmountCurrentRotorsInUse() {
        return enigmaMachine.getNumOfActiveRotors();
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
        return originalSettingsFormat.isSettingsInitialized();

    }
    //endregion

    public SettingsFormat getOriginalSettingsFormat() {
        return originalSettingsFormat;
    }

    public int getMessagesCounter() {
        return messagesCounter;
    }
}