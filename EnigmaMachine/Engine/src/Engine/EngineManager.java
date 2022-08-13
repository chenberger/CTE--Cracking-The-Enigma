package Engine;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import EnigmaMachine.*;
import EnigmaMachineException.*;
import Jaxb.Schema.Generated.*;
import TDO.MachineDetails;
import Jaxb.Schema.Generated.CTEEnigma;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import EnigmaMachine.RomanNumber;
import static java.lang.Integer.parseInt;
import static javafx.application.Platform.exit;


public class EngineManager implements MachineOperations, Serializable {

    //region private data members
    private EnigmaMachine enigmaMachine;
    private StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer;
    private final GeneralEnigmaMachineException enigmaMachineException;


    //endregion

    public EngineManager(){
        this.enigmaMachineException = new GeneralEnigmaMachineException();
        this.statisticsAndHistoryAnalyzer = new StatisticsAndHistoryAnalyzer();
        this.enigmaMachine = null;

        //region test
        Map<Character, Integer> ABC = new HashMap<Character, Integer>();
        ABC.put('A', 0);
        ABC.put('B', 1);
        ABC.put('C', 2);
        ABC.put('D', 3);
        ABC.put('E', 4);
        ABC.put('F', 5);

        ArrayList<Pair<Character, Character>> mappingABC1 = new ArrayList<>();
        List<Pair<Character, Character>> mappingABC2 = new ArrayList<>();
        mappingABC1.add(new Pair<Character, Character>('F', 'A'));
        mappingABC1.add(new Pair<Character, Character>('E', 'B'));
        mappingABC1.add(new Pair<Character, Character>('D', 'C'));
        mappingABC1.add(new Pair<Character, Character>('C', 'D'));
        mappingABC1.add(new Pair<Character, Character>('B', 'E'));
        mappingABC1.add(new Pair<Character, Character>('A', 'F'));

        mappingABC2.add(new Pair<Character, Character>('E', 'A'));
        mappingABC2.add(new Pair<Character, Character>('B', 'B'));
        mappingABC2.add(new Pair<Character, Character>('D', 'C'));
        mappingABC2.add(new Pair<Character, Character>('F', 'D'));
        mappingABC2.add(new Pair<Character, Character>('C', 'E'));
        mappingABC2.add(new Pair<Character, Character>('A', 'F'));

        Rotor rotor1 = new Rotor(1, 3,true, mappingABC1, 'C');
        Rotor rotor2 = new Rotor(2, 0, false, mappingABC2, 'C');

        Map<Integer, Rotor> rotors = new HashMap<Integer, Rotor>();
        rotors.put(rotor1.id(), rotor1);
        rotors.put(rotor2.id(), rotor2);

        Map<Integer, Integer> reflectorPairs = new HashMap<Integer, Integer>();
        reflectorPairs.put(0, 3);
        reflectorPairs.put(3, 0);
        reflectorPairs.put(4, 1);
        reflectorPairs.put(1, 4);
        reflectorPairs.put(2, 5);
        reflectorPairs.put(5, 2);
        Reflector reflector = new Reflector(RomanNumber.I, reflectorPairs);
        Map<RomanNumber, Reflector> reflectors = new HashMap<RomanNumber, Reflector>();
        reflectors.put(reflector.id(), reflector);

        enigmaMachine = new EnigmaMachine(rotors, reflectors, ABC, 2);

        //endregion

    }

    //region JAXB Translation
    public void setMachineDetailsFromXmlFile(String machineDetailsXmlFilePath) throws GeneralEnigmaMachineException, JAXBException, NotXmlFileException, FileNotFoundException {
        // TODO implement here also validation.(the file exist)
        try {
            InputStream inputStream = new FileInputStream(new File(machineDetailsXmlFilePath));
            if (!machineDetailsXmlFilePath.endsWith(".xml")) {
                throw new NotXmlFileException();
            }
            CTEEnigma enigma = deserializeFrom(inputStream);
            transformJAXBClassesToEnigmaMachine(enigma);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }catch (JAXBException e){
            throw new RuntimeException();
        }



   }

