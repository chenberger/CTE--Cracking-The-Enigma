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
    private final Map<String,List<Integer>> indexesAppearsMoreThanOnce = new HashMap<>();
    private final List<String> outOfRangeReflectors = new ArrayList<>();
    private final List<String> reflectorsIdsDuplicateList = new ArrayList<>();

    private final List<String>reflectorsIdsNotValidList = new ArrayList<>();
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
        addDuplicateReflectorsException();
        addMissingReflectorsIdsFromSequenceList();
        addReflectorsWithIndexesOutOfRangeException();
        addInputColDuplicateIndexesException();
        addOutputColDuplicateIndexesException();
        addReflectorsWithIndexesMappedToThemselves();
        addNoReflectorsFoundException();
        addTooManyReflectorsException();
        addOutOfRangeReflectorsException();
        addInvalidSizedReflectorsException();
        addDuplicatedIndexesInReflectorsException();

    }
//region add Exceptions to the list
    private void addDuplicatedIndexesInReflectorsException() {
        if(!indexesAppearsMoreThanOnce.isEmpty()){
            for(Map.Entry<String,List<Integer>> entry : indexesAppearsMoreThanOnce.entrySet()){
                String message = "";
                message += EXCEPTION_IDENTATION +errorIndex.toString() + ": Reflector " + entry.getKey() + " has indexes that appears more than once and they are: "
                        + System.lineSeparator() + EXCEPTION_IDENTATION + INDEX_IDENTATION +  entry.getValue() + System.lineSeparator();
                exceptions.add(new Exception(message));
            }
            errorIndex++;
        }
    }
    private void addDuplicateReflectorsException() {
        if (!reflectorsIdsDuplicateList.isEmpty()) {
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The following reflectors appears more than once: "+ System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION   + reflectorsIdsDuplicateList + System.lineSeparator()));
            errorIndex++;
        }
    }
    private void addReflectorsWithIndexesOutOfRangeException(){
        if(!indexesOutOfRange.isEmpty()){
            for(Map.Entry<String, List<Integer>> entry : indexesOutOfRange.entrySet()){
                exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The following indexes in reflector " + entry.getKey() + " are out of range: " + System.lineSeparator()
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION + entry.getValue() + System.lineSeparator()
                        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "all indexes should be between 1 to " + (maxPairsInAlphabet) * 2 + System.lineSeparator()));
                errorIndex++;
            }
        }
    }
    private void addInputColDuplicateIndexesException() {
        if (inputColDuplicateIndexes.size() > 0) {
            for (Map.Entry<String, List<Integer>> entry : inputColDuplicateIndexes.entrySet()) {
                exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The input column in reflector " + entry.getKey() + " contains indexes which appear more than once and they are: "
                        + System.lineSeparator()
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
                        + ": The output column in reflector " + entry.getKey() + " contains indexes which appear more than once and they are: "
                        + System.lineSeparator()  + EXCEPTION_IDENTATION + INDEX_IDENTATION
                        + entry.getValue() + System.lineSeparator()));
                errorIndex++;

            }
        }
    }
    private void addReflectorsWithIndexesMappedToThemselves() {
        if(indexesMappedToThemselves.size() > 0) {
            for (Map.Entry<String, List<Integer>> entry : indexesMappedToThemselves.entrySet()) {
                exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                        + ": The Reflector: "+ entry.getKey() +" has the following indexes mapped to themselves: " + System.lineSeparator()
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
                    + ": The following reflectors ids are invalid:" + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + outOfRangeReflectors +", all ids should be from the following ids: " + Arrays.toString(Arrays.stream(RomanNumber.values()).toArray()) + System.lineSeparator()));
            errorIndex++;
        }
    }
    private void addInvalidSizedReflectorsException() {
        if(invalidSizedReflectors.size() > 0) {
            exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The following reflectors contains illegal number of pairs:" + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + invalidSizedReflectors.entrySet().stream().map(entry -> entry.getKey() + " contains the following number of pairs: "
                    + entry.getValue()).collect(Collectors.joining(System.lineSeparator() +EXCEPTION_IDENTATION + INDEX_IDENTATION)) + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + "All reflectors should contain " + maxPairsInAlphabet +" pairs." + System.lineSeparator()));
            errorIndex++;
        }
    }

    private void addMissingReflectorsIdsFromSequenceList() {
        if(missingReflectorsIdsFromSequenceList.size() > 0) {
            exceptions.add(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The following reflector/s Ids are missing in the desired sequence:" + System.lineSeparator()
                    + EXCEPTION_IDENTATION + INDEX_IDENTATION
                    + missingReflectorsIdsFromSequenceList + System.lineSeparator()));
            errorIndex++;
        }
    }

    public List<Exception> getExceptions() {
        return exceptions;
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

    public void addReflectorIdDuplicate(String id) {
        if(!reflectorsIdsDuplicateList.contains(id)) {
            reflectorsIdsDuplicateList.add(id);
        }
    }

    public void addIndexAppearsMoreThanOnce(String reflectorId, int index) {
        if(indexesAppearsMoreThanOnce.containsKey(reflectorId)) {
            if(!indexesAppearsMoreThanOnce.get(reflectorId).contains(index)){
                indexesAppearsMoreThanOnce.get(reflectorId).add(index);
            };
        }
        else {
            indexesAppearsMoreThanOnce.put(reflectorId, new ArrayList<>(Arrays.asList(index)));
        }
    }
}
