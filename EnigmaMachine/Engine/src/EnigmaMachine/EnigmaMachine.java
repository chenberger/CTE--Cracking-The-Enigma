package EnigmaMachine;

import EnigmaMachineException.PluginBoardSettingsException;
import EnigmaMachineException.ReflectorSettingsException;
import EnigmaMachineException.RotorsInUseSettingsException;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import Engine.*;
import javafx.util.Pair;

import java.util.*;

public class EnigmaMachine{
    private Map<Integer, Rotor> rotors;
    private int numOfActiveRotors;
    private List<Rotor> rotorsInUse;
    private Reflector reflectorInUse;
    private Map<RomanNumber, Reflector> reflectors;
    private PluginBoard pluginBoard;
    private final Map<Character, Integer> keyboard;

    private SettingsFormat settingsFormat;
    private boolean isMachineSettingInitialized;

    public EnigmaMachine(Map<Integer, Rotor> rotors, Map<RomanNumber, Reflector> reflectors, Map<Character, Integer> keyboard, int numOfActiveRotors){
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.keyboard = keyboard;
        this.numOfActiveRotors = numOfActiveRotors;
        this.rotorsInUse = new ArrayList<Rotor>();
        this.pluginBoard = new PluginBoard();
        this.settingsFormat = new SettingsFormat();
    }

    //region set settings
    public void setPluginBoardSettings(PluginBoardSector pluginBoardSector) throws PluginBoardSettingsException {
        PluginBoardSettingsException pluginBoardSettingsException = new PluginBoardSettingsException();
        Map<Character, Boolean> charactersUsingForPluginBoard = new HashMap<>();
        initializeCharactersUsingForPluggedBoard(charactersUsingForPluginBoard);
        pluginBoard.clear();

        if(pluginBoardSector != null && pluginBoardSector.getElements().size() > 0) {
            if(pluginBoardSector.getElements().size() > getMaximumPairs()) {
                pluginBoardSettingsException.addIllegalPairsSize(pluginBoardSector.getElements().size(), getMaximumPairs());
            }

            checkValidPluggedPair(pluginBoardSector, pluginBoardSettingsException, charactersUsingForPluginBoard);

            if (pluginBoardSettingsException.isExceptionNeedToThrown()) {
                pluginBoard.clear();
                throw pluginBoardSettingsException;
            }
            else {
                settingsFormat.addSector(pluginBoardSector);
                settingsFormat.isPluginBoardSet(true);
            }
        }
        else {
            settingsFormat.isPluginBoardSet(false);
        }
    }

    private void checkValidPluggedPair(PluginBoardSector pluginBoardSector, PluginBoardSettingsException pluginBoardSettingsException, Map<Character, Boolean> charactersUsingForPluginBoard) {
        boolean isValidKey;

        for (Pair<Character, Character> pluginPair : pluginBoardSector.getElements()) {
            isValidKey = true;
            if (pluginPair.getKey() == pluginPair.getValue()) {
                isValidKey = false;
                pluginBoardSettingsException.addValuePluggedToHimself(pluginPair.getKey());
            }

            if (!keyboard.containsKey(pluginPair.getKey())) {
                isValidKey = false;
                pluginBoardSettingsException.addIllegalCharNotFromTheKeyboard(pluginPair.getKey(), keyboard.keySet());
            }

            if (!keyboard.containsKey(pluginPair.getValue())) {
                isValidKey = false;
                pluginBoardSettingsException.addIllegalCharNotFromTheKeyboard(pluginPair.getValue(), keyboard.keySet());
            }

            if (charactersUsingForPluginBoard.containsKey(pluginPair.getKey()) && charactersUsingForPluginBoard.get(pluginPair.getKey())) {
                isValidKey = false;
                pluginBoardSettingsException.addValuePluggedToMoreThenOneChar(pluginPair.getKey());
            }

            if (charactersUsingForPluginBoard.containsKey(pluginPair.getValue()) && charactersUsingForPluginBoard.get(pluginPair.getValue())) {
                isValidKey = false;
                pluginBoardSettingsException.addValuePluggedToMoreThenOneChar(pluginPair.getValue());
            }

            if (isValidKey) {
                pluginBoard.addPluginPair(pluginPair);
                charactersUsingForPluginBoard.put(pluginPair.getValue(), true);
                charactersUsingForPluginBoard.put(pluginPair.getKey(), true);
            }
        }
    }

    private void initializeCharactersUsingForPluggedBoard(Map<Character, Boolean> charactersUsingForPluginBoard) {
        for(Character character : keyboard.keySet()) {
            charactersUsingForPluginBoard.put(character , false);
        }
    }

    public void setReflectorInUseSettings(ReflectorIdSector reflectorIdSector) throws ReflectorSettingsException, CloneNotSupportedException {
        ReflectorSettingsException reflectorSettingsException = new ReflectorSettingsException();
        boolean isReflectorFound = false;
        RomanNumber reflectorId;

        if(reflectorInUse != null) {
            reflectorInUse.clear();
        }

        if(reflectorIdSector.getElements().size() != 1) {
            reflectorSettingsException.addIllegalReflectorsSize(reflectorIdSector.getElements().size());
         }

        reflectorId = (RomanNumber) reflectorIdSector.getElements().get(0);

        for(RomanNumber romanNumber : reflectors.keySet()) {
            if(romanNumber == reflectorId) {
                reflectorInUse = (Reflector) reflectors.get(romanNumber).clone();
                isReflectorFound = true;
            }
        }

        if(!isReflectorFound) {
            reflectorSettingsException.addIllegalReflectorId(reflectorId, reflectors.keySet());
        }

        if(reflectorSettingsException.isExceptionNeedToThrown()) {
            if(reflectorInUse != null) {
                reflectorInUse.clear();
            }
            throw reflectorSettingsException;
        }
        else {
            settingsFormat.addSector(reflectorIdSector);
        }
    }

