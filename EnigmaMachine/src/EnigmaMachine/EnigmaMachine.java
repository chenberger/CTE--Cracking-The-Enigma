package EnigmaMachine;

import java.util.*;

public class EnigmaMachine{
    private List<Rotor> rotors;
    private List<Rotor> rotorsInUse;
    private Reflctor reflectorInUse;
    private List<Reflctor> reflectors;
    private PluginBoard pluginBoard;
    private Map<Character, Integer> machineCharacters;

    public EnigmaMachine(List<Rotor> i_Rotors, Reflctor i_Reflectors, PluginBoard PluginBoard, Map<Character, Integer> i_MachineCharacters) {
        rotorsInUse = i_Rotors;
        reflectorInUse = i_Reflectors;
        pluginBoard = PluginBoard;
        machineCharacters = i_MachineCharacters;
    }

    public Character Decode(Character inputChar) {
        int currentCharIndex;
        Character currentChar;

        RotateRotors();

        currentChar = pluginBoard.getPlugedPair(inputChar);
        currentCharIndex = machineCharacters.get(currentChar);
        currentCharIndex = DecodeByDirection(currentCharIndex, Direction.FORWARD);
        currentCharIndex = reflectorInUse.SetIndex(currentCharIndex);
        currentCharIndex = DecodeByDirection(currentCharIndex, Direction.BACKWARD);
        currentChar = GetKeyByValue(machineCharacters, currentCharIndex);
        currentChar = pluginBoard.getPlugedPair(currentChar);

        return currentChar;
    }

    private Character GetKeyByValue(Map<Character, Integer> machineCharacters, int valueToSearch) {
        for (Map.Entry<Character,Integer> entry : machineCharacters.entrySet())
            if(entry.getValue() == valueToSearch) {
                return entry.getKey();
        }

        return null;
    }

    private void RotateRotors() {
        boolean isPreviewsRotorNotchReachedTheWindow = false;
        for(Rotor rotor : rotorsInUse) {
            isPreviewsRotorNotchReachedTheWindow = rotor.Rotate(isPreviewsRotorNotchReachedTheWindow);
        }
    }

    private int DecodeByDirection(int currentCharIndex, Direction direction) {
        List<Rotor> rotorsOrder = new ArrayList<>(rotorsInUse);

        if(direction == Direction.BACKWARD) {
            Collections.reverse(rotorsOrder);
        }
        for (Rotor rotor : rotorsOrder) {
           currentCharIndex = rotor.Decode(currentCharIndex, direction);
        }

        return currentCharIndex;
    }
}
