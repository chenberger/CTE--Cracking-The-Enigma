package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Reflector;
import EnigmaMachine.RomanNumber;
import EnigmaMachine.Rotor;
import EnigmaMachineException.*;
import Jaxb.Schema.Generated.*;
import TDO.MachineDetails;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
    public void setSettingsAutomatically() throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, PluginBoardSettingsException, SettingsFormatException, CloneNotSupportedException, MachineNotExistsException, SettingsNotInitializedException {
        List<Sector> randomSectors;
        RandomSettingsGenerator randomSettingsGenerator = new RandomSettingsGenerator(enigmaMachine);

        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE);
        }

        randomSectors = randomSettingsGenerator.getRandomSectorSettings();
        validateMachineSettings(randomSectors);
        initializeSettings(randomSectors);
    }

    //endregion

    //region set Settings
    public void initializeSettings(List<Sector> settingsSector) throws MachineNotExistsException, RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException, SettingsFormatException, SettingsNotInitializedException {
        setMachineSettings(settingsSector);
        enigmaMachine.setTheInitialCodeDefined(true);
        resetSettings();
        setSettingsFormat(settingsSector);
        checkIfTheSettingsFormatInitialized();
    }

    private void setSettingsFormat(List<Sector> settingsSector) {
        enigmaMachine.clearSettings();
        settingsSector.forEach(sector -> sector.addSectorToSettingsFormat(enigmaMachine));

        if (enigmaMachine.isPluginBoardSet()) {
            enigmaMachine.getOriginalSettingsFormat().setIfPluginBoardSet(true);
        } else {
            enigmaMachine.getOriginalSettingsFormat().setIfPluginBoardSet(false);
        }
    }

    private void setMachineSettings(List<Sector> settingsSector) throws CloneNotSupportedException, MachineNotExistsException {
        clearSettings();
        settingsSector.forEach(sector -> {
            try {
                sector.setSectorInTheMachine(enigmaMachine);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void validateMachineSettings(List<Sector> sectorSettings) throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException {
        sectorSettings.forEach(sector -> {
            try {
                sector.validateSector(enigmaMachine);
            } catch (RotorsInUseSettingsException | ReflectorSettingsException | PluginBoardSettingsException |
                     StartingPositionsOfTheRotorException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void clearSettings() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE);
        }

        enigmaMachine.clearSettings();
    }
    public void checkIfTheSettingsFormatInitialized() throws SettingsFormatException, CloneNotSupportedException {
        if(!enigmaMachine.isTheInitialCodeDefined()) {
            throw new SettingsFormatException(enigmaMachine.getOriginalSettingsFormat());
        }

        statisticsAndHistoryAnalyzer.addSettingConfiguration((SettingsFormat) enigmaMachine.getOriginalSettingsFormat().clone());
    }
    //endregion

    @Override
    public void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException, SettingsNotInitializedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE);
        }
        if(!enigmaMachine.isTheInitialCodeDefined()) {
            throw new SettingsNotInitializedException(OperationType.SET_SETTINGS_MANUAL, OperationType.SET_SETTINGS_AUTOMATIC);
        }

        enigmaMachine.resetSettings();
    }

    @Override
    public MachineDetails displaySpecifications() throws MachineNotExistsException, CloneNotSupportedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE);
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
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE);
        }

        return statisticsAndHistoryAnalyzer.toString();
    }

    @Override
    public String processInput(String inputToProcess) throws MachineNotExistsException, IllegalArgumentException {
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

    public EnigmaMachine getCurrentEnigmaMachine() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE);
        }

        return enigmaMachine;
    }
}