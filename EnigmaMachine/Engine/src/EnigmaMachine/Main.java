package EnigmaMachine;

import EnigmaMachineException.PluginBoardSettingsException;
import Operations.Engine;
import TDO.MachineDetails;
import javafx.util.Pair;

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

/*            engine.setRotorsInUse(rotorIDSector);
            engine.setStartingPositionRotors(initialRotorPositionSector, rotorIDSector);
            engine.setReflectorInUse(reflectorIdSector);
            engine.setPluginBoard(pluginPairSector);
            engine.checkIfTheSettingsInitialized();

            MachineDetails details = engine.displaySpecifications();
            System.out.println("Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
            System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
            System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
            System.out.println("Machine settings: " + details.getMachineSettings());
            System.out.println("Decode : " + engine.processInput("C"));
            System.out.println("Decode : " + engine.processInput("D"));
            System.out.println("Decode : " + engine.processInput("A"));*/

          //TODO check why somtimes the pluginboard get null
            engine.setSettingsAutomatically();
            MachineDetails details = engine.displaySpecifications();
            System.out.println("Auto:" + System.lineSeparator() + "Total rotors: " + details.getAmountOfTotalRotors() + ", Current rotors: " + details.getAmountCurrentRotorsInUse());
            System.out.println("Notch positions: " + details.getNotchPositionsInRotorsInUse());
            System.out.println("Reflectors amount: " + details.getAmountOfTotalReflectors());
            System.out.println("Machine settings: " + details.getMachineSettings());
            System.out.println("Decode : " + engine.processInput("C"));
            System.out.println("Decode : " + engine.processInput("D"));
            System.out.println("Decode : " + engine.processInput("A"));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


        //region Format test
        /*List<Integer> rotorId = new ArrayList<>(Arrays.asList(1, 2, 3));
        char openSector = '<';
        char closeSector = '>';
        CharSequence delimiter = ",";
        CharSequence delimiterPair = "|";

        RotorIDSector rotorIDSector = new RotorIDSector(rotorId);
        //InitialRotorPositionSector initialRotorPositionSector = new InitialRotorPositionSector(rotorId.toArray());
        ReflectorIdSector reflectorIdSector = new ReflectorIdSector(new ArrayList<RomanNumber>(Arrays.asList(RomanNumber.IV)));
        PluginBoardSector pluginPairSector = new PluginBoardSector(new ArrayList<>(Arrays.asList(new Pair<>('A', 'D'))));
        System.out.println(enigmaMachine.decode('D'));

        SettingsFormat test = new SettingsFormat();
        test.addSector(rotorIDSector);
        //test.addSector(initialRotorPositionSector);
        test.addSector(reflectorIdSector);
        test.addSector(pluginPairSector);

        System.out.println(test);*/
        //endregion
    }
}
