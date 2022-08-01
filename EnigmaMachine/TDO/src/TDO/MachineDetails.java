package TDO;

import EnigmaMachine.*;
import javafx.util.Pair;

import java.util.*;

public class MachineDetails {

    private static final char SEPARATOR = ',';
    private static final char OPEN_SECTOR = '<';
    private static final char CLOSE_SECTOR = '>';

    private final List<Rotor> rotors;
    private final List<Rotor> rotorsInUse;
    private final List<Reflctor> reflectors;
    private final Reflctor reflectorInUse;
    private final Set<Character> keyboard;
    private final PluginBoard pluginBoard;

    public MachineDetails(List<Rotor> rotors, List<Rotor> rotorsInUse, List<Reflctor> reflectors, Reflctor reflectorInUse, Set<Character> keyboard, PluginBoard pluginBoard) {
        this.rotors = rotors;
        this.rotorsInUse = rotorsInUse;
        this.reflectors = reflectors;
        this.reflectorInUse = reflectorInUse;
        this.keyboard = keyboard;
        this.pluginBoard = pluginBoard;
    }
    public List<Pair<Integer, Integer>> getNotchPositionsInRotorsInUse() {
        List<Pair<Integer, Integer>> notchPositions = new ArrayList<>();

        for(Rotor rotor : rotorsInUse) {
            notchPositions.add(new Pair<>(rotor.id(), rotor.notch()));
        }

        return notchPositions;
    }

    public String machineSettings() throws Exception {
        return getCurrentRotorsInUseFormat() +
                   getCurrentReflectorFormat() +
                   getPluginBoardInUseFormat();
    }

    private String getPluginBoardInUseFormat() throws Exception {
        StringBuilder pluggedPairsFormat = new StringBuilder();

        if(pluginBoard == null) {
            throw  new Exception("There is no Plugin Board in the Machine");
        }
        else if(pluginBoard.size() <= 0) {
            throw  new Exception("There is no plugged pairs in the plugin board");
        }
        else {
            pluggedPairsFormat.append(OPEN_SECTOR);
            for(Map.Entry<Character, Character> pluggedPair : pluginBoard.getAllPluggedPairs().entrySet()) {
                pluggedPairsFormat.append(pluggedPair.getKey() + PluginBoard.PAIR_SEPARATOR + pluggedPair.getValue());
            }
            pluggedPairsFormat.append(CLOSE_SECTOR);

            return pluggedPairsFormat.toString();
        }
    }

    private String getCurrentReflectorFormat() throws Exception {
        if(reflectorInUse != null) {
            return reflectorInUse.id();
        }
        else {
            throw  new Exception("There is no Reflector in the Machine");
        }
    }

    private String getCurrentRotorsInUseFormat() throws Exception {
        StringBuilder currentRotorsInUseFormat = new StringBuilder();
        StringBuilder startingPositionForCurrentRotatorsFormat = new StringBuilder();

        if (rotorsInUse == null || rotorsInUse.size() <= 0) {
            throw  new Exception("There is no Reflector in the Machine");
        }
        else {
            currentRotorsInUseFormat.append(OPEN_SECTOR);
            for(int i = rotorsInUse.size() -1; i >=0; i--) {
                currentRotorsInUseFormat.append(rotorsInUse.get(i).id());
                startingPositionForCurrentRotatorsFormat.append(rotorsInUse.get(i).startingRightCharToWindow());

                if(i > 0) {
                    currentRotorsInUseFormat.append(SEPARATOR);
                    startingPositionForCurrentRotatorsFormat.append(SEPARATOR);
                }
            }
            currentRotorsInUseFormat.append(CLOSE_SECTOR);
            startingPositionForCurrentRotatorsFormat.append(CLOSE_SECTOR);

            return currentRotorsInUseFormat.append(startingPositionForCurrentRotatorsFormat.toString()).toString();
        }
    }

    public int getCountOfRotorsInUse() throws Exception {
        return getCountOfComponent(rotorsInUse);
    }

    public int getCountOfTotalRotors() throws Exception {
        return getCountOfComponent(rotors);
    }

    public int getCountOfKeyboardCharacters() throws Exception {
        return keyboard.size();
    }

    public int getCountOfTotalReflectors() throws Exception {
        return getCountOfComponent(reflectors);
    }

    private <T> int getCountOfComponent(List<T> components) throws Exception {
        if(rotorsInUse == null) {
            return 0;
        }
        else {
            return rotorsInUse.size();
        }
    }
}