    public CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("Jaxb.Schema.Generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    private void transformJAXBClassesToEnigmaMachine(CTEEnigma JAXBGeneratedEnigma) throws GeneralEnigmaMachineException {
        // TODO implement here also validation.(the file exist),exceptions. also change the init settings to false.
        List<Character> generatedABC = new ArrayList<>();
        ABCNotValidException abcNotValidException = new ABCNotValidException();
        checkIfABCIsValid(JAXBGeneratedEnigma.getCTEMachine().getABC(), abcNotValidException, generatedABC);
        abcNotValidException.addExceptionsToTheList();

        if(abcNotValidException.shouldThrowException()){
            enigmaMachineException.addException(abcNotValidException);
            throw enigmaMachineException;
        }

        List<CTERotor> CTERotors = JAXBGeneratedEnigma.getCTEMachine().getCTERotors().getCTERotor();
        List<CTEReflector> CTEReflectors = JAXBGeneratedEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        Map<Integer,Rotor> machineRotors;
        Map<RomanNumber, Reflector> machineReflectors;
        Map<Character,Integer> machineKeyBoard;
        int rotorsInUseCounter = JAXBGeneratedEnigma.getCTEMachine().getRotorsCount();;

        machineKeyBoard = getMachineKeyboardFromCTEKeyboard(generatedABC, abcNotValidException);
        if(abcNotValidException.shouldThrowException()){
            enigmaMachineException.addException(abcNotValidException);
            throw enigmaMachineException;
        }

        machineRotors = getMachineRotorsFromCTERotors(CTERotors, generatedABC);
        machineReflectors = getMachineReflectorsFromCTEReflectors(CTEReflectors,generatedABC);

        if(!enigmaMachineException.isExceptionNeedToThrown()) {
            enigmaMachine = new EnigmaMachine(machineRotors, machineReflectors, machineKeyBoard, rotorsInUseCounter);
        }
        else {
            throw enigmaMachineException;
        }
    }

    private void checkIfABCIsValid(String abc,ABCNotValidException abcNotValidException,List<Character> generatedABC ) {
        if(abc.length() == 0) {
            abcNotValidException.setABCempty();
        }
        abc = abc.trim();
        checkIfKeyBoardContainsDuplications(abc,abcNotValidException,generatedABC);
    }

