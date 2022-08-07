package EnigmaMachine;

import Operations.*;
import TDO.MachineDetails;

import java.util.ArrayList;
import java.util.*;
public class Main {

    public static void main(String[] args) {
        try {
            Engine engine = new Engine();

            List<Integer> rotorId = new ArrayList<>(Arrays.asList(1, 2));
            List<Character> rotorPositions = new ArrayList<>(Arrays.asList('A', 'C'));

            RotorIDSector rotorIDSector = new RotorIDSector(rotorId);
            StartingRotorPositionSector initialRotorPositionSector = new StartingRotorPositionSector(rotorPositions);
            ReflectorIdSector reflectorIdSector = new ReflectorIdSector(new ArrayList<RomanNumber>(Arrays.asList(RomanNumber.I)));
            PluginBoardSector pluginPairSector = new PluginBoardSector(new ArrayList<>());

            engine.clearSettings();
            engine.setRotorsInUse(rotorIDSector);
            engine.setStartingPositionRotors(initialRotorPositionSector, rotorIDSector);
            engine.setReflectorInUse(reflectorIdSector);
            engine.setPluginBoard(pluginPairSector);
            engine.checkIfTheSettingsInitialized();

            MachineDetails details = engine.displaySpecifications();
            System.out.println("Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
            System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
            System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
            System.out.println("Machine settings: " + details.getMachineSettings());
            System.out.println("Decode : " + engine.processInput("CDA"));
            System.out.println("Decode : " + engine.processInput("D"));
            engine.resetSettings();
            System.out.println("Decode : " + engine.processInput("CDA"));
            System.out.println("Decode : " + engine.processInput("D"));
            System.out.println(engine.analyzeHistoryAndStatistics());

/*            for (int i = 1; i < 5; i++) {
                if(i == 1) {
                    engine.setSettingsAutomatically();
                    details = engine.displaySpecifications();
                    System.out.println("Auto " + i + ": " + System.lineSeparator() + "Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
                    System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
                    System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
                    System.out.println("Machine settings: " + details.getMachineSettings());
                    System.out.println("Decode : " + engine.processInput("C"));
                    System.out.println("Decode : " + engine.processInput("D"));
                    System.out.println("Messages counter: " + engine.displaySpecifications().getMessagesCounter() + System.lineSeparator());

                }

                if(i == 2)
                {
                    engine.setSettingsAutomatically();
                    details = engine.displaySpecifications();
                    System.out.println("Auto " + i + ": " + System.lineSeparator() + "Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
                    System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
                    System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
                    System.out.println("Machine settings: " + details.getMachineSettings());
                    System.out.println("Decode : " + engine.processInput("A"));
                    System.out.println("Decode : " + engine.processInput("B"));
                    System.out.println("Messages counter: " + engine.displaySpecifications().getMessagesCounter() + System.lineSeparator());
                    System.out.println(engine.analyzeHistoryAndStatistics());
                }
            }*/
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
