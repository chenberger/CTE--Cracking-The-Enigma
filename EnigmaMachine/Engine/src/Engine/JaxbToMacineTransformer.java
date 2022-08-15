package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.*;
import Jaxb.Schema.Generated.*;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import EnigmaMachine.Reflector;
import EnigmaMachine.RomanNumber;
import EnigmaMachine.Rotor;

public class JaxbToMacineTransformer {

    private final GeneralEnigmaMachineException enigmaMachineException;
    JaxbToMacineTransformer(){
        this.enigmaMachineException = new GeneralEnigmaMachineException();
    }

    public CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("Jaxb.Schema.Generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public EnigmaMachine transformJAXBClassesToEnigmaMachine(CTEEnigma JAXBGeneratedEnigma) throws GeneralEnigmaMachineException {
        // TODO implement here also validation.(the file exist),exceptions. also change the init settings to false.
        List<Character> generatedABC = new ArrayList<>();
        ABCNotValidException abcNotValidException = new ABCNotValidException();
        checkIfABCIsValid(JAXBGeneratedEnigma.getCTEMachine().getABC(), abcNotValidException);
        generatedABC = getABCFromString(JAXBGeneratedEnigma.getCTEMachine().getABC().trim());

        abcNotValidException.addExceptionsToTheList();
        throwExceptionIfAlphabetNotValid(abcNotValidException);


        List<CTERotor> CTERotors = JAXBGeneratedEnigma.getCTEMachine().getCTERotors().getCTERotor();
        List<CTEReflector> CTEReflectors = JAXBGeneratedEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        Map<Integer, Rotor> machineRotors;
        Map<RomanNumber, Reflector> machineReflectors;
        Map<Character,Integer> machineKeyBoard;
        int rotorsInUseCounter = JAXBGeneratedEnigma.getCTEMachine().getRotorsCount();;

        machineKeyBoard = getMachineKeyboardFromCTEKeyboard(generatedABC, abcNotValidException);
        //if(abcNotValidException.shouldThrowException()){
        //    enigmaMachineException.addException(abcNotValidException);
        //    throw enigmaMachineException;
        //}

        machineRotors = getMachineRotorsFromCTERotors(CTERotors, generatedABC, rotorsInUseCounter);
        machineReflectors = getMachineReflectorsFromCTEReflectors(CTEReflectors,generatedABC);

        if(!enigmaMachineException.isExceptionNeedToThrown()) {
            EnigmaMachine enigmaMachine = new EnigmaMachine(machineRotors, machineReflectors, machineKeyBoard, rotorsInUseCounter);
            return enigmaMachine;
        }
        else {
            throw enigmaMachineException;
        }
    }
    private void throwExceptionIfAlphabetNotValid(ABCNotValidException abcNotValidException) throws GeneralEnigmaMachineException {
        if(abcNotValidException.shouldThrowException()){
            enigmaMachineException.addException(abcNotValidException);
            throw enigmaMachineException;
        }
    }

    private void checkIfABCIsValid(String abc,ABCNotValidException abcNotValidException ) {
        abc = abc.trim();
        if(abc.length() == 0) {
            abcNotValidException.setABCempty();
        }
        else if(abc.length()%2 != 0) {
            abcNotValidException.setIsOddLength();
        }
    }

    private List<Character> getABCFromString(String abc) {
        List<Character> generatedABC = new ArrayList<>();
        for (int i = 0; i < abc.length(); i++) {
            generatedABC.add(abc.charAt(i));
        }
        return generatedABC;
    }