    private boolean checkIfKeyBoardContainsDuplications(String abc, ABCNotValidException abcNotValidException, List<Character> generatedABC) {
        Map<Character, Integer> abcMap = new HashMap<>();
        for(int i = 0; i < abc.length(); i++){
            if(abcMap.containsKey(abc.charAt(i))){
                return true;
            }
            else{
                abcMap.put(abc.charAt(i), 1);
            }
        }
        for(Character charInABC : abcMap.keySet()){
            if(abcMap.get(charInABC) > 1){
                abcNotValidException.addCharToDuplicateChars(charInABC);
            }
            else {
                generatedABC.add(charInABC);
            }
        }

        return false;
    }
    private Map<RomanNumber, Reflector> getMachineReflectorsFromCTEReflectors(List<CTEReflector> cteReflectors, List<Character> CTEAbc) {
        Map<RomanNumber, Reflector> machineReflectors = new HashMap<>();
        Map<String, Boolean> insertedReflectorsIds = fillReflectorMapIdsMapWithFalseValues(cteReflectors.size());

        ReflectorNotValidException reflectorNotValidException = new ReflectorNotValidException();
        reflectorNotValidException.setMaxPairsInAlphabet(CTEAbc.size()/2);
        if(cteReflectors.size() == 0) {
            reflectorNotValidException.setReflectorsEmpty();
        }
        else if(cteReflectors.size() > 5) {
            reflectorNotValidException.setToManyReflectors();
        }
        fillMapByInsertedReflectors(cteReflectors, insertedReflectorsIds);
        if(!allReflectorsIdsInSequence(insertedReflectorsIds,cteReflectors.size())){
                reflectorNotValidException.setMissingReflectorsIdsFromSequenceList(insertedReflectorsIds,cteReflectors.size());
        }
        for(CTEReflector reflector: cteReflectors){
             if(IsReflectorIdIsValid(reflector, reflectorNotValidException) && numberOfPairsInReflectorValid(reflector,CTEAbc,reflectorNotValidException)) {
             Map<Integer,Integer> currentReflectorMapping = new HashMap<>();
             Map<Integer,Integer> outPutColMap = new HashMap<>();
             Map<Integer,Integer> inputColMap = new HashMap<>();
             for(CTEReflect reflect:reflector.getCTEReflect()){
                 if(indexOutOfRange(reflect.getInput(), CTEAbc)){
                     reflectorNotValidException.addReflectorIndexOutOfRange(reflector.getId(),reflect.getInput());
                 }
                 else if(indexOutOfRange(reflect.getOutput(), CTEAbc)) {
                     reflectorNotValidException.addReflectorIndexOutOfRange(reflector.getId(), reflect.getOutput());
                 }
                 // TODO check if the Length is 1 and if the character is in ABC, and that there are no duplicates of chars in each side, check that the numbers in the reflector are in the length of, and that the index map to another one.
                 if(outPutColMap.containsKey(reflect.getOutput())){
                        reflectorNotValidException.addReflectorDuplicateOutput(reflector.getId(),reflect.getOutput());
                    }
                 if(inputColMap.containsKey(reflect.getInput())){
                        reflectorNotValidException.addReflectorDuplicateInput(reflector.getId(),reflect.getInput());
                    }
                outPutColMap.put(reflect.getOutput(),1);
                inputColMap.put(reflect.getInput(),1);

                 if(reflect.getInput() == reflect.getOutput()){
                     reflectorNotValidException.addIndexMappedToHimSelf(reflect.getInput(),reflector.getId());
                 }
                 currentReflectorMapping.put(reflect.getInput() - 1, reflect.getOutput() - 1);
                 currentReflectorMapping.put(reflect.getOutput() - 1,reflect.getInput() - 1);
             }
             insertedReflectorsIds.put(reflector.getId(),true);
             Reflector currentReflector = new Reflector(RomanNumber.valueOf(reflector.getId()), currentReflectorMapping);
             machineReflectors.put(RomanNumber.convertStringToRomanNumber(reflector.getId()),currentReflector);}
        }

        reflectorNotValidException.addExceptionsToTheList();
        if(reflectorNotValidException.shouldThrowException()){
            enigmaMachineException.addException(reflectorNotValidException);
        }
        return machineReflectors;
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

    private Map<Integer, Rotor> getMachineRotorsFromCTERotors(List<CTERotor> cteRotors, List<Character> cteABC) {
        Map<Integer,Rotor> machineRotors = new HashMap<Integer, Rotor>();
        Map<Integer,Boolean> generatedRotorsIds = fillRotorsMapWithFalseValues(cteRotors.size());
        RotorNotValidException rotorNotValidException = new RotorNotValidException();
        rotorNotValidException.setMaxAlphabetLength(cteABC.size());
        rotorNotValidException.setMaxRotorId(cteRotors.size());
        fillIdsMapByInsertedRotors(cteRotors, generatedRotorsIds);
        checkIfRotorsIdsAreValid(cteRotors, rotorNotValidException);
        if(cteRotors.size() < 2) {
            rotorNotValidException.setNumberOfRotorsToAdd(2 - cteRotors.size());
        }
        if(!AllRotorsIdsInSequence(generatedRotorsIds)) {
            rotorNotValidException.setRotorsIdsNotInSequenceList(generatedRotorsIds);
        }
        //TODO check if rotors id are numbers, left, right from abc. length is as the length of abc and each shows once, that the notch is in the length of the abc.
        //TODO check that the rotors count which the number of rotors in use is between 2 and 99, return the rotors count to erez.
        for(CTERotor rotor: cteRotors){
            Map<Character,Character> currentRotorMap = new HashMap<>();
            Map<String,Integer> leftColInRotor = new HashMap<>();
            Map<String,Integer> rightColInRotor = new HashMap<>();
            if(rotor.getNotch() > cteABC.size() || rotor.getNotch() < 0){
                rotorNotValidException.addNotchOutOfRange(rotor.getId(),rotor.getNotch());
            }
            if(rotor.getCTEPositioning().size() != cteABC.size()){
                rotorNotValidException.setNumberOfPairsInRotorInvalid(rotor,cteABC);
            }

            List<Pair<Character,Character>> currentRotorPairs = new ArrayList<>();
            for(CTEPositioning position: rotor.getCTEPositioning()){

                checkIfPositionLettersInABC(position, cteABC, rotorNotValidException, rotor.getId());

                if(leftColInRotor.containsKey(position.getLeft())){
                    rotorNotValidException.addDuplicatedCharToLeftCol(rotor.getId(),position.getLeft());
                }
                if(rightColInRotor.containsKey(position.getRight())){
                    rotorNotValidException.addDUDuplicatedCharToRightCol(rotor.getId(),position.getRight());
                }
                leftColInRotor.put(position.getLeft(),1);
                rightColInRotor.put(position.getRight(),1);

                Pair<Character,Character> currentPair = new Pair<>(position.getLeft().charAt(0),position.getRight().charAt(0));
                // TODO check if the Length is 1 and if the character is in ABC, and that there are no duplicates of chars in each side where ever there are numbers, check that they are ints.
                currentRotorPairs.add(currentPair);
                currentRotorMap.put(position.getLeft().charAt(0),position.getRight().charAt(0));
            }

            generatedRotorsIds.put(rotor.getId() ,true);
            Rotor currentRotor = new Rotor(rotor.getId(), rotor.getNotch() - 1, currentRotorPairs);
            machineRotors.put(rotor.getId(),currentRotor);
        }
        rotorNotValidException.addExceptionsToTheList();
        if(rotorNotValidException.shouldThrowException()){
            enigmaMachineException.addException(rotorNotValidException);
        }
        return machineRotors;
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

    //endregion

    //region Operations implements
    //region set automatic settings
    @Override
    public void setSettingsAutomatically() throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, PluginBoardSettingsException, SettingsFormatException, CloneNotSupportedException, MachineNotExistsException {
        RotorIDSector rotorIDSector = getRandomRotorsIdSector();
        StartingRotorPositionSector startingRotorPositionSector = getRandomStartingPositionRotorsSector(rotorIDSector.getElements().size());
        ReflectorIdSector reflectorIdSector = getRandomReflectorSector();
        PluginBoardSector pluginBoardSector = getRandomPluginBoardSector();

        validateMachineSettings(rotorIDSector, startingRotorPositionSector, reflectorIdSector, pluginBoardSector);
        initializeSettings(rotorIDSector, startingRotorPositionSector, reflectorIdSector, pluginBoardSector);
    }

    private PluginBoardSector getRandomPluginBoardSector() {
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

    private ReflectorIdSector getRandomReflectorSector() {
        Random randomGenerator = new Random();
        RomanNumber[] reflectorIdArr = enigmaMachine.getAllReflectors().keySet().toArray(new RomanNumber[enigmaMachine.getAllReflectors().keySet().size()]);
        List<RomanNumber> reflectorId = new ArrayList<RomanNumber>();

        reflectorId.add(reflectorIdArr[randomGenerator.nextInt(reflectorIdArr.length)]);

        return new ReflectorIdSector(reflectorId);
    }

    private StartingRotorPositionSector getRandomStartingPositionRotorsSector(int rotorsInUseSize) {
        List<Character> startingPositionsOfTheRotors = new ArrayList<>();

        for (int i = 0; i < rotorsInUseSize; i++) {
            startingPositionsOfTheRotors.add(getRandomCharacterFromTheKeyboard(enigmaMachine.getKeyboard().stream().collect(Collectors.toList())));
        }
        return new StartingRotorPositionSector(startingPositionsOfTheRotors);
    }

    private Character getRandomCharacterFromTheKeyboard(List<Character> optionalCharacters) {
        Random randomGenerator = new Random();

        return optionalCharacters.get(randomGenerator.nextInt(optionalCharacters.size()));
    }

    private RotorIDSector getRandomRotorsIdSector() {
        List<Integer> rotorsId = new ArrayList<>();
        Set<Integer> optionalRotorsId = new HashSet<>(enigmaMachine.getAllRotors().keySet());
        Integer randomId;

        for (int i = 0; i < enigmaMachine.getNumOfActiveRotors(); i++) {
            randomId =  getRandomRotorId(optionalRotorsId.stream().collect(Collectors.toList()));
            rotorsId.add(randomId);
            optionalRotorsId.remove(randomId);
        }

        return new RotorIDSector(rotorsId);
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

    //endregion

    //region set Settings
    public void initializeSettings(RotorIDSector rotorIDSector, StartingRotorPositionSector startingPositionRotorsSector, ReflectorIdSector reflectorSector, PluginBoardSector pluginBoardSector) throws MachineNotExistsException, RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException, SettingsFormatException {
        setMachineSettings(rotorIDSector, startingPositionRotorsSector, reflectorSector, pluginBoardSector);
        enigmaMachine.setTheInitialCodeDefined(true);
        resetSettings();
        setSettingsFormat(rotorIDSector, startingPositionRotorsSector, reflectorSector, pluginBoardSector);
        checkIfTheSettingsFormatInitialized();
    }

    private void setSettingsFormat(RotorIDSector rotorIDSector, StartingRotorPositionSector startingPositionRotorsSector, ReflectorIdSector reflectorSector, PluginBoardSector pluginBoardSector) {
        enigmaMachine.clearSettings();
        rotorIDSector.setCurrentNotchPositions(enigmaMachine.getCurrentRotorsInUse().stream().map(rotor -> rotor.getStartingNotchPosition()).collect(Collectors.toList()));
        enigmaMachine.getOriginalSettingsFormat().addSector(rotorIDSector);
        enigmaMachine.getOriginalSettingsFormat().addSector(startingPositionRotorsSector);
        enigmaMachine.getOriginalSettingsFormat().addSector(reflectorSector);
        enigmaMachine.getOriginalSettingsFormat().addSector(pluginBoardSector);

        if (pluginBoardSector.getElements().size() > 0) {
            enigmaMachine.getOriginalSettingsFormat().isPluginBoardSet(true);
        } else {
            enigmaMachine.getOriginalSettingsFormat().isPluginBoardSet(false);
        }
    }

    private void setMachineSettings(RotorIDSector rotorIDSector, StartingRotorPositionSector startingPositionRotorsSector, ReflectorIdSector reflectorSector, PluginBoardSector pluginBoardSector) throws CloneNotSupportedException, MachineNotExistsException {
        clearSettings();
        enigmaMachine.setRotorsInUseSettings(rotorIDSector);
        enigmaMachine.setStartingPositionRotorsSettings(startingPositionRotorsSector, rotorIDSector);
        enigmaMachine.setReflectorInUseSettings(reflectorSector);
        enigmaMachine.setPluginBoardSettings(pluginBoardSector);
    }

    private void validateMachineSettings(RotorIDSector rotorIDSector, StartingRotorPositionSector startingPositionRotorsSector, ReflectorIdSector reflectorSector, PluginBoardSector pluginBoardSector) throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException {
        enigmaMachine.validateRotorsInUseSettings(rotorIDSector);
        enigmaMachine.validateStartingPositionRotorsSettings(startingPositionRotorsSector, rotorIDSector);
        enigmaMachine.validateReflectorInUseSettings(reflectorSector);
        enigmaMachine.validatePluginBoardSettings(pluginBoardSector);
    }

    public void clearSettings() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation");
        }

        enigmaMachine.clearSettings();
    }
    public void checkIfTheSettingsFormatInitialized() throws SettingsFormatException, CloneNotSupportedException {
        if(!enigmaMachine.isTheInitialCodeDefined()) {
            throw new SettingsFormatException(enigmaMachine.getOriginalSettingsFormat());
        }

        statisticsAndHistoryAnalyzer.addSettingConfiguration((SettingsFormat) enigmaMachine.getOriginalSettingsFormat().clone());
    }
    public void validateRotorsInUse(RotorIDSector rotorIDSector) throws RotorsInUseSettingsException {
        enigmaMachine.validateRotorsInUseSettings(rotorIDSector);
    }

    public void validateStartingPositionRotors(StartingRotorPositionSector startingPositionTheRotors, RotorIDSector rotorIDSector) throws StartingPositionsOfTheRotorException {
        enigmaMachine.validateStartingPositionRotorsSettings(startingPositionTheRotors, rotorIDSector);
    }

    public void validateReflectorInUse(ReflectorIdSector reflectorInUse) throws ReflectorSettingsException, CloneNotSupportedException {
        enigmaMachine.validateReflectorInUseSettings(reflectorInUse);
    }

    public void validatePluginBoard(PluginBoardSector pluginBoardSector) throws PluginBoardSettingsException {
        enigmaMachine.validatePluginBoardSettings(pluginBoardSector);
    }
    //endregion

    @Override
    public void resetSettings() throws MachineNotExistsException, IllegalArgumentException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation again");
        }
        if(!enigmaMachine.isTheInitialCodeDefined()) {
            throw new IllegalArgumentException("Error: The initial code configuration has not been configured for the machine, you must return to operation 3 or 4 and then return to this operation");
        }

        enigmaMachine.resetSettings();
    }

