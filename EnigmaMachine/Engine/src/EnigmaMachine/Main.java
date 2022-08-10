package EnigmaMachine;

import Engine.*;
import TDO.MachineDetails;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.*;
public class Main {

    public static void main(String[] args) {
        try {
            EngineManager engineManager = new EngineManager();
            engineManager.setMachineDetails("C:\\Chen Berger\\Java exercises\\Ex1\\Enigma-Machine\\EnigmaMachine\\Engine\\src\\Resources\\ex1-sanity-small.xml");
            List<Integer> rotorId = new ArrayList<>(Arrays.asList(1, 2));
            List<Character> rotorPositions = new ArrayList<>(Arrays.asList('C', 'C'));

            RotorIDSector rotorIDSector = new RotorIDSector(rotorId);
            StartingRotorPositionSector initialRotorPositionSector = new StartingRotorPositionSector(rotorPositions);
            ReflectorIdSector reflectorIdSector = new ReflectorIdSector(new ArrayList<RomanNumber>(Arrays.asList(RomanNumber.I)));
            PluginBoardSector pluginPairSector = new PluginBoardSector(new ArrayList<>(Arrays.asList(new Pair<>('C', 'B'))));

            engineManager.clearSettings();
            engineManager.setRotorsInUse(rotorIDSector);
            engineManager.setStartingPositionRotors(initialRotorPositionSector, rotorIDSector);
            engineManager.setReflectorInUse(reflectorIdSector);
            engineManager.setPluginBoard(pluginPairSector);
            engineManager.checkIfTheSettingsInitialized();

            MachineDetails details = engineManager.displaySpecifications();
            System.out.println("Before:");
            System.out.println("Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
            System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
            System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
            System.out.println("Machine settings: " + details.getMachineSettings());
            System.out.println("Decode : " + engineManager.processInput("CDA"));
            //TODO erez: fix reset bug
            engineManager.resetSettings();
            System.out.println("After:");
/*            engine.clearSettings();
            engine.setRotorsInUse(rotorIDSector);
            engine.setStartingPositionRotors(initialRotorPositionSector, rotorIDSector);
            engine.setReflectorInUse(reflectorIdSector);
            engine.setPluginBoard(pluginPairSector);
            engine.checkIfTheSettingsInitialized();*/
            details = engineManager.displaySpecifications();

            System.out.println("Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
            System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
            System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
            System.out.println("Machine settings: " + details.getMachineSettings());
            System.out.println("After: Decode : " + engineManager.processInput("CDA"));
            System.out.println(engineManager.analyzeHistoryAndStatistics());

            for (int i = 1; i < 5; i++) {
                if(i == 1) {
                    engineManager.setSettingsAutomatically();
                    details = engineManager.displaySpecifications();
                    System.out.println("Auto " + i + ": " + System.lineSeparator() + "Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
                    System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
                    System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
                    System.out.println("Machine settings: " + details.getMachineSettings());
                    System.out.println("Decode : " + engineManager.processInput("CDA"));
                    System.out.println("Messages counter: " + engineManager.displaySpecifications().getMessagesCounter() + System.lineSeparator());
                    engineManager.resetSettings();
                    System.out.println("After:");
                    System.out.println("Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
                    System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
                    System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
                    System.out.println("Machine settings: " + details.getMachineSettings());
                    System.out.println("After: Decode : " + engineManager.processInput("CDA"));
                }

                if(i == 2)
                {
                    engineManager.setSettingsAutomatically();
                    details = engineManager.displaySpecifications();
                    System.out.println("Auto " + i + ": " + System.lineSeparator() + "Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
                    System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
                    System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
                    System.out.println("Machine settings: " + details.getMachineSettings());
                    System.out.println("Decode : " + engineManager.processInput("A"));
                    System.out.println("Decode : " + engineManager.processInput("B"));
                    System.out.println("Messages counter: " + engineManager.displaySpecifications().getMessagesCounter() + System.lineSeparator());
                    System.out.println(engineManager.analyzeHistoryAndStatistics());
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
