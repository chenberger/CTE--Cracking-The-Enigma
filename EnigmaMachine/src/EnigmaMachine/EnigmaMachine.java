package EnigmaMachine;

import java.util.*;

public class EnigmaMachine{
    private List<Rotor> rotors;
    private List<Rotor> rotorsInUse;
    private Reflctor reflectorInUse;
    private List<Reflctor> reflectors;
    private PluginBoard pluginBoard;
    private Map<Character, Integer> machineCharacters;

    public EnigmaMachine(List<Rotor> i_Rotors, List<Reflctor> i_Reflectors, PluginBoard pluginBoard, Map<Character, Integer> machineCharacters) {
        rotors = i_Rotors;
        reflectors = i_Reflectors;
        this.pluginBoard = pluginBoard;
        this.machineCharacters = machineCharacters;
    }

    public int Decode(Character inputChar, Direction direction) {
        int currentCharIndex;
        Character currentChar;

        RotateRotors();

        currentChar = pluginBoard.getPlugedPair(inputChar);
        currentCharIndex = machineCharacters.get(currentChar);
        currentCharIndex = DecodeByDirection(currentCharIndex, Direction.FORWARD);
        currentCharIndex = reflectorInUse.SetIndex(currentCharIndex);
        currentCharIndex = DecodeByDirection(currentCharIndex, Direction.BACKWARD);
        currentChar = GetKeyByValue(machineCharacters, currentCharIndex);
        currentCharIndex = pluginBoard.getPlugedPair(currentChar);

        return currentCharIndex;
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
        for(Rotor rotor : rotorsInUse) {
            currentCharIndex = rotor.Decode(currentCharIndex, direction);
        }

        return currentCharIndex;
    }
}
