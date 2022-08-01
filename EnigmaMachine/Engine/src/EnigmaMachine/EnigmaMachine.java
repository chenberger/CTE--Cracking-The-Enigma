package EnigmaMachine;

import Operations.RomanNumber;

import java.util.*;

public class EnigmaMachine{
    private List<Rotor> rotors;
    private List<Rotor> rotorsInUse;
    private Map<Integer,Rotor> allRotors;
    private Reflctor reflectorInUse;

    private Map<RomanNumber,Reflctor> allReflectors;
    private List<Reflctor> reflectors;
    private PluginBoard pluginBoard;
    private final Map<Character, Integer> keyboard;

    public EnigmaMachine(List<Rotor> rotors, Reflctor reflectors, PluginBoard pluginBoard, Map<Character, Integer> keyboard) {
        this.rotorsInUse = rotors;
        this.reflectorInUse = reflectors;
        this.pluginBoard = pluginBoard;
        this.keyboard = keyboard;
    }
    public EnigmaMachine(Map<Integer,Rotor> rotors, Map<RomanNumber,Reflctor> reflectors, Map<Character, Integer> keyboard) {
        this.allRotors = rotors;
        this.allReflectors = reflectors;
        this.keyboard = keyboard;
    }
    public Character decode(Character inputtedChar) {
        int currentCharIndex;
        Character currentChar;

        rotateRotors();

        currentChar = pluginBoard.getPlugedPair(inputtedChar);
        currentCharIndex = keyboard.get(currentChar);
        currentCharIndex = decodeByDirection(currentCharIndex, Direction.FORWARD);
        currentCharIndex = reflectorInUse.SetIndex(currentCharIndex);
        currentCharIndex = decodeByDirection(currentCharIndex, Direction.BACKWARD);
        currentChar = getKeyByValue(keyboard, currentCharIndex);

        currentChar = pluginBoard.getPlugedPair(currentChar);

        return currentChar;
    }


    private Character getKeyByValue(Map<Character, Integer> machineCharacters, int valueToSearch) {

        for (Map.Entry<Character,Integer> entry : machineCharacters.entrySet())
            if(entry.getValue() == valueToSearch) {
                return entry.getKey();
        }

        return null;
    }


    private void rotateRotors() {
        boolean isPreviewsRotorNotchReachedTheWindow = false;
        for(Rotor rotor : rotorsInUse) {
            isPreviewsRotorNotchReachedTheWindow = rotor.rotate(isPreviewsRotorNotchReachedTheWindow);
        }
    }

    private int decodeByDirection(int currentCharIndex, Direction direction) {

        List<Rotor> rotorsOrder = new ArrayList<>(rotorsInUse);

        if(direction == Direction.BACKWARD) {
            Collections.reverse(rotorsOrder);
        }
        for (Rotor rotor : rotorsOrder) {
           currentCharIndex = rotor.decode(currentCharIndex, direction);
        }

        return currentCharIndex;
    }
    private Reflctor findReflectorById(String reflectorInUseId) {
        for(Reflctor reflector : allReflectors) {
            if(reflector.id().equals(reflectorInUseId)) {
                return reflector;
            }
        }
        return null;
    }
    //region Setters

    public void setPluginBoard(PluginBoard pluginBoard) {
        this.pluginBoard = pluginBoard;
    }
    public void setRotorsInUse(List<Rotor> rotorsInUse) {
        this.rotorsInUse = rotorsInUse;
    }
    public void setReflectorInUse(String id) {
        this.reflectorInUse = findReflectorById(id);
    }

    //endregion
    //region Getters
    public List<Rotor> getAllrotors() {
        return rotors;
    }

    public List<Rotor> getCurrentRotorsInUse() {
        return rotorsInUse;
    }

    public List<Reflctor> getAllReflectors() {
        return reflectors;
    }

    public Reflctor getCurrentReflectorInUse() {
        return reflectorInUse;
    }

    public PluginBoard getPluginBoard() {
        return pluginBoard;
    }

    public Set<Character> getKeyboard() {
        return keyboard.keySet();
    }
    //endregion
}
