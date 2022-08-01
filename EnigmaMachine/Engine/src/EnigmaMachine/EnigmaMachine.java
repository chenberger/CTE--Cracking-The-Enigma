package EnigmaMachine;

import javafx.util.Pair;

import java.util.*;

public class EnigmaMachine{
    private Map<Integer, Rotor> rotors;
    private List<Rotor> rotorsInUse;
    private Reflctor reflectorInUse;
    private Map<RomanNumber, Reflctor> reflectors;
    private PluginBoard pluginBoard;

    private Map<Character, Integer> keyboard;

    public EnigmaMachine(Map<Integer, Rotor> rotors, Map<RomanNumber, Reflctor> reflectors, PluginBoard pluginBoard, Map<Character, Integer> keyboard) {
        this.rotors = rotors;
        this.reflectors = reflectors;
        this.pluginBoard = pluginBoard;
        this.keyboard = keyboard;
    }

    public void initializeSetting(SettingsFormat settingsFormat) throws Exception {
        for (Sector sector : settingsFormat.getSettingsFormat()) {
            if(sector.type == SectorType.ROTORS_ID) {
                initializeRotorsInUse((RotorIDSector) sector);
            }

            if(sector.type == SectorType.START_POSITION_ROTORS) {
                initializeStartingPositionRotors((InitialRotorPositionSector) sector, (RotorIDSector) settingsFormat.getSettingsFormat().get(0));
            }

            if(sector.type == SectorType.REFLECTOR) {
                initializeReflector((ReflectorIdSector) sector);
            }

            if(sector.type == SectorType.PLUGIN_BOARD) {
                initializePluginBoard((PluginBoardSector) sector);
            }
        }
    }

    private void initializePluginBoard(PluginBoardSector pluginBoardSector) throws Exception {
        Map<Character, Boolean> charactersUsingForPluginBoard = new HashMap<>();
        initializeCharactersUsingForPluggedBoard(charactersUsingForPluginBoard);

        if(pluginBoardSector.getElements().size() > keyboard.size() / 2) {
            throw new Exception("Error in initializePluginBoard: The amount of pairs was inserted is : "
                    + pluginBoardSector.getElements().size()
                    + "The max amount of pairs is : "
                    + keyboard.size() / 2);
        }

        for(Pair<Character, Character> pluginPair : pluginBoardSector.getElements()) {
            if(pluginPair.getKey() == pluginPair.getValue()) {
                throw new Exception("Error in initializePluginBoard: The character " + pluginPair.getKey() + "cannot plugged to himself");
            }

            if(keyboard.containsKey(pluginPair.getKey())) {
                if(keyboard.containsKey(pluginPair.getValue())) {
                    if(!charactersUsingForPluginBoard.get(pluginPair.getKey())) {
                        if(!charactersUsingForPluginBoard.get(pluginPair.getValue())) {
                            pluginBoard.addPlugginPair(pluginPair);
                        }
                        else {
                            throw new Exception("Error in initializePluginBoard: The character " + pluginPair.getValue() + "cannot plugged to more then one character");
                        }
                    }
                    else {
                        throw new Exception("Error in initializePluginBoard: The character " + pluginPair.getKey() + "cannot plugged to more then one character");
                    }
                }
                else {
                    throw new Exception("Error in initializePluginBoard: The character " + pluginPair.getKey() + "is illegal");
                }
            }
            else {
                throw new Exception("Error in initializePluginBoard: The character " + pluginPair.getValue() + "is illegal");
            }
        }

    }

    private void initializeCharactersUsingForPluggedBoard(Map<Character, Boolean> charactersUsingForPluginBoard) {
        for(Character character : keyboard.keySet()) {
            charactersUsingForPluginBoard.put(character , false);
        }
    }

    private void initializeReflector(ReflectorIdSector reflectorIdSector) throws Exception {
        boolean isReflectorFound = false;
        RomanNumber reflectorId;

        if(reflectorIdSector.getElements().size() != 1) {
            throw new Exception("There amount of reflectors that was inserted in wrong! the amount is needed 1 and the amount was inserted is : " + reflectorIdSector.getElements().size());
        }

        reflectorId = reflectorIdSector.getElements().get(0);

        for(RomanNumber romanNumber : RomanNumber.values()) {
            if(romanNumber == reflectorId) {
                reflectorInUse = reflectors.get(romanNumber);
                isReflectorFound = true;
            }
        }

        if(!isReflectorFound) {
            throw new Exception("There is no any reflector found with the id : " + reflectorId);
        }
    }

    private void initializeStartingPositionRotors(InitialRotorPositionSector startPositionsOfTheRotors, RotorIDSector rotorIDSector) throws Exception {
        List<Character> reversedStartPositionsOfTheRotors = new ArrayList<Character>(startPositionsOfTheRotors.getElements());
        Collections.reverse(reversedStartPositionsOfTheRotors);
        int index = 0;
        boolean isCharacterFound;
        rotorsInUse.clear();

        for(Character startingRightCharToWindow : reversedStartPositionsOfTheRotors) {
            isCharacterFound = false;
            for(Character character : keyboard.keySet()) {
                if(character == startingRightCharToWindow) {
                    rotors.get(rotorIDSector.getElements().get(index)).setStartingRightCharToWindow(startingRightCharToWindow);
                    isCharacterFound = true;
                }
            }

            if(!isCharacterFound) {
                throw new Exception("There character " + startingRightCharToWindow + "is not matching to any char from the keyboard characters");
            }
            index++;
        }
    }

    private void initializeRotorsInUse(RotorIDSector rotorIDSector) throws Exception {
        List<Integer> reversedRotorsId = new ArrayList<Integer>(rotorIDSector.getElements());
        Collections.reverse(reversedRotorsId);
        rotorsInUse.clear();

        for(Integer rotorId : reversedRotorsId) {
            if(rotors.containsKey(rotorId)) {
                    rotorsInUse.add(rotors.get(rotorId));
            }
            else {
                throw new Exception("There is no any rotor found with the id: " + rotorId);
            }
        }
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


    //region Getters
    public Map<Integer, Rotor> getAllrotors() {
        return rotors;
    }

    public List<Rotor> getCurrentRotorsInUse() {
        return rotorsInUse;
    }

    public Map<RomanNumber, Reflctor> getAllReflectors() {
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