    //private boolean checkIfKeyBoardContainsDuplications(String abc, ABCNotValidException abcNotValidException, List<Character> generatedABC) {
    //    Map<Character, Integer> abcMap = new HashMap<>();
    //    for(int i = 0; i < abc.length(); i++){
    //        if(abcMap.containsKey(abc.charAt(i))){
    //            abcNotValidException.addCharToDuplicateChars(abc.charAt(i));
    //        }
    //        else{
    //            abcMap.put(abc.charAt(i), 1);
    //        }
    //    }
    //    for(Character charInABC : abcMap.keySet()){
    //        if(abcMap.get(charInABC) > 1){
    //            abcNotValidException.addCharToDuplicateChars(charInABC);
    //        }
    //        else {
    //            generatedABC.add(charInABC);
    //        }
    //    }
//
    //    return false;
    //}
    private Map<RomanNumber, Reflector> getMachineReflectorsFromCTEReflectors(List<CTEReflector> cteReflectors, List<Character> CTEAbc) {
        Map<RomanNumber, Reflector> machineReflectors = new HashMap<>();
        Map<String, Boolean> insertedReflectorsIds = fillReflectorMapIdsMapWithFalseValues(cteReflectors.size());

        ReflectorNotValidException reflectorNotValidException = new ReflectorNotValidException();
        reflectorNotValidException.setMaxPairsInAlphabet(CTEAbc.size()/2);
        addNumberOfReflectorsNotValidExceptionIfNecessary(cteReflectors.size(), reflectorNotValidException);

        fillMapByInsertedReflectors(cteReflectors, insertedReflectorsIds);
        addReflectorsIdsNotValidExceptionIfNecessary(insertedReflectorsIds, reflectorNotValidException, cteReflectors.size());

        checkIfReflectorsIdsContainDuplication(cteReflectors, reflectorNotValidException);
        for(CTEReflector reflector: cteReflectors){
            if(IsReflectorIdIsValid(reflector, reflectorNotValidException) && numberOfPairsInReflectorValid(reflector,CTEAbc,reflectorNotValidException)) {
                addReflectorToMachineReflectors(reflector, machineReflectors, reflectorNotValidException, CTEAbc, insertedReflectorsIds);
            }
        }
        reflectorNotValidException.addExceptionsToTheList();
        if(reflectorNotValidException.shouldThrowException()){
            enigmaMachineException.addException(reflectorNotValidException);
        }
        return machineReflectors;
    }

    private void addReflectorToMachineReflectors(CTEReflector reflector, Map<RomanNumber,Reflector> machineReflectors, ReflectorNotValidException reflectorNotValidException, List<Character> CTEAbc, Map<String, Boolean> insertedReflectorsIds) {
        Map<Integer,Integer> currentReflectorMapping = new HashMap<>();
        Map<Integer,Integer> outPutColMap = new HashMap<>();
        Map<Integer,Integer> inputColMap = new HashMap<>();

        for(CTEReflect reflect:reflector.getCTEReflect()){
            addIndexesOfReflectOutOfRangeExceptionIfNecessary(reflector.getId(),reflect, reflectorNotValidException, CTEAbc);
            addReflectorsDuplicatesExceptionIfNecessary(reflector.getId(),reflect,outPutColMap,inputColMap, reflectorNotValidException);
            outPutColMap.put(reflect.getOutput(),1);
            inputColMap.put(reflect.getInput(),1);
            addIndexesMappedToThemselvesExceptionIfNecessary(reflector.getId(),reflect, reflectorNotValidException);
            currentReflectorMapping.put(reflect.getInput() - 1, reflect.getOutput() - 1);
            currentReflectorMapping.put(reflect.getOutput() - 1,reflect.getInput() - 1);
        }
        insertedReflectorsIds.put(reflector.getId(),true);
        Reflector currentReflector = new Reflector(RomanNumber.valueOf(reflector.getId()), currentReflectorMapping);
        machineReflectors.put(RomanNumber.convertStringToRomanNumber(reflector.getId()),currentReflector);
    }

    private void addIndexesMappedToThemselvesExceptionIfNecessary(String reflectorId, CTEReflect reflect, ReflectorNotValidException reflectorNotValidException) {
        if(reflect.getInput() == reflect.getOutput()){
            reflectorNotValidException.addIndexMappedToHimSelf(reflect.getInput(),reflectorId);
        }
    }

    private void addReflectorsDuplicatesExceptionIfNecessary(String reflectorId, CTEReflect reflect, Map<Integer, Integer> outPutColMap, Map<Integer, Integer> inputColMap, ReflectorNotValidException reflectorNotValidException) {
        if(outPutColMap.containsKey(reflect.getOutput())){
            reflectorNotValidException.addReflectorDuplicateOutput(reflectorId,reflect.getOutput());
        }
        if(inputColMap.containsKey(reflect.getInput())){
            reflectorNotValidException.addReflectorDuplicateInput(reflectorId,reflect.getInput());
        }
    }

