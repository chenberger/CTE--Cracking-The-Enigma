package Decryption;

import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.SettingsFormat;
import EnigmaMachineException.IllegalAgentsAmountException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class DecryptionManager {
    private int numberOfAgents;
    private Dictionary dictionary;

    private EnigmaMachine enigmaMachine;
    private static final int MIN_AGENTS_AMOUNT = 2;
    private static final int MAX_AGENTS_AMOUNT = 50;

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    private Integer sizeOfTask;

    private Map<SettingsFormat, DecryptionCandidateFormat> decryptionCandidates;

    public DecryptionManager(EnigmaMachine enigmaMachine, Dictionary dictionary) {
        this.decryptionCandidates = new HashMap<>();
        this.sizeOfTask = 0;
        this.numberOfAgents = 0;
        this.enigmaMachine = enigmaMachine;
        this.dictionary = dictionary;
    }


    public void addDecryptionCandidate(SettingsFormat settingsFormat, DecryptionCandidateFormat decryptionCandidateFormat){
        decryptionCandidates.put(settingsFormat, decryptionCandidateFormat);
    }
    public void setSizeOfTask(int sizeOfTask) {
        this.sizeOfTask = sizeOfTask;
    }
    public void setNumberOfAgents(int numberOfAgents) throws IllegalAgentsAmountException {
        if(numberOfAgents >= MIN_AGENTS_AMOUNT && numberOfAgents <= MAX_AGENTS_AMOUNT) {
            this.numberOfAgents = numberOfAgents;
        }
        else {
            throw new IllegalAgentsAmountException(numberOfAgents, MIN_AGENTS_AMOUNT, MAX_AGENTS_AMOUNT);
        }
    }
    public String getDecryptionCandidatesFormat() {
        if(decryptionCandidates.size() == 0) {
            return ("No candidate was found during the search." + System.lineSeparator());
        }
        else {
            return "Candidates found:" + System.lineSeparator() +
                    decryptionCandidates.entrySet().stream()
                    .map(pair -> pair.getValue().toString() + System.lineSeparator() +
                            "Found at configuration: "+ pair.getKey().toString() + System.lineSeparator())
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
