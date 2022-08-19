package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.MachineNotExistsException;

import java.io.*;
public class MachineState implements Serializable {
    private EnigmaMachine enigmaMachine;
    private  StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer;

    public MachineState(EnigmaMachine enigmaMachine, StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer) {
        this.enigmaMachine = enigmaMachine;
        this.statisticsAndHistoryAnalyzer = statisticsAndHistoryAnalyzer;
    }

    public EnigmaMachine getEnigmaMachine() {
        return enigmaMachine;
    }

    public void setEnigmaMachine(EnigmaMachine enigmaMachine) {
        this.enigmaMachine = enigmaMachine;
    }

    public StatisticsAndHistoryAnalyzer getStatisticsAndHistoryAnalyzer() {
        return statisticsAndHistoryAnalyzer;
    }

    public void setStatisticsAndHistoryAnalyzer(StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer) {
        this.statisticsAndHistoryAnalyzer = statisticsAndHistoryAnalyzer;
    }

    public static void saveStateMachineToFile(String path, EngineManager engineManager) throws IOException, MachineNotExistsException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(path))) {
            out.writeObject(new MachineState(engineManager.getCurrentEnigmaMachine(), engineManager.getCurrentStatisticsAndHistory()));
            out.flush();
        }
    }

    public static void loadStateMachineFromFile(String path, EngineManager engineManager) throws  IOException, ClassNotFoundException{
        try(ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(path))) {
            MachineState machineState = (MachineState) in.readObject();
            engineManager.setCurrentMachine(machineState.enigmaMachine);
            engineManager.setCurrentStatisticsAndHistory(machineState.statisticsAndHistoryAnalyzer);
        }
    }
}
