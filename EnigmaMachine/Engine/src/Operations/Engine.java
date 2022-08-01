package Operations;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.EnigmaMachine.*;
import EnigmaMachine.Reflctor;
import EnigmaMachine.Rotor;
import EnigmaMachineException.GeneralEnigmaMachineException;
import EnigmaMachineException.NotXmlFileException;
import Jaxb.Schema.Generated.*;
import TDO.MachineDetails;
import javafx.util.Pair;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static java.lang.Integer.parseInt;


public class Engine implements OperationsMachine, Serializable {
    private EnigmaMachine enigmaMachine;
    private final GeneralEnigmaMachineException enigmaMachineException = new GeneralEnigmaMachineException();
    public void setMachineDetails(String machineDetailsXmlFilePath) {
        // TODO implement here also validation.(the file exist)
        try {
            InputStream inputStream = new FileInputStream(new File(machineDetailsXmlFilePath));
            if(!machineDetailsXmlFilePath.endsWith(".xml")){
                throw new NotXmlFileException();
            }
            CTEEnigma enigma = deserializeFrom(inputStream);
            System.out.println("name of first country is: " + enigma.getCTEMachine().getABC());
        }
        catch (JAXBException | FileNotFoundException | NotXmlFileException e) { //should catch the exception of the xml in the UI.
            e.printStackTrace();
        }
        transformJAXBClassesToEnigmaMachine(engima);

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
        Map<RomanNumber ,Reflctor> machineReflectors;
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
            machineReflectors.put(convertStringToRomanNumber(reflector.getId()),currentReflector);
        }

        return machineReflectors;
    }
    private RomanNumber convertStringToRomanNumber(String romanNumberString){
        RomanNumber convertedRomanNumber = null;
        for(RomanNumber romanNumber: RomanNumber.values()){
            if(romanNumber.toString().equals(romanNumberString)){
                convertedRomanNumber = romanNumber;
            }
        }
        return convertedRomanNumber;
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

    @Override
    public void automaticSettingsInitialize() {

    }

    @Override
    public void manualSettingsInitialize() {

    }

    @Override
    public void setMachineDetails() {

    }

    @Override
    public MachineDetails getMachineDetails() throws Exception {
        if(enigmaMachine != null)
            return new MachineDetails(enigmaMachine.getAllrotors(), enigmaMachine.getCurrentRotorsInUse(), enigmaMachine.getAllReflectors(), enigmaMachine.getCurrentReflectorInUse(), enigmaMachine.getKeyboard(), enigmaMachine.getPluginBoard());
        else {
            throw new Exception("There is no exists Machine");
        }
    }

    @Override
    public void analyzeMachineHistoryAndStatistics() {

    }

    @Override
    public void processInput() {

    }
}
