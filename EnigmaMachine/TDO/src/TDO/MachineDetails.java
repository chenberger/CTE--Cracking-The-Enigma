package TDO;

import EnigmaMachine.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class MachineDetails {

    private SettingsFormat settingsFormat;
    private final Map<Integer, Rotor> rotors;
    private final List<Rotor> rotorsInUse;
    private final Map<RomanNumber, Reflctor> reflectors;
    private final Reflctor reflectorInUse;
    private final Set<Character> keyboard;
    private final PluginBoard pluginBoard;

    public MachineDetails(Map<Integer, Rotor> rotors, List<Rotor> rotorsInUse, Map<RomanNumber, Reflctor> reflectors, Reflctor reflectorInUse, Set<Character> keyboard, PluginBoard pluginBoard) {
        this.rotors = rotors;
        this.rotorsInUse = rotorsInUse;
        this.reflectors = reflectors;
        this.reflectorInUse = reflectorInUse;
        this.keyboard = keyboard;
        this.pluginBoard = pluginBoard;
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
        return rotorsInUse.stream().map(rotor -> new Pair<>(rotor.id(), rotor.notch())).collect(Collectors.toList());
    }

    public int getAmountCurrentRotorsInUse() throws Exception {
        if(rotorsInUse == null || rotorsInUse.size() <= 0) {
            throw new Exception("There is no Rotor in the Machine");
        }

        return rotorsInUse.size();
    }

    public int getAmountOfTotalRotors() throws Exception {
        if(rotors == null || rotors.size() <= 0) {
            throw new Exception("There is no Rotor in the Machine");
        }

        return rotors.size();
    }

    public int getAmountOfTotalReflectors() throws Exception {
        if(reflectors == null || reflectors.size() <= 0) {
            throw new Exception("There is no Reflector in the Machine");
        }

        return reflectors.size();
    }



    //region Get Sector Methods
    private PluginBoardSector getPluginBoardSector() throws Exception {
        if(pluginBoard == null) {
            throw  new Exception("There is no Plugin Board in the Machine");
        }
        else if(pluginBoard.size() <= 0) {
            throw  new Exception("There is no plugged pairs in the plugin board");
        }

        return new PluginBoardSector(pluginBoard.getAllPluggedPairs()
                    .entrySet()
                    .stream()
                    .map(pluggedPair -> new Pair<>(pluggedPair.getKey(), pluggedPair.getValue()))
                    .collect(Collectors.toList()));
    }

    private ReflectorIdSector getCurrentReflectorSector() throws Exception {
        if(reflectorInUse == null) {
            throw new Exception("There is no Reflector in the Machine");
        }

        return new ReflectorIdSector(new ArrayList<RomanNumber>(Arrays.asList(reflectorInUse.id())));
    }

    private RotorIDSector getCurrentIdRotorsInUseSector() throws Exception {
        List<Rotor> reversedRotorsInUse = new ArrayList<Rotor>(rotorsInUse);
        Collections.reverse(reversedRotorsInUse);

        if (rotorsInUse == null || rotorsInUse.size() <= 0) {
            throw  new Exception("There is no Rotor in the Machine");
        }

        return new RotorIDSector(reversedRotorsInUse
                .stream()
                .map(rotor -> rotor.id())
                .collect(Collectors.toList()));
    }

    private StartingRotorPositionSector getCurrentInitialRotorPositionRotorsInUseSector() throws Exception {
        List<Rotor> reversedRotorsInUse = new ArrayList<Rotor>(rotorsInUse);
        Collections.reverse(reversedRotorsInUse);

        if (rotorsInUse == null || rotorsInUse.size() <= 0) {
            throw new Exception("There is no Rotor in the Machine");
        }

        return new StartingRotorPositionSector(reversedRotorsInUse
                .stream()
                .map(rotor -> rotor.getStartingRightCharToWindow())
                .collect(Collectors.toList()));
    }
    //endregion
}