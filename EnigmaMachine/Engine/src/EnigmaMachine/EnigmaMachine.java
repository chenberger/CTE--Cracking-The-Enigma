package EnigmaMachine;

import EnigmaMachineException.PluginBoardSettingsException;
import EnigmaMachineException.ReflectorSettingsException;
import EnigmaMachineException.RotorsInUseSettingsException;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import Engine.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class EnigmaMachine{
    private final Map<Integer, Rotor> rotors;
    private final int numOfActiveRotors;
    private final List<Rotor> rotorsInUse;
    private Reflector reflectorInUse;
    private final Map<RomanNumber, Reflector> reflectors;
    private PluginBoard pluginBoard;
    private final Map<Character, Integer> keyboard;
    private boolean isTheInitialCodeDefined;
    private final SettingsFormat originalSettingsFormat;

    public EnigmaMachine(Map<Integer, Rotor> rotors, Map<RomanNumber, Reflector> reflectors, Map<Character, Integer> keyboard, int numOfActiveRotors){
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.keyboard = keyboard;
        this.numOfActiveRotors = numOfActiveRotors;
        this.rotorsInUse = new ArrayList<Rotor>();
        this.pluginBoard = new PluginBoard();
        this.originalSettingsFormat = new SettingsFormat();
        this.isTheInitialCodeDefined = false;
    }

    //region set settings
    public void setPluginBoardSettings(PluginBoardSector pluginBoardSector) {
        pluginBoard.clear();

        for (Pair<Character, Character> pluginPair : pluginBoardSector.getElements()) {
            pluginBoard.addPluginPair(pluginPair);
        }
    }
    public void validatePluginBoardSettings(PluginBoardSector pluginBoardSector) throws PluginBoardSettingsException {
        PluginBoardSettingsException pluginBoardSettingsException = new PluginBoardSettingsException();
        Map<Character, Boolean> charactersUsingForPluginBoard = new HashMap<>();
        initializeCharactersUsingForPluggedBoard(charactersUsingForPluginBoard);

        if(pluginBoardSector != null && pluginBoardSector.getElements().size() > 0) {
            if(pluginBoardSector.getElements().size() > getMaximumPairs()) {
                pluginBoardSettingsException.addIllegalPairsSize(pluginBoardSector.getElements().size(), getMaximumPairs());
            }

            checkValidPluggedPair(pluginBoardSector, pluginBoardSettingsException, charactersUsingForPluginBoard);

            if (pluginBoardSettingsException.isExceptionNeedToThrown()) {
                throw pluginBoardSettingsException;
            }
            else {
                originalSettingsFormat.addSector(pluginBoardSector);
                originalSettingsFormat.setIfPluginBoardSet(true);
            }
        }
        else {
            originalSettingsFormat.setIfPluginBoardSet(false);
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

    public void setReflectorInUseSettings(ReflectorIdSector reflectorIdSector) throws CloneNotSupportedException {
        if(reflectorInUse != null) {
            reflectorInUse.clear();
        }

        for(RomanNumber romanNumber : reflectors.keySet()) {
            if(romanNumber == reflectorIdSector.getElements().get(0)) {
                reflectorInUse = (Reflector) reflectors.get(romanNumber).clone();
                break;
            }
        }
    }

    public void validateReflectorInUseSettings(ReflectorIdSector reflectorIdSector) throws ReflectorSettingsException {
        ReflectorSettingsException reflectorSettingsException = new ReflectorSettingsException();
        boolean isReflectorFound = false;
        RomanNumber reflectorId;

        if(reflectorIdSector.getElements().size() != 1) {
            reflectorSettingsException.addIllegalReflectorsSize(reflectorIdSector.getElements().size());
         }

        reflectorId = reflectorIdSector.getElements().get(0);

        for(RomanNumber romanNumber : reflectors.keySet()) {
            if(romanNumber == reflectorId) {
                isReflectorFound = true;
            }
        }

        if(!isReflectorFound) {
            reflectorSettingsException.addIllegalReflectorId(reflectorId, reflectors.keySet());
        }

        if(reflectorSettingsException.isExceptionNeedToThrown()) {
            throw reflectorSettingsException;
        }
    }

    public void setStartingPositionRotorsSettings(StartingRotorPositionSector startPositionsOfTheRotors) {
        int index = 0;
        for (Character startingRightCharToWindow : startPositionsOfTheRotors.getElements()) {
                rotorsInUse.get(index).setStartingRightCharToWindow(startingRightCharToWindow);
                index++;
        }
    }

    public void validateStartingPositionRotorsSettings(StartingRotorPositionSector startPositionsOfTheRotors) throws StartingPositionsOfTheRotorException {
        StartingPositionsOfTheRotorException startingPositionsOfTheRotorException = new StartingPositionsOfTheRotorException();
        boolean isCharacterFound;

        if (startPositionsOfTheRotors.getElements().size() != numOfActiveRotors) {
            startingPositionsOfTheRotorException.addIllegalPositionsSize(startPositionsOfTheRotors.getElements().size(), numOfActiveRotors);
        }

        for (Character startingRightCharToWindow : startPositionsOfTheRotors.getElements()) {
            isCharacterFound = false;
            for (Character character : keyboard.keySet()) {
                if (character == startingRightCharToWindow) {
                    isCharacterFound = true;
                    break;
                }
            }

            if (!isCharacterFound) {
                startingPositionsOfTheRotorException.addIllegalCharacter(startingRightCharToWindow, keyboard.keySet());
            }
        }

        if (startingPositionsOfTheRotorException.isExceptionNeedToThrown()) {
            throw startingPositionsOfTheRotorException;
        }
    }

    public void setRotorsInUseSettings(RotorIDSector rotorIDSector) {
        rotorsInUse.clear();
        int index = 0;

        for(Integer rotorId : rotorIDSector.getElements()) {
            rotors.get(rotorId).setIsFirstRotor(index == 0);
            rotorsInUse.add(rotors.get(rotorId));
            index++;
        }
    }

    public void validateRotorsInUseSettings(RotorIDSector rotorIDSector) throws RotorsInUseSettingsException {
        RotorsInUseSettingsException rotorsInUseSettingsException = new RotorsInUseSettingsException();
        Map<Integer, Boolean> rotorsUsingForTheMachine = new HashMap<>();
        initializeRotorsUsingForTheMachine(rotorsUsingForTheMachine, rotorIDSector.getElements());

        if(rotorIDSector.getElements().size() != numOfActiveRotors) {
            rotorsInUseSettingsException.addIllegalAmountOfRotors(rotorIDSector.getElements().size(), numOfActiveRotors);
        }

        for(Integer rotorId : rotorIDSector.getElements()) {
            if(rotorsUsingForTheMachine.get(rotorId)) {
                rotorsInUseSettingsException.addRotorIdDuplicates(rotorId);
            }

            if(rotors.containsKey(rotorId)) {
                    rotorsUsingForTheMachine.put(rotorId, true);
            }
            else {
                rotorsInUseSettingsException.addIllegalRotorId(rotorId, rotors.keySet());
            }
        }

        if (rotorsInUseSettingsException.isExceptionNeedToThrown()) {
            throw rotorsInUseSettingsException;
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
        if(isTheInitialCodeDefined) {
            for(Rotor rotor : rotorsInUse) {
                rotor.reset();
            }
        }
    }
    //region Setters

    public void setPluginBoard(PluginBoard pluginBoard) {
        this.pluginBoard = pluginBoard;
    }
    public void validateReflectorInUseSettings(String id) {
        this.reflectorInUse = findReflectorById(id);
    }
    //endregion

    //region Getters
    public int getNumOfActiveRotors() {
        return numOfActiveRotors;
    }
    public Map<Integer, Rotor> getAllRotors() { return rotors; }

    public List<Rotor> getCurrentRotorsInUse() { return rotorsInUse; }

    public Map<RomanNumber, Reflector> getAllReflectors() { return reflectors; }

    public Reflector getCurrentReflectorInUse() { return reflectorInUse; }

    public PluginBoard getPluginBoard() { return pluginBoard; }

    public Set<Character> getKeyboard() { return keyboard.keySet(); }

    public SettingsFormat getOriginalSettingsFormat() { return originalSettingsFormat; }

    public SettingsFormat getCurrentSettingsFormat() throws CloneNotSupportedException {
        SettingsFormat currentSettingsFormat = (SettingsFormat) originalSettingsFormat.clone();
        if(isTheInitialCodeDefined) {
            RotorIDSector rotorIDSector = (RotorIDSector) (currentSettingsFormat.getSectorByType(SectorType.ROTORS_ID));
            rotorIDSector.setCurrentNotchPositions(rotorsInUse.stream().map(rotor -> rotor.notch()).collect(Collectors.toList()));
        }

        return currentSettingsFormat;
    }
    //endregion

    public void clearSettings() {
        originalSettingsFormat.clear();
    }

    public boolean isTheInitialCodeDefined() {
        return isTheInitialCodeDefined;
    }

    public void setTheInitialCodeDefined(boolean theInitialCodeDefined) {
        isTheInitialCodeDefined = theInitialCodeDefined;
    }

    public boolean isPluginBoardSet() {
        return originalSettingsFormat.isPluginBoardSet();
    }
}