    public void setStartingPositionRotorsSettings(StartingRotorPositionSector startPositionsOfTheRotors, RotorIDSector rotorIDSector) throws StartingPositionsOfTheRotorException {
        StartingPositionsOfTheRotorException startingPositionsOfTheRotorException = new StartingPositionsOfTheRotorException();
        int index = 0;
        boolean isCharacterFound;

        if (startPositionsOfTheRotors.getElements().size() != rotorIDSector.getElements().size()) {
            startingPositionsOfTheRotorException.addIllegalPositionsSize(startPositionsOfTheRotors.getElements().size(), rotorIDSector.getElements().size());
        }

        for (Character startingRightCharToWindow : startPositionsOfTheRotors.getElements()) {
            isCharacterFound = false;
            for (Character character : keyboard.keySet()) {
                if (character == startingRightCharToWindow) {
                    if(index < rotorIDSector.getElements().size()) {
                        rotors.get(rotorIDSector.getElements().get(index)).setStartingRightCharToWindow(startingRightCharToWindow);
                    }

                    isCharacterFound = true;
                    break;
                }
            }

            if (!isCharacterFound) {
                startingPositionsOfTheRotorException.addIllegalCharacter(startingRightCharToWindow, keyboard.keySet());
            }
            index++;
        }

        if (startingPositionsOfTheRotorException.isExceptionNeedToThrown()) {
            throw startingPositionsOfTheRotorException;
        }
        else {
            settingsFormat.addSector(startPositionsOfTheRotors);
        }
    }

    public void initializeRotorsInUseSettings(RotorIDSector rotorIDSector) throws RotorsInUseSettingsException {
        //TODO Erez: check if the size of the count rotors equal to rotorIDSector size
        RotorsInUseSettingsException rotorsInUseSettingsException = new RotorsInUseSettingsException();
        Map<Integer, Boolean> rotorsUsingForTheMachine = new HashMap<>();
        initializeRotorsUsingForTheMachine(rotorsUsingForTheMachine, rotorIDSector.getElements());
        rotorsInUse.clear();

        if(rotorIDSector.getElements().size() > rotors.size()) {
            rotorsInUseSettingsException.addIllegalAmountOfRotors(rotorIDSector.getElements().size(), rotors.size());
        }

        for(Integer rotorId : rotorIDSector.getElements()) {
            if(rotorsUsingForTheMachine.get(rotorId)) {
                rotorsInUseSettingsException.addRotorIdDuplicates(rotorId);
            }

            if(rotors.containsKey(rotorId)) {
                    rotorsInUse.add(rotors.get(rotorId));
                    rotorsUsingForTheMachine.put(rotorId, true);
            }
            else {
                rotorsInUseSettingsException.addIllegalRotorId(rotorId, rotors.keySet());
            }
        }

        if (rotorsInUseSettingsException.isExceptionNeedToThrown()) {
            rotorsInUse.clear();
            throw rotorsInUseSettingsException;
        }
        else {
            settingsFormat.addSector(rotorIDSector);
        }
    }

    private void initializeRotorsUsingForTheMachine(Map<Integer, Boolean> rotorsUsingForTheMachine, List<Integer> rotors) {
        for(Integer rotorId : rotors) {
            rotorsUsingForTheMachine.put(rotorId, false);
        }
    }

    //endregion

    public int getMaximumPairs() {
        return keyboard.size() / 2;
    }

    public Character decode(Character inputtedChar) {
        int currentCharIndex;
        Character currentChar;

        rotateRotors();

        currentChar = pluginBoard.getPluggedPair(inputtedChar);
        currentCharIndex = keyboard.get(currentChar);
        currentCharIndex = decodeByDirection(currentCharIndex, Direction.FORWARD);
        currentCharIndex = reflectorInUse.SetIndex(currentCharIndex);
        currentCharIndex = decodeByDirection(currentCharIndex, Direction.BACKWARD);
        currentChar = getKeyByValue(keyboard, currentCharIndex);

        currentChar = pluginBoard.getPluggedPair(currentChar);

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
    private Reflector findReflectorById(String reflectorInUseId) {
        for(Reflector reflector : reflectors.values()) {
            if(reflector.id().equals(reflectorInUseId)) {
                return reflector;
            }
        }
        return null;
    }

    public void resetSettings() {
        if(settingsFormat.isSettingsInitialized()) {
            for(Rotor rotor : rotorsInUse) {
                rotor.reset();
            }
        }
    }
    //region Setters

    public void setPluginBoard(PluginBoard pluginBoard) {
        this.pluginBoard = pluginBoard;
    }
    public void setReflectorInUseSettings(String id) {
        this.reflectorInUse = findReflectorById(id);
    }
    //endregion

    //region Getters
    public Map<Integer, Rotor> getAllRotors() { return rotors; }

    public List<Rotor> getCurrentRotorsInUse() { return rotorsInUse; }

    public Map<RomanNumber, Reflector> getAllReflectors() { return reflectors; }

    public Reflector getCurrentReflectorInUse() { return reflectorInUse; }

    public PluginBoard getPluginBoard() { return pluginBoard; }

    public Set<Character> getKeyboard() { return keyboard.keySet(); }

    public SettingsFormat getSettingsFormat() { return settingsFormat; }
    //endregion

    public boolean isMachineSettingInitialized() {
        return settingsFormat.isSettingsInitialized();
    }

    public void clearSettings() {
        settingsFormat.clear();
    }
}