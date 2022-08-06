package Operations;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import EnigmaMachine.*;
import EnigmaMachineException.GeneralEnigmaMachineException;
import EnigmaMachineException.NotXmlFileException;
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


abstract public class Engine implements MachineOperations, Serializable {

    //region private data members
    private EnigmaMachine enigmaMachine;
    private SettingsFormat settingsFormat;
    private MachineDetails machineDetails;
    private final GeneralEnigmaMachineException enigmaMachineException = new GeneralEnigmaMachineException();

    //endregion

    //region JAXB Translation
    public void setMachineDetails(String machineDetailsXmlFilePath) {
        // TODO implement here also validation.(the file exist)
        is
        try {
            InputStream inputStream = new FileInputStream(new File(machineDetailsXmlFilePath));
            if(!machineDetailsXmlFilePath.endsWith(".xml")){
                throw new NotXmlFileException();
            }
            CTEEnigma enigma = deserializeFrom(inputStream);
            transformJAXBClassesToEnigmaMachine(engima);
        }
        catch (JAXBException | FileNotFoundException | NotXmlFileException e) { //should catch the exception of the xml in the UI.
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
        checkIfABCIsValid(JAXBGeneratedEnigma.getCTEMachine().getABC());
        List<CTERotor> CTERotors = JAXBGeneratedEnigma.getCTEMachine().getCTERotors().getCTERotor();
        List<CTEReflector> CTEReflectors = JAXBGeneratedEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        Map<Integer,Rotor> machineRotors;
        Map<RomanNumber,Reflctor> machineReflectors;
        Map<Character,Integer> machineKeyBoard;
        int rotorsCount = JAXBGeneratedEnigma.getCTEMachine().getRotorsCount();;

        machineKeyBoard = getMachineKeyboardFromCTEKeyboard(JAXBGeneratedEnigma.getCTEMachine().getABC().toCharArray());
        machineRotors = getMachineRotorsFromCTERotors(CTERotors, JAXBGeneratedEnigma.getCTEMachine().getABC().toCharArray());
        machineReflectors = getMachineReflectorsFromCTEReflectors(CTEReflectors);

        if(enigmaMachineException.noExceptionRaised()) {
            enigmaMachine = new EnigmaMachine(machineRotors, machineReflectors, machineKeyBoard,rotorsCount);
        }
        else {
            throw enigmaMachineException;
        }
    }

    private void checkIfABCIsValid(String abc) {
        if(abc.length() == 0 || abcContainsDuplications(abc)){
            throw new IllegalArgumentException("the xml abc contains duplications or is empty");
        }
    }

    private boolean abcContainsDuplications(String abc) {
        Map<Character, Integer> abcMap = new HashMap<>();
        for(int i = 0; i < abc.length(); i++){
            if(abcMap.containsKey(abc.charAt(i))){
                return true;
            }
            else{
                abcMap.put(abc.charAt(i), 1);
            }
        }
        return false;
    }


    private Map<RomanNumber ,Reflctor> getMachineReflectorsFromCTEReflectors(List<CTEReflector> cteReflectors) {
        Map<RomanNumber,Reflctor> machineReflectors = new HashMap<>();
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
            Reflctor currentReflector = new Reflctor(RomanNumber.valueOf(reflector.getId()), currentReflectorMapping);
            machineReflectors.put(RomanNumber.convertStringToRomanNumber(reflector.getId()),currentReflector);
        }

        return machineReflectors;
    }
    private Map<Integer, Rotor> getMachineRotorsFromCTERotors(List<CTERotor> cteRotors, char[] cteABC) {
        Map<Integer,Rotor> machineRotors = new HashMap<Integer, Rotor>();//TODO check if rotors id are numbers, left, right from abc. length is as the length of abc and each shows once, that the notch is in the length of the abc.
        //TODO check that the rotors count which the number of rotors in use is between 2 and 99, return the rotors count to erez.
        for(CTERotor rotor: cteRotors){
            Map<Character,Character> currentRotorMap = new HashMap<>();
            List<Pair<Character,Character>> currentRotorPairs = new ArrayList<>();
            for(CTEPositioning position: rotor.getCTEPositioning()){
                checkIfPositionLettersInABC(position, cteABC);
                if(currentRotorMap.containsKey(position.getLeft().charAt(0))){
                    enigmaMachineException.addValuesWithSameMappingInOneRotor(position.getLeft().charAt(0),position.getRight().charAt(0), currentRotorMap.get(position.getLeft().charAt(0)));
                }
                Pair<Character,Character> currentPair = new Pair<>(position.getLeft().charAt(0),position.getRight().charAt(0));
                // TODO check if the Length is 1 and if the character is in ABC, and that there are no duplicates of chars in each side where ever there are numbers, check that they are ints.
                currentRotorPairs.add(currentPair);
                currentRotorMap.put(position.getLeft().charAt(0),position.getRight().charAt(0));
            }

            Rotor currentRotor = new Rotor(rotor.getId(), rotor.getNotch() - 1, currentRotorPairs);
            machineRotors.put(rotor.getId(),currentRotor);
        }
        return machineRotors;
    }

