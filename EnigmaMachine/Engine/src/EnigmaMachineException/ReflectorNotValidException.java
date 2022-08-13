package EnigmaMachineException;

import EnigmaMachine.RomanNumber;
import Jaxb.Schema.Generated.CTEReflector;

import java.util.*;
import java.util.stream.Collectors;

public class ReflectorNotValidException extends Exception {

    private final String EXCEPTION_IDENTATION = "       ";
    private final String INDEX_IDENTATION = "   ";
    private Integer errorIndex;
    private final String startingMessage = "Reflectors Sector:" + System.lineSeparator();
    private final List<Exception> exceptions;
    int maxPairsInAlphabet;
    private final Map<String, List<Integer>> indexesOutOfRange = new HashMap<>();
    private final Map<String, List<Integer>> outPutColDuplicateIndexes = new HashMap<>();
    private final Map<String, List<Integer>> inputColDuplicateIndexes = new HashMap<>();
    private final Map<String, List<Integer>>  indexesMappedToThemselves = new HashMap<>();
    private boolean NoReflectorsFound = false;
    private boolean tooManyReflectors = false;
    private final Map<String,Integer> invalidSizedReflectors = new HashMap<>();
    private final List<String> outOfRangeReflectors = new ArrayList<>();
    private boolean reflectorsNotInOrder = false;
    private List<String> missingReflectorsIdsFromSequenceList = new ArrayList<>();
    public ReflectorNotValidException() {
        this.errorIndex = 1;
        this.exceptions = new ArrayList<>();
    }
    @Override
    public String getMessage() {
        return System.lineSeparator() + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }
    public void addExceptionsToTheList() {
        addMissingReflectorsIdsFromSequenceList();
        addReflectorsWithIndexesOutOfRangeException();
        addInputColDuplicateIndexesException();
        addOutputColDuplicateIndexesException();
        addReflectorsWithIndexesMappedToThemselves();
        addNoReflectorsFoundException();
        addTooManyReflectorsException();
        addOutOfRangeReflectorsException();
        addInvalidSizedReflectorsException();

    }
//region add Exceptions to the list
    private void addReflectorsWithIndexesOutOfRangeException(){
        if(!indexesOutOfRange.isEmpty()){
            for(Map.Entry<String, List<Integer>> entry : indexesOutOfRange.entrySet()){
                exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The indexes in reflector" + entry.getKey() + "that were inserted are out of range" + System.lineSeparator()
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The indexes that were inserted are: " + entry.getValue() + System.lineSeparator()
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The indexes that can be inserted are: " + (maxPairsInAlphabet) + System.lineSeparator()));
                errorIndex++;
            }
        }
    }
    private void addInputColDuplicateIndexesException() {
        if (inputColDuplicateIndexes.size() > 0) {
            for (Map.Entry<String, List<Integer>> entry : inputColDuplicateIndexes.entrySet()) {
                exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The input column in reflector " + entry.getKey() + " contains duplicate indexes which are: "
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION
                        + entry.getValue() + System.lineSeparator()));
                errorIndex++;

            }
        }
    }

    private void addOutputColDuplicateIndexesException() {
        if (outPutColDuplicateIndexes.size() > 0) {
            for (Map.Entry<String, List<Integer>> entry : outPutColDuplicateIndexes.entrySet()) {
                exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The output column in reflector " + entry.getKey() + " contains duplicate indexes which are: "
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION
                        + entry.getValue() + System.lineSeparator()));
                errorIndex++;

            }
        }
    }
    private void addReflectorsWithIndexesMappedToThemselves() {
        if(indexesMappedToThemselves.size() > 0) {
            for (Map.Entry<String, List<Integer>> entry : indexesMappedToThemselves.entrySet()) {
                exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The Reflector: "+ entry.getKey() +"has the following indices mapped to themselves: "
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION
                        + entry.getValue() + System.lineSeparator()));
                errorIndex++;
            }
        }
    }
    private void addNoReflectorsFoundException() {
        if(NoReflectorsFound) {
            exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": There are no reflectors found in the file you inserted" + System.lineSeparator()));
            errorIndex++;
        }
    }
    private void addTooManyReflectorsException() {
        if (tooManyReflectors) {
            exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The amount of reflectors that was inserted is illegal" + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The amount that you need to insert is between 1 and 5" + System.lineSeparator()));
        }
    }
    private void addOutOfRangeReflectorsException() {
        if(outOfRangeReflectors.size() > 0) {
            exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The following reflectors are out of range" + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + String.join("", outOfRangeReflectors) + System.lineSeparator()));
            errorIndex++;
        }
    }
    private void addInvalidSizedReflectorsException() {
        if(invalidSizedReflectors.size() > 0) {
            exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The following reflectors are invalid sized:" + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + invalidSizedReflectors.entrySet().stream().map(entry -> entry.getKey() + ": "
                    + entry.getValue()).collect(Collectors.joining("")) + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + "Should be in size of " + maxPairsInAlphabet + System.lineSeparator()));
            errorIndex++;
        }
    }

    private void addMissingReflectorsIdsFromSequenceList() {
        if(missingReflectorsIdsFromSequenceList.size() > 0) {
            exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The following reflectors Ids are missing in the desired sequence:" + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + String.join(", ", missingReflectorsIdsFromSequenceList) + System.lineSeparator()));
            errorIndex++;
        }
    }
    //endregion
    public boolean shouldThrowException() {
        return exceptions.size() > 0;// no exceptions to throw
    }
    public void checkIfReflectorsIdsInSequence(Map<String, Boolean> reflectorsIdsInOrder) {
        for(Map.Entry<String, Boolean> entry : reflectorsIdsInOrder.entrySet()) {
            if (!entry.getValue()) {
                reflectorsNotInOrder = true;
                break;
            }
        }
    }

    public void addReflectorsToOutOfRangeList(String reflectorToAdd) {
        outOfRangeReflectors.add(reflectorToAdd);
    }

    public void setToManyReflectors() {
        this.tooManyReflectors = true;
    }

    public void setNumberOfPairsInReflectorInvalid(CTEReflector reflector, List<Character> cteAbc) {
        invalidSizedReflectors.put(reflector.getId(), reflector.getCTEReflect().size());
    }

    public void setReflectorsEmpty() {
        NoReflectorsFound = true;
    }
    //region setters
    public void setNoReflectorsFound() {
        this.NoReflectorsFound = true;
    }
    public void setMaxPairsInAlphabet(int maxPairsInAlphabet) {
        this.maxPairsInAlphabet = maxPairsInAlphabet;
    }
    public void addReflectorIndexOutOfRange(String id, int input) {
        if(indexesOutOfRange.containsKey(id)) {
            indexesOutOfRange.get(id).add(input);
        }
        else {
            indexesOutOfRange.put(id, new ArrayList<>(Arrays.asList(input)));
        }
    }
    public void addIndexMappedToHimSelf(int input, String id) {
        if(indexesMappedToThemselves.containsKey(id)) {
            indexesMappedToThemselves.get(id).add(input);
        }
        else {
            indexesMappedToThemselves.put(id, new ArrayList<>(Arrays.asList(input)));
        }
    }
    public void addReflectorDuplicateOutput(String id, int output) {
        if(outPutColDuplicateIndexes.containsKey(id)) {
            outPutColDuplicateIndexes.get(id).add(output);
        }
        else {
            outPutColDuplicateIndexes.put(id, new ArrayList<>(Arrays.asList(output)));
        }
    }

    public void addReflectorDuplicateInput(String id, int input) {
        if(inputColDuplicateIndexes.containsKey(id)) {
            inputColDuplicateIndexes.get(id).add(input);
        }
        else {
            inputColDuplicateIndexes.put(id, new ArrayList<>(Arrays.asList(input)));
        }
    }

    public void setMissingReflectorsIdsFromSequenceList(Map<String, Boolean> insertedReflectorsIds,int numberOfInsertedReflectors) {
        int counter = 0;
        for(RomanNumber romanNumber : RomanNumber.values()) {
            if(!insertedReflectorsIds.get(romanNumber.toString()) && counter < numberOfInsertedReflectors) {
                missingReflectorsIdsFromSequenceList.add(romanNumber.toString());
            }
            counter++;
        }
    }
}
