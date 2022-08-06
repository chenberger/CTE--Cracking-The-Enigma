package Operations;

import java.io.*;
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


public class Engine implements MachineOperations, Serializable {

    //region private data members
    private SettingsFormat settingsFormat;
    private EnigmaMachine enigmaMachine;
    private MachineDetails machineDetails;
    private final GeneralEnigmaMachineException enigmaMachineException;

    //endregion

    public Engine(){
        this.settingsFormat = new SettingsFormat();
        this.enigmaMachineException = new GeneralEnigmaMachineException();

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

        enigmaMachine = new EnigmaMachine(rotors, reflectors, ABC);

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
        // TODO implement here also validation.(the file exist),exceptions.
        List<CTERotor> CTERotors = JAXBGeneratedEnigma.getCTEMachine().getCTERotors().getCTERotor();
        List<CTEReflector> CTEReflectors = JAXBGeneratedEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        Map<Integer,Rotor> machineRotors;
        Map<RomanNumber, Reflector> machineReflectors;
        Map<Character,Integer> machineKeyBoard;

        machineKeyBoard = getMachineKeyboardFromCTEKeyboard(JAXBGeneratedEnigma.getCTEMachine().getABC().toCharArray());
        machineRotors = getMachineRotorsFromCTERotors(CTERotors);
        machineReflectors = getMachineReflectorsFromCTEReflectors(CTEReflectors);

        if(enigmaMachineException.noExceptionRaised()) {
            enigmaMachine = new EnigmaMachine(machineRotors, machineReflectors, machineKeyBoard);
        }
        else {
            throw enigmaMachineException;
        }

    }

    private Map<RomanNumber , Reflector> getMachineReflectorsFromCTEReflectors(List<CTEReflector> cteReflectors) {
        Map<RomanNumber, Reflector> machineReflectors = new HashMap<>();
        for(CTEReflector reflector: cteReflectors){
            if(parseInt(reflector.getId()) > 4 && parseInt(reflector.getId())< 0){
                enigmaMachineException.setReflectorNotFound();
            }
            Map<Integer,Integer> currentReflectorMapping = new HashMap<>();
            for(CTEReflect reflect:reflector.getCTEReflect()){
                // TODO check if the Length is 1 and if the character is in ABC, and that there are no duplicates of chars in each side
                if(currentReflectorMapping.containsKey(reflect.getInput())){
                    enigmaMachineException.addValuesWithSameMappingInOneReflector(reflect.getInput(), reflect.getOutput(), currentReflectorMapping.get(reflect.getInput()));
                }
                if(currentReflectorMapping.containsValue(reflect.getOutput())){
                    enigmaMachineException.addValuesWithSameMappingInOneReflector(reflect.getInput(),reflect.getOutput(),currentReflectorMapping.get(reflect.getOutput()));
                }
                currentReflectorMapping.put(reflect.getInput(),reflect.getOutput());
                currentReflectorMapping.put(reflect.getOutput(),reflect.getInput());
            }
            Reflector currentReflector = new Reflector(RomanNumber.valueOf(reflector.getId()), currentReflectorMapping);
            machineReflectors.put(RomanNumber.convertStringToRomanNumber(reflector.getId()),currentReflector);
        }

        return machineReflectors;
    }
    private Map<Integer,Rotor> getMachineRotorsFromCTERotors(List<CTERotor> cteRotors) {
        Map<Integer,Rotor> machineRotors = new HashMap<Integer, Rotor>();
        for(CTERotor rotor: cteRotors){
            Map<Character,Character> currentRotorMap = new HashMap<>();
            List<Pair<Character,Character>> currentRotorPairs = new ArrayList<>();
            for(CTEPositioning position: rotor.getCTEPositioning()){
                if(currentRotorMap.containsKey(position.getLeft().charAt(0))){
                    enigmaMachineException.addValuesWithSameMappingInOneRotor(position.getLeft().charAt(0),position.getRight().charAt(0), currentRotorMap.get(position.getLeft().charAt(0)));
                }
                Pair<Character,Character> currentPair = new Pair<>(position.getLeft().charAt(0),position.getRight().charAt(0));
                // TODO check if the Length is 1 and if the character is in ABC, and that there are no duplicates of chars in each side.
                currentRotorPairs.add(currentPair);
                currentRotorMap.put(position.getLeft().charAt(0),position.getRight().charAt(0));
            }

            Rotor currentRotor = new Rotor(rotor.getId(), rotor.getNotch() - 1, currentRotorPairs);
            machineRotors.put(rotor.getId(),currentRotor);
        }
        return machineRotors;
    }

