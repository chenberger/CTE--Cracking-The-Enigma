package EnigmaMachineException;

import EnigmaMachine.Rotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GeneralEnigmaMachineException extends Exception {
    private List<Character> DuplicateChars;
    private int numberOfRotorsToAdd = 0;
    private boolean oddLength = false;

    private Map<Integer, Integer> RotorsWithSameId;

    private Map<Integer, List<Integer>> sameMappingInOneReflector;
    private Map<Character, List<Character>> sameMappingInOneRotor;

    private boolean reflectorNotFound = false;

    public boolean noExceptionRaised() {
        return !reflectorNotFound && !oddLength && DuplicateChars.isEmpty() && sameMappingInOneRotor.isEmpty() && sameMappingInOneReflector.isEmpty() && numberOfRotorsToAdd == 0;
    }
    public void addCharToDuplicateChars(Character inputedChar) {
        DuplicateChars.add(inputedChar);
    }
    public void addRotorToRotorsWithSameId(int inputedId) {
        RotorsWithSameId.put(inputedId, RotorsWithSameId.getOrDefault(inputedId, 1) + 1);
    }

    public void addValuesWithSameMappingInOneReflector(int inputedId, int firstValueMappedTo, int secondValueMappedTo) {
        sameMappingInOneReflector.putIfAbsent(inputedId, new ArrayList<>(Arrays.asList(firstValueMappedTo, secondValueMappedTo)));
        sameMappingInOneReflector.get(inputedId).add(firstValueMappedTo);
    }

    public void addValuesWithSameMappingInOneRotor(Character charMappedIntoMoreThenOneChar, Character FirstCharMappedInto, Character SecondCharMappedInto) {
        sameMappingInOneRotor.putIfAbsent(charMappedIntoMoreThenOneChar, new ArrayList<>(Arrays.asList(FirstCharMappedInto, SecondCharMappedInto)));

        sameMappingInOneRotor.get(charMappedIntoMoreThenOneChar).add(FirstCharMappedInto);
    }

    //region Setters
    public void setNumberOfRotorsToAdd(int numberOfRotorsToAdd) {
        this.numberOfRotorsToAdd = numberOfRotorsToAdd;
    }
    public void setReflectorNotFound() {
        this.reflectorNotFound = true;
    }
    public void setIsoddLength() {
        this.oddLength = true;
    }
    //end region
}