    private void addIndexesOfReflectOutOfRangeExceptionIfNecessary(String reflectorId,CTEReflect reflect, ReflectorNotValidException reflectorNotValidException, List<Character> cteAbc) {
        if(indexOutOfRange(reflect.getInput(), cteAbc)){
            reflectorNotValidException.addReflectorIndexOutOfRange(reflectorId,reflect.getInput());
        }
        else if(indexOutOfRange(reflect.getOutput(), cteAbc)) {
            reflectorNotValidException.addReflectorIndexOutOfRange(reflectorId, reflect.getOutput());
        }
    }

    private void addReflectorsIdsNotValidExceptionIfNecessary(Map<String, Boolean> insertedReflectorsIds, ReflectorNotValidException reflectorNotValidException, int numberOfReflectors) {
        if(!allReflectorsIdsInSequence(insertedReflectorsIds,numberOfReflectors)){
            reflectorNotValidException.setMissingReflectorsIdsFromSequenceList(insertedReflectorsIds,numberOfReflectors);
        }
    }

    private void addNumberOfReflectorsNotValidExceptionIfNecessary(int numberOfReflectors, ReflectorNotValidException reflectorNotValidException) {
        if(numberOfReflectors == 0) {
            reflectorNotValidException.setReflectorsEmpty();
        }
        else if(numberOfReflectors > 5) {
            reflectorNotValidException.setToManyReflectors();
        }
    }

    private void checkIfReflectorsIdsContainDuplication(List<CTEReflector> cteReflectors, ReflectorNotValidException reflectorNotValidException) {
        Map<String, Boolean> insertedReflectorsIds = fillReflectorMapIdsMapWithFalseValues(cteReflectors.size());
        for(CTEReflector reflector: cteReflectors){
            if(insertedReflectorsIds.get(reflector.getId()) == true){
                reflectorNotValidException.addReflectorIdDuplicate(reflector.getId());
            }
            else{
                insertedReflectorsIds.put(reflector.getId(),true);
            }
        }
    }

    private void fillMapByInsertedReflectors(List<CTEReflector> cteReflectors, Map<String, Boolean> insertedReflectorsIds) {
        for(CTEReflector reflector: cteReflectors){
            insertedReflectorsIds.put(reflector.getId(),true);
        }
    }

    private boolean allReflectorsIdsInSequence(Map<String, Boolean> insertedReflectorsIds, int size) {
        int numberOfInsertedReflectorsIdsCounter = 0;
        for(RomanNumber id : RomanNumber.values()){
            if(!insertedReflectorsIds.get(id.toString()) && numberOfInsertedReflectorsIdsCounter < size){
                return false;
            }
            numberOfInsertedReflectorsIdsCounter++;
        }
        return true;
    }

    private boolean indexOutOfRange(int input, List<Character> cteAbc) {
        return input > cteAbc.size();
    }

    private boolean numberOfPairsInReflectorValid(CTEReflector reflector, List<Character> CTEAbc,ReflectorNotValidException reflectorNotValidException) {
        if(reflector.getCTEReflect().size() != CTEAbc.size()/2) {
            reflectorNotValidException.setNumberOfPairsInReflectorInvalid(reflector,CTEAbc);
            return false;
        }
        return true;
    }

    private boolean IsReflectorIdIsValid(CTEReflector reflector,ReflectorNotValidException reflectorNotValidException) {
        if(!isValidRomanNumber(reflector.getId())) {
            reflectorNotValidException.addReflectorsToOutOfRangeList(reflector.getId());
            return false;
        }
        return true;
    }
    private boolean isValidRomanNumber(String romanNumberString){
        boolean isValid = false;
        for(RomanNumber romanNumber: RomanNumber.values()){
            if(romanNumber.toString().equals(romanNumberString)){
                isValid = true;
            }
        }
        return isValid;
    }
    private Map<String, Boolean> fillReflectorMapIdsMapWithFalseValues(int size){
        Map<String , Boolean> idsMap = new HashMap<>(size);
        for(RomanNumber romanNumber: RomanNumber.values()){
            idsMap.put(romanNumber.toString(),false);
        }
        return idsMap;
    }
    private Map<Integer,Boolean> fillRotorsMapWithFalseValues(int size){
        Map<Integer,Boolean> idsMap = new HashMap<>(size);
        for(int i = 1; i <= size; i++){
            idsMap.put(i,false);
        }
        return idsMap;
    }

