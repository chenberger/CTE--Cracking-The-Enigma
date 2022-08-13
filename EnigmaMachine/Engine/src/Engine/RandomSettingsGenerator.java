package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.RomanNumber;
import EnigmaMachineException.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class RandomSettingsGenerator {
    private EnigmaMachine enigmaMachine;
    private RotorIDSector rotorIDSector;

    public RandomSettingsGenerator(EnigmaMachine enigmaMachine) {
        this.enigmaMachine = enigmaMachine;
    }

    //region set automatic settings
    public List<Sector> getRandomSectorSettings() throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, PluginBoardSettingsException, SettingsFormatException, CloneNotSupportedException, MachineNotExistsException, SettingsNotInitializedException {
        List<Sector> randomSectors = new ArrayList<>();

        for(SectorType sectorType : SectorType.values()) {
            randomSectors.add(sectorType.getRandomSector(this));
        }

        return randomSectors;
    }

    public PluginBoardSector getRandomPluginBoardSector() {
        List<Pair<Character, Character>> pluginPairs = new ArrayList<>();
        Set<Character> optionalCharacters = new HashSet<>(enigmaMachine.getKeyboard());
        Random randomGenerator = new Random();
        int amountOfPairs = randomGenerator.nextInt(enigmaMachine.getMaximumPairs() + 1);
        Pair<Character, Character> randomPair;

        for (int i = 0; i < amountOfPairs; i++) {
            randomPair = getRandomPluginPair(optionalCharacters.stream().collect(Collectors.toList()), pluginPairs);
            pluginPairs.add(randomPair);
            optionalCharacters.remove(randomPair.getKey());
            optionalCharacters.remove(randomPair.getValue());
        }

        return new PluginBoardSector(pluginPairs);
    }

    private Pair<Character, Character> getRandomPluginPair(List<Character> optionalCharacters, List<Pair<Character, Character>> pluginPairs) {
        Character leftCharacter = getRandomCharacterFromTheKeyboard(optionalCharacters);
        optionalCharacters.remove(leftCharacter);
        Character rightCharacter = getRandomCharacterFromTheKeyboard(optionalCharacters);
        Pair<Character, Character> randomPluginPair = new Pair<>(leftCharacter, rightCharacter);

        while(!isValidPair(randomPluginPair, pluginPairs))
        {
            optionalCharacters.add(leftCharacter);
            leftCharacter = getRandomCharacterFromTheKeyboard(optionalCharacters);
            optionalCharacters.remove(leftCharacter);
            rightCharacter = getRandomCharacterFromTheKeyboard(optionalCharacters);
            randomPluginPair = new Pair<>(leftCharacter, rightCharacter);
        }

        return randomPluginPair;
    }

    private boolean isValidPair(Pair<Character, Character> randomPair, List<Pair<Character, Character>> pluginPairs) {
        if(randomPair.getKey() == randomPair.getValue()) {
            return false;
        }

        //check if left char or right char already exists in any other plugged pair
        if(pluginPairs.stream().map(Pair::getKey).collect(Collectors.toSet()).contains(randomPair.getValue()) ||
                pluginPairs.stream().map(Pair::getKey).collect(Collectors.toSet()).contains(randomPair.getKey()) ||
                pluginPairs.stream().map(Pair::getValue).collect(Collectors.toSet()).contains(randomPair.getValue()) ||
                pluginPairs.stream().map(Pair::getValue).collect(Collectors.toSet()).contains(randomPair.getKey())) {
            return false;
        }

        return true;
    }

    public ReflectorIdSector getRandomReflectorSector() {
        Random randomGenerator = new Random();
        RomanNumber[] reflectorIdArr = enigmaMachine.getAllReflectors().keySet().toArray(new RomanNumber[enigmaMachine.getAllReflectors().keySet().size()]);
        List<RomanNumber> reflectorId = new ArrayList<RomanNumber>();

        reflectorId.add(reflectorIdArr[randomGenerator.nextInt(reflectorIdArr.length)]);

        return new ReflectorIdSector(reflectorId);
    }

    public StartingRotorPositionSector getRandomStartingPositionRotorsSector() {
        List<Character> startingPositionsOfTheRotors = new ArrayList<>();

        for (int i = 0; i < rotorIDSector.getElements().size(); i++) {
            startingPositionsOfTheRotors.add(getRandomCharacterFromTheKeyboard(enigmaMachine.getKeyboard().stream().collect(Collectors.toList())));
        }

        return new StartingRotorPositionSector(startingPositionsOfTheRotors);
    }

    private Character getRandomCharacterFromTheKeyboard(List<Character> optionalCharacters) {
        Random randomGenerator = new Random();

        return optionalCharacters.get(randomGenerator.nextInt(optionalCharacters.size()));
    }

    public RotorIDSector getRandomRotorsIdSector() {
        List<Integer> rotorsId = new ArrayList<>();
        Set<Integer> optionalRotorsId = new HashSet<>(enigmaMachine.getAllRotors().keySet());
        Integer randomId;

        for (int i = 0; i < enigmaMachine.getNumOfActiveRotors(); i++) {
            randomId =  getRandomRotorId(optionalRotorsId.stream().collect(Collectors.toList()));
            rotorsId.add(randomId);
            optionalRotorsId.remove(randomId);
        }
        rotorIDSector = new RotorIDSector(rotorsId);

        return rotorIDSector;
    }

    private Integer getRandomRotorId(List<Integer> optionalRotorsId) {
        Random randomGenerator = new Random();
        int randomRotorId = optionalRotorsId.get(randomGenerator.nextInt(optionalRotorsId.size()));

        while(!enigmaMachine.getAllRotors().containsKey(randomRotorId))
        {
            randomRotorId = optionalRotorsId.get(randomGenerator.nextInt(optionalRotorsId.size()));
        }

        return randomRotorId;
    }
}
