package EnigmaMachineException;

import Jaxb.Schema.Generated.CTERotor;

import java.util.*;
import java.util.stream.Collectors;

public class RotorNotValidException extends Exception {
    private int maxAlphabetLength;
    private final String EXCEPTION_IDENTATION = "       ";
    private final String INDEX_IDENTATION = "   ";
    private Integer errorIndex;
    private final String startingMessage = "Rotors Sector:" + System.lineSeparator();
    private List<Exception> exceptions;
    private Map<Integer, Integer> RotorsWithSameId;
    private final Map<Integer,List<String>>RotorsWithnotValidLetters;
    private final Map<Integer,Integer> rotorsWithNotEnoghPositions;
    private final Map<Integer, Integer> rotorsWithNotchOutOfRange;
    private final Map<Integer,List<Character>> leftColDuplicateChars;
    private final Map<Integer,List<Character>> RightColDuplicateChars;
    private int numberOfRotorsToAdd;
    List<Integer> MissingRotorsIdsInSequenceList;

    private boolean oddLength = false;
    private Map<Character, List<Character>> sameMappingInOneRotor;
    public void clearPreviousExceptions() {
        if(exceptions.size() > 0){
            exceptions.clear();
        }
    }

    public RotorNotValidException(){
        exceptions = new ArrayList<>();
        RotorsWithSameId = new HashMap<>();
        RotorsWithnotValidLetters = new HashMap<>();
        rotorsWithNotEnoghPositions = new HashMap<>();
        rotorsWithNotchOutOfRange = new HashMap<>();
        leftColDuplicateChars = new HashMap<>();
        RightColDuplicateChars = new HashMap<>();
        sameMappingInOneRotor = new HashMap<>();
        numberOfRotorsToAdd = 0;
        errorIndex = 1;
        oddLength = false;
        MissingRotorsIdsInSequenceList = new ArrayList<>();

    }

    public void addExceptionsToTheList(){
        addRotorsDuplicateException();
        addRotorsWithNotEnoughPositionsException();
        addRotorsWithNotchOutOfRangeException();
        addRotorsWithNotValidLettersException();
        addDuplicateleftColException();
        addDuplicateRightColException();
        addMissingRotorsIdsException();
    }
    public boolean shouldThrowException() {
        return exceptions.size() > 0;
    }
    @Override
    public String getMessage() {
        return System.lineSeparator() + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }

    public void addValuesWithSameMappingInOneRotor(Character charMappedIntoMoreThenOneChar, Character FirstCharMappedInto, Character SecondCharMappedInto) {
        sameMappingInOneRotor.putIfAbsent(charMappedIntoMoreThenOneChar, new ArrayList<>(Arrays.asList(FirstCharMappedInto, SecondCharMappedInto)));

        sameMappingInOneRotor.get(charMappedIntoMoreThenOneChar).add(FirstCharMappedInto);
    }
    public void setNumberOfRotorsToAdd(int numberOfRotorsToAdd) {
        this.numberOfRotorsToAdd = numberOfRotorsToAdd;
    }
    public void addNotValidLetter(String letter, int rotorId) {
        if(!RotorsWithnotValidLetters.containsKey(rotorId)){
            RotorsWithnotValidLetters.put(rotorId, new ArrayList<>());
        }

        RotorsWithnotValidLetters.get(rotorId).add(letter);

    }
    public void addRotorToRotorsWithSameId(int inputtedId) {
        RotorsWithSameId.put(inputtedId, RotorsWithSameId.getOrDefault(inputtedId, 1) + 1);
    }

    public void setNumberOfPairsInRotorInvalid(CTERotor rotor, List<Character> cteABC) {
        // TODO Implement.
        if(rotor.getCTEPositioning().size() != cteABC.size()) {
            rotorsWithNotEnoghPositions.put(rotor.getId(), rotor.getCTEPositioning().size());
        }
        maxAlphabetLength = cteABC.size();
    }

    //TODO check that the rotor id is positive number. V
    //TODO check that the id's are valid. V
    //TODO check that the each column is all the alphabet. V
    //TODO rows are the same length of abc. V

