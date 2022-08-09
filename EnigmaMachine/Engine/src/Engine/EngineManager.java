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
    public void setMachineDetails(String machineDetailsXmlFilePath) {
        // TODO implement here also validation.(the file exist)
        try {
            InputStream inputStream = new FileInputStream(new File(machineDetailsXmlFilePath));
            if(!machineDetailsXmlFilePath.endsWith(".xml")){
                throw new NotXmlFileException();
            }
            CTEEnigma enigma = deserializeFrom(inputStream);
            transformJAXBClassesToEnigmaMachine(enigma);
        }
        catch (JAXBException | FileNotFoundException | NotXmlFileException | GeneralEnigmaMachineException e) { //should catch the exception to the xml in the UI.
            e.printStackTrace();
        }
   }

    public CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("Jaxb.Schema.Generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    private void transformJAXBClassesToEnigmaMachine(CTEEnigma JAXBGeneratedEnigma) throws GeneralEnigmaMachineException {
        // TODO implement here also validation.(the file exist),exceptions. also change the init settings to false.
        ABCNotValidException abcNotValidException = new ABCNotValidException();
        checkIfABCIsValid(JAXBGeneratedEnigma.getCTEMachine().getABC(), abcNotValidException);
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

        machineKeyBoard = getMachineKeyboardFromCTEKeyboard(JAXBGeneratedEnigma.getCTEMachine().getABC().toCharArray(), abcNotValidException);
        if(abcNotValidException.shouldThrowException()){
            enigmaMachineException.addException(abcNotValidException);
            throw enigmaMachineException;
        }

        machineRotors = getMachineRotorsFromCTERotors(CTERotors, JAXBGeneratedEnigma.getCTEMachine().getABC().toCharArray());
        machineReflectors = getMachineReflectorsFromCTEReflectors(CTEReflectors,JAXBGeneratedEnigma.getCTEMachine().getABC().toCharArray());

        if(!enigmaMachineException.isExceptionNeedToThrown()) {
            enigmaMachine = new EnigmaMachine(machineRotors, machineReflectors, machineKeyBoard, rotorsInUseCounter);
        }
        else {
            throw enigmaMachineException;
        }
    }

    private void checkIfABCIsValid(String abc,ABCNotValidException abcNotValidException ) {
        if(abc.length() == 0) {
            abcNotValidException.setABCempty();
        }
        checkIfKeyBoardContainsDuplications(abc,abcNotValidException);
    }

    private boolean checkIfKeyBoardContainsDuplications(String abc,ABCNotValidException abcNotValidException) {
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
        }

        return false;
    }
    private Map<RomanNumber, Reflector> getMachineReflectorsFromCTEReflectors(List<CTEReflector> cteReflectors, char[] CTEAbc) {
        Map<RomanNumber, Reflector> machineReflectors = new HashMap<>();

        ReflectorNotValidException reflectorNotValidException = new ReflectorNotValidException();
        if(cteReflectors.size() == 0) {
            reflectorNotValidException.setReflectorsEmpty();
        }
        else if(cteReflectors.size() > 5) {
            reflectorNotValidException.setToManyReflectors();
        }
        for(CTEReflector reflector: cteReflectors){
             if(IsReflectorIdIsValid(reflector, reflectorNotValidException) && numberOfPairsInReflectorValid(reflector,CTEAbc,reflectorNotValidException)) {
             Map<Integer,Integer> currentReflectorMapping = new HashMap<>();
             for(CTEReflect reflect:reflector.getCTEReflect()){
                 Map<Integer,Integer> outPutColMap = new HashMap<>();
                 Map<Integer,Integer> inputColMap = new HashMap<>();
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
                 currentReflectorMapping.put(reflect.getInput(),reflect.getOutput());
                 currentReflectorMapping.put(reflect.getOutput(),reflect.getInput());
             }
             Reflector currentReflector = new Reflector(RomanNumber.valueOf(reflector.getId()), currentReflectorMapping);
             machineReflectors.put(RomanNumber.convertStringToRomanNumber(reflector.getId()),currentReflector);}
        }
        reflectorNotValidException.addExceptionsToTheList();
        if(reflectorNotValidException.shouldThrowException()){
            enigmaMachineException.addException(reflectorNotValidException);
        }
        return machineReflectors;
    }

    private boolean indexOutOfRange(int input, char[] cteAbc) {
        return input > cteAbc.length / 2;
    }

    private boolean numberOfPairsInReflectorValid(CTEReflector reflector, char[] CTEAbc,ReflectorNotValidException reflectorNotValidException) {
        if(reflector.getCTEReflect().size() != CTEAbc.length/2) {
            reflectorNotValidException.setNumberOfPairsInReflectorInvalid(reflector,CTEAbc);
            return false;
        }
        return true;
    }

    private boolean IsReflectorIdIsValid(CTEReflector reflector,ReflectorNotValidException reflectorNotValidException) {
        if(parseInt(reflector.getId()) > 4 && parseInt(reflector.getId())< 0){
            reflectorNotValidException.addReflectorsToOutOfRangeList(reflector.getId());
            return false;
        }
        return true;
    }

    private Map<Integer, Rotor> getMachineRotorsFromCTERotors(List<CTERotor> cteRotors, char[] cteABC) {
        Map<Integer,Rotor> machineRotors = new HashMap<Integer, Rotor>();
        RotorNotValidException rotorNotValidException = new RotorNotValidException();
        checkIfRotorsIdsAreValid(cteRotors, rotorNotValidException);
        if(cteRotors.size() < 2) {
            rotorNotValidException.setNumberOfRotorsToAdd(2 - cteRotors.size());
        }

        checkThatRotorsIdsAreValid(cteRotors, rotorNotValidException);
        //TODO check if rotors id are numbers, left, right from abc. length is as the length of abc and each shows once, that the notch is in the length of the abc.
        //TODO check that the rotors count which the number of rotors in use is between 2 and 99, return the rotors count to erez.
        for(CTERotor rotor: cteRotors){
            Map<Character,Character> currentRotorMap = new HashMap<>();
            if(rotor.getNotch() > cteABC.length || rotor.getNotch() < 0){
                rotorNotValidException.addNotchOutOfRange(rotor.getId(),rotor.getNotch());
            }
            if(rotor.getCTEPositioning().size() != cteABC.length){
                rotorNotValidException.setNumberOfPairsInRotorInvalid(rotor,cteABC);
            }

            List<Pair<Character,Character>> currentRotorPairs = new ArrayList<>();
            for(CTEPositioning position: rotor.getCTEPositioning()){

                checkIfPositionLettersInABC(position, cteABC, rotorNotValidException, rotor.getId());
                Map<Character,Integer> leftColInRotor = new HashMap<>();
                Map<Character,Integer> rightColInRotor = new HashMap<>();
                if(leftColInRotor.containsKey(position.getLeft())){
                    rotorNotValidException.addDUplicatedCharToLeftCol(rotor.getId(),position.getLeft());
                }
                if(rightColInRotor.containsKey(position.getRight())){
                    rotorNotValidException.addDUplicatedCharToRightCol(rotor.getId(),position.getRight());
                }
                leftColInRotor.put(position.getLeft().charAt(0),1);
                rightColInRotor.put(position.getRight().charAt(0),1);

                Pair<Character,Character> currentPair = new Pair<>(position.getLeft().charAt(0),position.getRight().charAt(0));
                // TODO check if the Length is 1 and if the character is in ABC, and that there are no duplicates of chars in each side where ever there are numbers, check that they are ints.
                currentRotorPairs.add(currentPair);
                currentRotorMap.put(position.getLeft().charAt(0),position.getRight().charAt(0));
            }
            rotorNotValidException.addExceptionsToTheList();
            if(rotorNotValidException.shouldThrowException()){
                enigmaMachineException.addException(rotorNotValidException);
            }
            Rotor currentRotor = new Rotor(rotor.getId(), rotor.getNotch() - 1, currentRotorPairs);
            machineRotors.put(rotor.getId(),currentRotor);
        }
        return machineRotors;
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

    private void checkThatRotorsIdsAreValid(List<CTERotor> cteRotors, RotorNotValidException rotorNotValidException) {
    }

    private void checkIfPositionLettersInABC(CTEPositioning position, char[] cteABC, RotorNotValidException rotorNotValidException, int rotorId) {
        //TODO add length validation.
        for(Character charInAbc: cteABC){
            if(position.getLeft().length() != 1){
                rotorNotValidException.addNotValidLetter(position.getLeft(),rotorId);
            }
            if(position.getLeft().charAt(0) == charInAbc){
                break;
            }
        }
        if(position.getLeft().length() == 1){
            rotorNotValidException.addNotValidLetter(position.getLeft(),rotorId);
        }

        for(Character charInAbc: cteABC){
            if(position.getRight().length() != 1){
                rotorNotValidException.addNotValidLetter(position.getRight(),rotorId);
                break;
            }
            if(position.getRight().charAt(0) == charInAbc){
                break;
            }
        }
        if(position.getRight().length() == 1){
            rotorNotValidException.addNotValidLetter(position.getRight(),rotorId);
        }

    }

    private Map<Character, Integer> getMachineKeyboardFromCTEKeyboard(char[] cteKeyboard,ABCNotValidException abcNotValidException) {
        Map<Character, Integer> machineKeyBoard = new HashMap<>();
        int indexToMappingTOInKeyboard = 0;

        if (cteKeyboard.length % 2 != 0) {
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
        setSettings(rotorIDSector, getRandomStartingPositionRotorsSector(rotorIDSector.getElements().size()), getRandomReflectorSector(), getRandomPluginBoardSector());
    }

    private void setSettings(RotorIDSector rotorIDSector, StartingRotorPositionSector startingPositionRotorsSector, ReflectorIdSector reflectorSector, PluginBoardSector pluginBoardSector) throws MachineNotExistsException, RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException, SettingsFormatException {
        clearSettings();
        setRotorsInUse(rotorIDSector);
        setStartingPositionRotors(startingPositionRotorsSector, rotorIDSector);
        setReflectorInUse(reflectorSector);
        setPluginBoard(pluginBoardSector);
        checkIfTheSettingsInitialized();
        resetSettings();
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
        Random randomGenerator = new Random();
        int amountOfRotors = randomGenerator.nextInt(enigmaMachine.getAllRotors().size()) + 1;
        Integer randomId;

        for (int i = 0; i < amountOfRotors; i++) {
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
    public void clearSettings() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation");
        }

        enigmaMachine.clearSettings();
    }
    public void checkIfTheSettingsInitialized() throws SettingsFormatException, CloneNotSupportedException {
        if(!enigmaMachine.isMachineSettingInitialized()) {
            throw new SettingsFormatException(enigmaMachine.getSettingsFormat());
        }

        statisticsAndHistoryAnalyzer.addSettingConfiguration((SettingsFormat) enigmaMachine.getSettingsFormat().clone());
    }
    public void setRotorsInUse(RotorIDSector rotorIDSector) throws RotorsInUseSettingsException {
        enigmaMachine.initializeRotorsInUseSettings(rotorIDSector);
    }

    public void setStartingPositionRotors(StartingRotorPositionSector startingPositionTheRotors, RotorIDSector rotorIDSector) throws StartingPositionsOfTheRotorException {
        enigmaMachine.setStartingPositionRotorsSettings(startingPositionTheRotors, rotorIDSector);
    }

    public void setReflectorInUse(ReflectorIdSector reflectorInUse) throws ReflectorSettingsException, CloneNotSupportedException {
        enigmaMachine.setReflectorInUseSettings(reflectorInUse);
    }

    public void setPluginBoard(PluginBoardSector pluginBoardSector) throws PluginBoardSettingsException {
        enigmaMachine.setPluginBoardSettings(pluginBoardSector);
    }
    //endregion

    @Override
    public void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation");
        }
        if(!enigmaMachine.isMachineSettingInitialized()) {
            throw new IllegalArgumentException("Error: The initial code configuration has not been configured for the machine, you must return to operation 3 or 4 and then return to this operation");
        }

        enigmaMachine.resetSettings();
    }

    @Override
    public MachineDetails displaySpecifications() throws Exception {
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
                                           enigmaMachine.getSettingsFormat());
    }

    @Override
    public String analyzeHistoryAndStatistics() throws Exception {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation again");
        }

        return statisticsAndHistoryAnalyzer.toString();
    }

    @Override
    public String processInput(String inputToProcess) throws Exception {
        if(!isMachineExists()) {
            throw new MachineNotExistsException("Go back to operation 1 and then run this operation");
        }
        if(!enigmaMachine.isMachineSettingInitialized()) {
            throw new IllegalArgumentException("Error: The initial code configuration has not been configured for the machine, you must return to operation 3 or 4 and then return to this operation");
        }

        OriginalStringFormat originalStringFormat = new OriginalStringFormat(inputToProcess.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        Instant start = Instant.now();
        String encryptedString = getProcessedInput(inputToProcess);
        Instant end = Instant.now();
        long durationEncryptedTimeInNanoSeconds = Duration.between(start, end).toNanos();
        EncryptedStringFormat encryptedStringFormat = new EncryptedStringFormat(encryptedString.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        ProcessedStringsFormat processedStringsFormat = new ProcessedStringsFormat(new ArrayList<>(Arrays.asList(originalStringFormat, encryptedStringFormat)),
                durationEncryptedTimeInNanoSeconds, enigmaMachine.getSettingsFormat().getIndexFormat());
        enigmaMachine.getSettingsFormat().advanceIndexFormat();
        statisticsAndHistoryAnalyzer.addProcessedStringFormat(enigmaMachine.getSettingsFormat(), processedStringsFormat);
        statisticsAndHistoryAnalyzer.advancedMessagesCounter();

        return encryptedString;
    }

    private String getProcessedInput(String inputToProcess) {
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

}