    private Map<Integer, Rotor> getMachineRotorsFromCTERotors(List<CTERotor> cteRotors, List<Character> cteABC, int rotorsCount) {
        Map<Integer, Rotor> machineRotors = new HashMap<Integer, Rotor>();
        Map<Integer,Boolean> generatedRotorsIds = fillRotorsMapWithFalseValues(cteRotors.size());
        RotorNotValidException rotorNotValidException = new RotorNotValidException();
        rotorNotValidException.setMaxAlphabetLength(cteABC.size());
        rotorNotValidException.setMaxRotorId(cteRotors.size());
        fillIdsMapByInsertedRotors(cteRotors, generatedRotorsIds);
        checkIfRotorsIdsAreValid(cteRotors, rotorNotValidException);
        checkIfRotorsCountIsValid(rotorsCount, rotorNotValidException);
        addNotEnoughRotorsExceptionIfNecessary(rotorsCount, cteRotors.size(), rotorNotValidException);
        addNotAllRotorsIdsInSequenceExceptionIfNecessary(generatedRotorsIds, rotorNotValidException);

        for(CTERotor rotor: cteRotors){
            addRotorToMachineRotors(rotor, machineRotors, cteABC, rotorNotValidException, generatedRotorsIds);
        }
        rotorNotValidException.addExceptionsToTheList();
        if(rotorNotValidException.shouldThrowException()){
            enigmaMachineException.addException(rotorNotValidException);
        }
        return machineRotors;
    }

    private void addRotorToMachineRotors(CTERotor rotor, Map<Integer,Rotor> machineRotors, List<Character> cteABC, RotorNotValidException rotorNotValidException, Map<Integer, Boolean> generatedRotorsIds) {
        Map<Character,Character> currentRotorMap = new HashMap<>();
        Map<String,Integer> leftColInRotor = new HashMap<>();
        Map<String,Integer> rightColInRotor = new HashMap<>();
        addNotchOutOfRangeExceptionIfNecessary(rotor.getId(),rotor.getNotch(),cteABC.size(), rotorNotValidException);
        addNumberOfPositionsNotValidExceptionIfNecessary(rotor,cteABC,rotor.getCTEPositioning().size(), rotorNotValidException);

        List<Pair<Character,Character>> currentRotorPairs = new ArrayList<>();
        for(CTEPositioning position: rotor.getCTEPositioning()){
            addPositionToTheRotor(currentRotorPairs,rotor.getId(),position, currentRotorMap, cteABC, rotorNotValidException,leftColInRotor,rightColInRotor);
        }

        generatedRotorsIds.put(rotor.getId() ,true);
        Rotor currentRotor = new Rotor(rotor.getId(), rotor.getNotch() - 1, currentRotorPairs);
        machineRotors.put(rotor.getId(),currentRotor);
    }

    private void addNumberOfPositionsNotValidExceptionIfNecessary(CTERotor rotor, List<Character> cteABC, int numberOfPositions, RotorNotValidException rotorNotValidException) {
        if(numberOfPositions != cteABC.size()){
            rotorNotValidException.setNumberOfPairsInRotorInvalid(rotor,cteABC);
        }
    }

    private void addNotchOutOfRangeExceptionIfNecessary(int rotorId, int rotorNotch,int AbcSize, RotorNotValidException rotorNotValidException) {
        if(rotorNotch > AbcSize || rotorNotch < 0){
            rotorNotValidException.addNotchOutOfRange(rotorId,rotorNotch);
        }
    }

    private void addPositionToTheRotor(List<Pair<Character, Character>> currentRotorPairs, int rotorId, CTEPositioning position, Map<Character, Character> currentRotorMap, List<Character> cteABC, RotorNotValidException rotorNotValidException, Map<String,Integer> leftColInRotor, Map<String,Integer> rightColInRotor ) {
        checkIfPositionLettersInABC(position, cteABC, rotorNotValidException, rotorId);

        if(leftColInRotor.containsKey(position.getLeft())){
            rotorNotValidException.addDuplicatedCharToLeftCol(rotorId,position.getLeft());
        }
        if(rightColInRotor.containsKey(position.getRight())){
            rotorNotValidException.addDUDuplicatedCharToRightCol(rotorId,position.getRight());
        }
        leftColInRotor.put(position.getLeft(),1);
        rightColInRotor.put(position.getRight(),1);

        Pair<Character,Character> currentPair = new Pair<>(position.getLeft().charAt(0),position.getRight().charAt(0));
        // TODO check if the Length is 1 and if the character is in ABC, and that there are no duplicates of chars in each side where ever there are numbers, check that they are ints.
        currentRotorPairs.add(currentPair);
        currentRotorMap.put(position.getLeft().charAt(0),position.getRight().charAt(0));
    }