    public void addNotchOutOfRange(int id, int notch) {
        rotorsWithNotchOutOfRange.put(id, notch);
    }
    //addException region
    private void addRotorsDuplicateException(){

        if(!RotorsWithSameId.isEmpty()){
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
            + ": The following rotors appear more then once: " + RotorsWithSameId + System.lineSeparator()));
            errorIndex++;
        }
    }
    private void addRotorsWithNotValidLettersException(){
        if(!RotorsWithnotValidLetters.isEmpty()) {
            for (Map.Entry<Integer, List<String>> entry : RotorsWithnotValidLetters.entrySet()) {
                exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                + ": The Rotor " + entry.getKey() + " contains the following invalid letters: "
                        + System.lineSeparator()
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION + entry.getValue() + System.lineSeparator()));
            }
            errorIndex++;
        }
    }
    private void addRotorsWithNotEnoughPositionsException() {
        for(Map.Entry<Integer, Integer> entry : rotorsWithNotEnoghPositions.entrySet()) {
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The Rotor " + entry.getKey() + " has " + entry.getValue()
                    + " positions, should be " + maxAlphabetLength/2 + "."));
            errorIndex++;
        }
    }

    private void addRotorsWithNotchOutOfRangeException(){
        if(!rotorsWithNotchOutOfRange.isEmpty()){
        for(Map.Entry<Integer, Integer> entry : rotorsWithNotchOutOfRange.entrySet()) {
           exceptions.add(new Exception( EXCEPTION_IDENTATION + errorIndex.toString() +
                   INDEX_IDENTATION + ": The Rotor " + entry.getKey() + " has a notch at "
                   + entry.getValue() + ", should be between 1 and " + maxAlphabetLength + "." +  System.lineSeparator()));
           errorIndex++;
        }
    }
    }

    private void addDuplicateleftColException(){
        if(!leftColDuplicateChars.isEmpty()) {
           for(Map.Entry<Integer, List<Character>> entry : leftColDuplicateChars.entrySet()) {
                exceptions.add(new Exception((EXCEPTION_IDENTATION + errorIndex.toString()
                   + ": The left col in rotor "+ entry.getKey()  + " contains duplications of the following letters:" + System.lineSeparator()
                   + EXCEPTION_IDENTATION + INDEX_IDENTATION + entry.getValue() + System.lineSeparator())));
                errorIndex++;
            }
        }
    }
    private void addDuplicateRightColException(){
        if(!RightColDuplicateChars.isEmpty()) {
            for(Map.Entry<Integer, List<Character>> entry : RightColDuplicateChars.entrySet()) {
                exceptions.add(new Exception((EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The right col in rotor "+ entry.getKey()  + " contains duplications of the following letters:" + System.lineSeparator()
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION + entry.getValue() + System.lineSeparator())));
                errorIndex++;
            }
        }
    }
    private void addMissingRotorsIdsException(){
        if(!MissingRotorsIdsInSequenceList.isEmpty()) {
            for(Integer id : MissingRotorsIdsInSequenceList) {
                exceptions.add(new Exception((EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The rotor " + id + " is missing from the desired sequence." + System.lineSeparator())));
                errorIndex++;
            }
        }
    }
    //end region
    public void addDUplicatedCharToLeftCol(int id, String left) {
        if(leftColDuplicateChars.containsKey(id)) {
            leftColDuplicateChars.get(id).add(left.charAt(0));
        } else {
            leftColDuplicateChars.put(id, new ArrayList<>(Arrays.asList(left.charAt(0))));
        }

    }
    public void addDUDuplicatedCharToRightCol(int id, String right) {
        if(RightColDuplicateChars.containsKey(id)) {
            RightColDuplicateChars.get(id).add(right.charAt(0));
        } else {
            RightColDuplicateChars.put(id, new ArrayList<>(Arrays.asList(right.charAt(0))));
        }
    }

    public void addDuplicatedRotorId(int id) {
        RotorsWithSameId.put(id, RotorsWithSameId.getOrDefault(id, 1) + 1);
    }

    public void addRotorsToOutOfRangeList(int id) {
        rotorsWithNotchOutOfRange.put(id, rotorsWithNotchOutOfRange.getOrDefault(id, 1) + 1);

    }

    public List<Exception> getExceptions() {
        return exceptions;
    }

    public boolean isGeneratedRotorsIdsInOrder(Map<Integer, Boolean> generatedRotorsIds) {
        return generatedRotorsIds.values().stream().allMatch(b -> b);
    }

    public Exception addRotorIdsNotInOrder(Map<Integer, Boolean> generatedRotorsIds) {
        Exception rotorsNotInOrder = new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                + ": The rotor ids are not in the right order(missing Ids in the sequence): " + System.lineSeparator());
        errorIndex++;
        return rotorsNotInOrder;
    }

    public void setRotorsIdsNotInSequenceList(Map<Integer,Boolean> IdsMap) {
        for(Map.Entry<Integer, Boolean> entry : IdsMap.entrySet()) {
            if(!entry.getValue()) {
                MissingRotorsIdsInSequenceList.add(entry.getKey());
            }
        }
    }
}

