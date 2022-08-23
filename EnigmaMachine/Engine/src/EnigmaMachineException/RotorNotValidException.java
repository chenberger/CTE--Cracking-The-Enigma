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
    private int maxRotorId;
    private final Map<Integer,List<String>>RotorsWithnotValidLetters;
    private final Map<Integer,Integer> rotorsWithNotEnoghPositions;
    private final Map<Integer, Integer> rotorsWithNotchOutOfRange;
    private final Map<Integer,List<Character>> leftColDuplicateChars;
    private final Map<Integer,List<Character>> RightColDuplicateChars;
    private final List<Integer> rotorsWithIdsOutOfRange;
    private boolean rotorsCountOutOfRange;
    private int numberOfRotorsToAdd;
    List<Integer> MissingRotorsIdsInSequenceList;

    private boolean oddLength = false;
    private Map<Character, List<Character>> sameMappingInOneRotor;

    public RotorNotValidException(){
        exceptions = new ArrayList<>();
        RotorsWithSameId = new HashMap<>();
        RotorsWithnotValidLetters = new HashMap<>();
        rotorsWithIdsOutOfRange = new ArrayList<>();
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
    public void setMaxAlphabetLength(int maxAlphabetLength) {
        this.maxAlphabetLength = maxAlphabetLength;
    }
    public void setMaxRotorId(int maxRotorId) {
        this.maxRotorId = maxRotorId;
    }

    public void addExceptionsToTheList(){
        addRotorsCountOutOfRangeException();
        addRotorsDuplicateException();
        addRotorsWithNotEnoughPositionsException();
        addRotorsWithNotchOutOfRangeException();
        addRotorsWithNotValidLettersException();
        addDuplicateLeftColException();
        addDuplicateRightColException();
        addMissingRotorsIdsException();
        addRotorsToOutOfRangeIdsListException();
        addNotEnoughRotorsException();

        }

    public boolean shouldThrowException() {
        return exceptions.size() > 0;
    }
    @Override
    public String getMessage() {
        return System.lineSeparator() + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
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

    public void setNumberOfPairsInRotorInvalid(CTERotor rotor, List<Character> cteABC) {
        if(rotor.getCTEPositioning().size() != cteABC.size()) {
            rotorsWithNotEnoghPositions.put(rotor.getId(), rotor.getCTEPositioning().size());
        }
        maxAlphabetLength = cteABC.size();
    }

    public void addNotchOutOfRange(int id, int notch) {
        rotorsWithNotchOutOfRange.put(id, notch);
    }
    //addException region
    private void addNotEnoughRotorsException() {
        if(numberOfRotorsToAdd > 0) {
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString() + ": There are not enough rotors in the file you inserted(number of rotors < rotors count): "
                    + System.lineSeparator() + EXCEPTION_IDENTATION + INDEX_IDENTATION + "you need to add " + numberOfRotorsToAdd + " rotor/s, or reduce the rotors count by "
                    + numberOfRotorsToAdd
                    + System.lineSeparator()));
        }
    }
    private void addRotorsToOutOfRangeIdsListException() {
        if(rotorsWithIdsOutOfRange.size() > 0) {
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString() + ": Rotors with Ids out of range:" + System.lineSeparator()
                     + EXCEPTION_IDENTATION + INDEX_IDENTATION + rotorsWithIdsOutOfRange
                     + ", all Ids Should be between 1 and " + maxRotorId + System.lineSeparator()));
            errorIndex++;
    }
    }
    private void addRotorsDuplicateException(){
        if(!RotorsWithSameId.isEmpty()){
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
            + ": The following rotors appear more then once: " + RotorsWithSameId.keySet() + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION + System.lineSeparator()));
            errorIndex++;
        }
    }
    private void addRotorsWithNotValidLettersException(){
        if(!RotorsWithnotValidLetters.isEmpty()) {
            for (Map.Entry<Integer, List<String>> entry : RotorsWithnotValidLetters.entrySet()) {
                exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                + ": Rotor " + entry.getKey() + " contains the following invalid letters: "
                        + System.lineSeparator()
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION + entry.getValue() + System.lineSeparator()));
            }
            errorIndex++;
        }
    }
    private void addRotorsWithNotEnoughPositionsException() {
        if(!rotorsWithNotEnoghPositions.isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : rotorsWithNotEnoghPositions.entrySet()) {
                exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": Rotor " + entry.getKey() + " has " + entry.getValue()
                        + " positions, should be " + maxAlphabetLength + "." + System.lineSeparator()));
                errorIndex++;
            }
        }
    }

    private void addRotorsWithNotchOutOfRangeException(){
        if(!rotorsWithNotchOutOfRange.isEmpty()){
        for(Map.Entry<Integer, Integer> entry : rotorsWithNotchOutOfRange.entrySet()) {
           exceptions.add(new Exception( EXCEPTION_IDENTATION + errorIndex.toString()
                   + ": Rotor " + entry.getKey() + " has his notch in position "
                   + entry.getValue() + ", the notch position should be between 1 and " + maxAlphabetLength + "." +  System.lineSeparator()));
           errorIndex++;
        }
    }
    }

    private void addDuplicateLeftColException(){
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
                        + ": Rotor " + id + "(id) is missing from the desired ids sequence." + System.lineSeparator())));
                errorIndex++;
            }
        }
    }
    private void addRotorsCountOutOfRangeException(){
        if(rotorsCountOutOfRange) {
            exceptions.add(new Exception((EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The inserted rotors count is invalid, should be between 2 and 99."  + System.lineSeparator())));
            errorIndex++;
        }
    }
    //end region
    public void addDuplicatedCharToLeftCol(int id, String left) {
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
        rotorsWithIdsOutOfRange.add(id);
    }

    public void setRotorsIdsNotInSequenceList(Map<Integer,Boolean> IdsMap) {
        for(Map.Entry<Integer, Boolean> entry : IdsMap.entrySet()) {
            if(!entry.getValue()) {
                MissingRotorsIdsInSequenceList.add(entry.getKey());
            }
        }
    }

    public void setRotorsCountOutOfRange() {
        rotorsCountOutOfRange = true;
    }
}