    private void addNotAllRotorsIdsInSequenceExceptionIfNecessary(Map<Integer, Boolean> generatedRotorsIds, RotorNotValidException rotorNotValidException) {
        if(!AllRotorsIdsInSequence(generatedRotorsIds)) {
            rotorNotValidException.setRotorsIdsNotInSequenceList(generatedRotorsIds);
        }
    }

    private void addNotEnoughRotorsExceptionIfNecessary(int rotorsCount, int  numberOfRotors, RotorNotValidException rotorNotValidException) {
        if(numberOfRotors < rotorsCount) {
            rotorNotValidException.setNumberOfRotorsToAdd(rotorsCount - numberOfRotors);
        }
    }

    private void checkIfRotorsCountIsValid(int rotorsCount, RotorNotValidException rotorNotValidException) {
        if(rotorsCount < 2 || rotorsCount > 99){
            rotorNotValidException.setRotorsCountOutOfRange();
        }
    }

    private void fillIdsMapByInsertedRotors(List<CTERotor> cteRotors, Map<Integer, Boolean> generatedRotorsIds) {
        for(CTERotor rotor: cteRotors){
            generatedRotorsIds.put(rotor.getId(),true);
        }
    }
    private void checkIfRotorsIdsAreValid(List<CTERotor> cteRotors, RotorNotValidException rotorNotValidException) {
        int numberOfRotors = cteRotors.size();
        Map<Integer,Boolean> rotorsIds = new HashMap<>();
        for(CTERotor rotor: cteRotors){
            if(rotor.getId() < 0 || rotor.getId() > numberOfRotors){
                rotorNotValidException.addRotorsToOutOfRangeList(rotor.getId());
            }
            if(rotorsIds.containsKey(rotor.getId())){
                rotorNotValidException.addDuplicatedRotorId(rotor.getId());
            }
            rotorsIds.put(rotor.getId(),true);
        }

    }

    private boolean AllRotorsIdsInSequence(Map<Integer,Boolean> generatedRotorsIds) {
        boolean allRotorsIdsInSequence = true;
        for(int i = 1; i <= generatedRotorsIds.size(); i++){
            if(!generatedRotorsIds.get(i)){
                allRotorsIdsInSequence = false;
                break;
            }
        }
        return allRotorsIdsInSequence;
    }

    private void checkIfPositionLettersInABC(CTEPositioning position, List<Character> cteABC, RotorNotValidException rotorNotValidException, int rotorId) {
        //TODO add length validation.
        if(!cteABC.contains(position.getLeft().toUpperCase().charAt(0)) || position.getLeft().length() != 1){
            rotorNotValidException.addNotValidLetter(position.getLeft().toUpperCase(),rotorId);
        }
        if(!cteABC.contains(position.getRight().toUpperCase().charAt(0)) || position.getRight().toUpperCase().length() != 1){
            rotorNotValidException.addNotValidLetter(position.getRight().toUpperCase(),rotorId);
        }

    }

    private Map<Character, Integer> getMachineKeyboardFromCTEKeyboard(List<Character> cteKeyboard,ABCNotValidException abcNotValidException) {
        Map<Character, Integer> machineKeyBoard = new HashMap<>();
        int indexToMappingTOInKeyboard = 0;

        if (cteKeyboard.size() % 2 != 0) {
            abcNotValidException.setIsOddLength();
        }
        for (Character letter : cteKeyboard) {
            if (machineKeyBoard.containsKey(Character.toUpperCase(letter))) {
                abcNotValidException.addCharToDuplicateChars(letter);
            }
            machineKeyBoard.put(Character.toUpperCase(letter), indexToMappingTOInKeyboard);
            indexToMappingTOInKeyboard++;
        }
        return machineKeyBoard;
    }
}