    private void checkIfPositionLettersInABC(CTEPositioning position, char[] cteABC) {
        //TODO add length validation.
        for(Character charInAbc: cteABC){
        }
            if(position.getLeft().charAt(0) == charInAbc){
                break;
            }
        }
        enigmaMachineException.addLettersToNotInABC(position.getLeft());
        for (Character charInAbc : cteABC) {
            if (position.getRight().charAt(0) == charInAbc) {
                break;
            }
        }
        enigmaMachineException.addLettersToNotInABC(position.getRight().charAt(0));
    }


    private Map<Character, Integer> getMachineKeyboardFromCTEKeyboard(char[] cteKeyboard) {
        Map<Character, Integer> machineKeyBoard = new HashMap<>();
        int indexToMappingTOInKeyboard = 0;

        if (cteKeyboard.length % 2 != 0) {
            enigmaMachineException.setIsoddLength();
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
    @Override
    public void setSettingsAutomatically() throws Exception {
        //TODO get random data
        //setSettings();

    }

/*    @Override
    public void setSettingsManually() throws Exception {
        setSettings();
        enigmaMachine.initializeSettings(settingsFormat);
    }

    private void setSettings() throws Exception {
        if(!isMachineExsists()) {
            throw new Exception("There is no exists Machine");
        }

        setRotorsInUse(rotorIdSector);
        setStartingPositionRotors(rotorIdSector);
        setReflectorInUse(rotorIdSector);
        setPluginBoard(rotorIdSector);
        enigmaMachine.setMachineSettingInitialized(true);
    }*/
     public void setRotorsInUse(RotorIDSector rotorIDSector) throws Exception {
        enigmaMachine.initializeRotorsInUseSettings(rotorIDSector);
        settings.put(Rotors)
     }

    public void setStartingPositionRotors(InitialRotorPositionSector startingPositionTheRotors, RotorIDSector rotorIDSector) throws Exception {
        enigmaMachine.setStartingPositionRotorsSettings(startingPositionTheRotors, rotorIDSector);
    }

    public void setReflectorInUse(ReflectorIdSector reflectorInUse) throws Exception {
        enigmaMachine.setReflectorInUseSettings(reflectorInUse);
    }

    public void setPluginBoard(PluginBoardSector pluginBoardSector) throws Exception {
        enigmaMachine.setPluginBoardSettings(pluginBoardSector);
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

       machineDetails = new MachineDetails(enigmaMachine.getAllrotors(), enigmaMachine.getCurrentRotorsInUse(), enigmaMachine.getAllReflectors(), enigmaMachine.getCurrentReflectorInUse(), enigmaMachine.getKeyboard(), enigmaMachine.getPluginBoard());
       machineDetails.initializeSettingFormat();

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
    public void finishSession(){
        exit();


    //endregion

    private boolean isMachineExists() {
        return
    }

}