    private Map<Character, Integer> getMachineKeyboardFromCTEKeyboard(char[] cteKeyboard) {
        Map<Character, Integer> machineKeyBoard = new HashMap<>();
        int indexToMappingTOInKeyboard = 0;

        if (cteKeyboard.length % 2 != 0) {
            enigmaMachineException.setIsOddLength();
        }
        for (Character letter : cteKeyboard) {
            if (machineKeyBoard.containsKey(Character.toUpperCase(letter))) {
                enigmaMachineException.addCharToDuplicateChars(letter);
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
    public void setSettingsAutomatically() throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, PluginBoardSettingsException, SettingsFormatException {
        setSettings();
        RotorIDSector rotorIDSector = getRandomRotorsIdSector();

        setRotorsInUse(rotorIDSector);
        setStartingPositionRotors(getRandomStartingPositionRotorsSector(rotorIDSector.getElements().size()), rotorIDSector);
        setReflectorInUse(getRandomReflectorSector());
        setPluginBoard(getRandomPluginBoardSector());
        checkIfTheSettingsInitialized();
    }

    private PluginBoardSector getRandomPluginBoardSector() {
        List<Pair<Character, Character>> pluginPairs = new ArrayList<>();
        Random randomGenerator = new Random();
        int amountOfPairs = randomGenerator.nextInt(enigmaMachine.getMaximumPairs() + 1);

        for (int i = 0; i < amountOfPairs; i++) {
            pluginPairs.add(getRandomPluginPair(pluginPairs));
        }

        return new PluginBoardSector(pluginPairs);
    }

    private Pair<Character, Character> getRandomPluginPair(List<Pair<Character, Character>> pluginPairs) {
        Pair<Character, Character> randomPluginPair = new Pair<>(
                getRandomCharacterFromTheKeyboard(), getRandomCharacterFromTheKeyboard());

        while(!isValidPair(randomPluginPair, pluginPairs))
        {
            randomPluginPair = new Pair<>(
                    getRandomCharacterFromTheKeyboard(), getRandomCharacterFromTheKeyboard());
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
            startingPositionsOfTheRotors.add(getRandomCharacterFromTheKeyboard());
        }

        return new StartingRotorPositionSector(startingPositionsOfTheRotors);
    }

    private Character getRandomCharacterFromTheKeyboard() {
        Random randomGenerator = new Random();
        Character[] keyboardArr = enigmaMachine.getKeyboard().toArray(new Character[enigmaMachine.getKeyboard().size()]);

        return keyboardArr[randomGenerator.nextInt(keyboardArr.length)];
    }

    private RotorIDSector getRandomRotorsIdSector() {
        List<Integer> rotorsId = new ArrayList<>();
        Random randomGenerator = new Random();
        int amountOfRotors = randomGenerator.nextInt(enigmaMachine.getAllrotors().size()) + 1;

        for (int i = 0; i < amountOfRotors; i++) {
              rotorsId.add(getRandomRotorId(rotorsId));
        }

        return new RotorIDSector(rotorsId);
    }

    private Integer getRandomRotorId(List<Integer> rotorsId) {
        Random randomGenerator = new Random();
        int randomRotorId = enigmaMachine.getAllrotors().get(randomGenerator.nextInt(enigmaMachine.getAllrotors().size())).id();

        while(rotorsId.contains(randomRotorId))
        {
            randomRotorId = enigmaMachine.getAllrotors().get(randomGenerator.nextInt(enigmaMachine.getAllrotors().size())).id();
        }

        return randomRotorId;
    }

    //endregion

    public void setSettings() {
        if(!isMachineExists()) {
            throw new IllegalArgumentException("Error: There machine is no exists, go back to operation 1 and then run this operation");
        }

        settingsFormat.clear();
    }
    public void checkIfTheSettingsInitialized() throws SettingsFormatException {
        if(!settingsFormat.isSettingsInitialized()) {
            throw new SettingsFormatException(settingsFormat);
        }

        enigmaMachine.setMachineSettingInitialized(true);
    }
    public void setRotorsInUse(RotorIDSector rotorIDSector) throws RotorsInUseSettingsException {
        enigmaMachine.initializeRotorsInUseSettings(rotorIDSector);
        settingsFormat.addSector(rotorIDSector);
    }

    public void setStartingPositionRotors(StartingRotorPositionSector startingPositionTheRotors, RotorIDSector rotorIDSector) throws StartingPositionsOfTheRotorException {
        enigmaMachine.setStartingPositionRotorsSettings(startingPositionTheRotors, rotorIDSector);
        settingsFormat.addSector(startingPositionTheRotors);
    }

    public void setReflectorInUse(ReflectorIdSector reflectorInUse) throws ReflectorSettingsException {
        enigmaMachine.setReflectorInUseSettings(reflectorInUse);
        settingsFormat.addSector(reflectorInUse);
    }

    public void setPluginBoard(PluginBoardSector pluginBoardSector) throws PluginBoardSettingsException {
        enigmaMachine.setPluginBoardSettings(pluginBoardSector);
        settingsFormat.addSector(pluginBoardSector);
    }


    @Override
    public void resetSettings() {
        enigmaMachine.resetSettings();
    }

    @Override
    public MachineDetails displaySpecifications() throws Exception {
        if(!isMachineExists()) {
            throw new Exception("There is no exists Machine");
       }

       machineDetails = new MachineDetails(enigmaMachine.getAllrotors(), enigmaMachine.getCurrentRotorsInUse(), enigmaMachine.getAllReflectors(), enigmaMachine.getCurrentReflectorInUse(), enigmaMachine.getKeyboard(), enigmaMachine.getPluginBoard(), settingsFormat);

       return machineDetails;
    }

    @Override
    public void analyzeHistoryAndStatistics() {

    }

    @Override
    public String processInput(String inputToProcess) {
        return getProcessedInput(inputToProcess);
    }

    private String getProcessedInput(String inputToProcess) {
        //TODO you need to initialize the machine first before decoding !
        // also maybe there is no any machine? or the xml file didnt loaded, or the setting didnt initialize?
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