    @Override
    public MachineDetails displaySpecifications() throws MachineNotExistsException, CloneNotSupportedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation again");
       }

       return new MachineDetails(enigmaMachine.getAllRotors(),
                                           enigmaMachine.getCurrentRotorsInUse(),
                                           enigmaMachine.getAllReflectors(),
                                           enigmaMachine.getCurrentReflectorInUse(),
                                           enigmaMachine.getKeyboard(),
                                           enigmaMachine.getPluginBoard(),
                                           statisticsAndHistoryAnalyzer.getMessagesCounter(),
                                           enigmaMachine.getOriginalSettingsFormat(),
                                           enigmaMachine.getCurrentSettingsFormat());
    }

    @Override
    public String analyzeHistoryAndStatistics() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation again");
        }

        return statisticsAndHistoryAnalyzer.toString();
    }

    @Override
    public String processInput(String inputToProcess) throws MachineNotExistsException, IllegalArgumentException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation");
        }

        OriginalStringFormat originalStringFormat = new OriginalStringFormat(inputToProcess.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        Instant start = Instant.now();
        String encryptedString = getProcessedInput(inputToProcess);
        Instant end = Instant.now();
        long durationEncryptedTimeInNanoSeconds = Duration.between(start, end).toNanos();
        EncryptedStringFormat encryptedStringFormat = new EncryptedStringFormat(encryptedString.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        ProcessedStringsFormat processedStringsFormat = new ProcessedStringsFormat(new ArrayList<>(Arrays.asList(originalStringFormat, encryptedStringFormat)),
                durationEncryptedTimeInNanoSeconds, enigmaMachine.getOriginalSettingsFormat().getIndexFormat());
        enigmaMachine.getOriginalSettingsFormat().advanceIndexFormat();
        statisticsAndHistoryAnalyzer.addProcessedStringFormat(enigmaMachine.getOriginalSettingsFormat(), processedStringsFormat);
        statisticsAndHistoryAnalyzer.advancedMessagesCounter();

        return encryptedString;
    }

    private String getProcessedInput(String inputToProcess) throws IllegalArgumentException{
        //TODO chen: throw exception with more info: what are the illegal char and send the legal keyboard chars
        if(containsCharNotInMAMachineKeyboard(inputToProcess)){
            throw new IllegalArgumentException("The input contains char/s that are not in the machine keyboard");
        }
        String processedInput = "";
        for(char letter: inputToProcess.toCharArray()){
            processedInput += enigmaMachine.decode(letter);
        }
        return processedInput;
    }

    private boolean containsCharNotInMAMachineKeyboard(String inputToProcess) {
        for(char letter: inputToProcess.toCharArray()){
            if(!enigmaMachine.getKeyboard().contains(letter)){
                return true;
            }
        }
        return false;
    }
    public void finishSession() {
        exit();
    }
    //endregion

    public boolean isMachineExists() {
        return enigmaMachine != null;
    }

    public boolean isMachineSettingInitialized() {
        return enigmaMachine.isTheInitialCodeDefined();
    }

    public int getAmountOfActiveRotors() {
        return enigmaMachine.getNumOfActiveRotors();
    }
}