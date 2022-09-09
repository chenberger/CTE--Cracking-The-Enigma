package EnigmaMachine;

import EnigmaMachine.Settings.*;
import EnigmaMachineException.PluginBoardSettingsException;
import EnigmaMachineException.ReflectorSettingsException;
import EnigmaMachineException.RotorsInUseSettingsException;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class EnigmaMachine implements Serializable {
    private final Map<Integer, Rotor> rotors;
    private final int numOfActiveRotors;
    private final List<Rotor> rotorsInUse;
    private Reflector reflectorInUse;
    private final Map<RomanNumber, Reflector> reflectors;
    private PluginBoard pluginBoard;
    private final Keyboard keyboard;
    private boolean isTheInitialCodeDefined;
    private final SettingsFormat originalSettingsFormat;

    public EnigmaMachine(Map<Integer, Rotor> rotors, Map<RomanNumber, Reflector> reflectors, Map<Character, Integer> keyboard, int numOfActiveRotors){
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.keyboard = new Keyboard(keyboard);
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
        for (Pair<Character, Character> pluginPair : pluginBoardSector.getElements()) {
            if (pluginPair.getKey() == pluginPair.getValue()) {
                pluginBoardSettingsException.addValuePluggedToHimself(pluginPair.getKey());
            }

            if (charactersUsingForPluginBoard.containsKey(pluginPair.getKey()) && charactersUsingForPluginBoard.get(pluginPair.getKey())) {
                pluginBoardSettingsException.addValuePluggedToMoreThenOneChar(pluginPair.getKey());
            }

            if (charactersUsingForPluginBoard.containsKey(pluginPair.getValue()) && charactersUsingForPluginBoard.get(pluginPair.getValue())) {
                pluginBoardSettingsException.addValuePluggedToMoreThenOneChar(pluginPair.getValue());
            }

            if (!keyboard.containsKey(pluginPair.getKey())) {
                pluginBoardSettingsException.addIllegalCharNotFromTheKeyboard(pluginPair.getKey(), keyboard.keySet());
            }
            else {
                charactersUsingForPluginBoard.put(pluginPair.getKey(), true);
            }

            if (!keyboard.containsKey(pluginPair.getValue())) {
                pluginBoardSettingsException.addIllegalCharNotFromTheKeyboard(pluginPair.getValue(), keyboard.keySet());
            }
            else {
                charactersUsingForPluginBoard.put(pluginPair.getValue(), true);
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
        else {
            reflectorId = reflectorIdSector.getElements().get(0);

            for(RomanNumber romanNumber : reflectors.keySet()) {
                if(romanNumber == reflectorId) {
                    isReflectorFound = true;
                }
            }

            if(!isReflectorFound) {
                reflectorSettingsException.addIllegalReflectorId(reflectorId, reflectors.keySet());
            }
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

    private Character decode(Character inputtedChar) {
        int currentCharIndex;
        Character currentChar;

        rotateRotors();

        currentChar = pluginBoard.getPluggedPair(inputtedChar);
        currentCharIndex = keyboard.get(currentChar);
        currentCharIndex = decodeByDirection(currentCharIndex, Direction.FORWARD);
        currentCharIndex = reflectorInUse.SetIndex(currentCharIndex);
        currentCharIndex = decodeByDirection(currentCharIndex, Direction.BACKWARD);
        currentChar = keyboard.getKeyByValue(currentCharIndex);
        currentChar = pluginBoard.getPluggedPair(currentChar);

        return currentChar;
    }

    public String processedInput(String inputToProcess) throws IllegalArgumentException{
        if(containsCharNotInMAMachineKeyboard(inputToProcess)){
            List<Character> lettersNotInAbc = new ArrayList<>(getCharsNotInMachineKeyboard(inputToProcess));
            throw new IllegalArgumentException("Error: The input contains char/s that are not in the machine keyboard which are: " + lettersNotInAbc + System.lineSeparator()
                    + "You can choose only from the following letters: " + keyboard.keySet());
        }
        String processedInput = "";
        for(char letter: inputToProcess.toCharArray()){
            processedInput += decode(letter);
        }
        return processedInput;
    }

    public List<Character> getCharsNotInMachineKeyboard(String inputToProcess) {
        return inputToProcess.chars().mapToObj(inputtedChar -> (char)inputtedChar).filter(inputtedChar -> !keyboard.containsKey(inputtedChar)).collect(Collectors.toList());
    }

    public boolean containsCharNotInMAMachineKeyboard(String inputToProcess) {
        for(char letter: inputToProcess.toCharArray()){
            if(!keyboard.containsKey(letter)){
                return true;
            }
        }
        return false;
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

    //region Getters
    public int getNumOfActiveRotors() {
        return numOfActiveRotors;
    }
    public Map<Integer, Rotor> getAllRotors() { return rotors; }

    public List<Rotor> getCurrentRotorsInUse() { return rotorsInUse; }

    public Map<RomanNumber, Reflector> getAllReflectors() { return reflectors; }

    public Reflector getCurrentReflectorInUse() { return reflectorInUse; }

    public PluginBoard getPluginBoard() { return pluginBoard; }

    public Set<Character> getKeyboardCharacters() { return keyboard.keySet(); }
    public Keyboard getKeyboard() { return keyboard; }

    public SettingsFormat getOriginalSettingsFormat() { return originalSettingsFormat; }

    public SettingsFormat getCurrentSettingsFormat() throws CloneNotSupportedException {
        SettingsFormat currentSettingsFormat = (SettingsFormat) originalSettingsFormat.clone();
        if(isTheInitialCodeDefined) {
            StartingRotorPositionSector startingRotorPositionSector = (StartingRotorPositionSector) (currentSettingsFormat.getSectorByType(SectorType.START_POSITION_ROTORS));
            startingRotorPositionSector.setCurrentNotchPositions(rotorsInUse.stream().map(rotor -> rotor.notch()).collect(Collectors.toList()));
            startingRotorPositionSector.setCurrentCharactersInTheWindow(rotorsInUse.stream().map(rotor -> rotor.getCurrentRightCharInTheWindow()).collect(Collectors.toList()));
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


    public Map<Integer,Rotor> cloneRotors() {
        Map<Integer,Rotor> clonedRotors = new HashMap<>();
        for(Map.Entry<Integer,Rotor> entry : rotors.entrySet()) {
            clonedRotors.put(entry.getKey(), (Rotor)entry.getValue().cloneRotor());
        }
        return clonedRotors;
    }

    public Map<RomanNumber, Reflector> cloneReflector() {
        Map<RomanNumber, Reflector> clonedReflectors = new HashMap<>();
        for(Map.Entry<RomanNumber, Reflector> entry : reflectors.entrySet()) {
            clonedReflectors.put(entry.getKey(), (Reflector)entry.getValue().cloneReflector());
        }
        return clonedReflectors;
    }

    public Map<Character, Integer> cloneKeyboard() {
        Map<Character, Integer> clonedKeyboard = new HashMap<>();
        for(Map.Entry<Character, Integer> entry : keyboard.entrySet()) {
            clonedKeyboard.put(entry.getKey(), entry.getValue());
        }
        return clonedKeyboard;
    }

    public void setReflector(Reflector reflector) {
        reflectorInUse = reflector;
    }
}