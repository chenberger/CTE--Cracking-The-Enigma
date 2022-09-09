package BruteForce;

import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Keyboard;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.WordNotValidInDictionaryException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class AgentTask {
    private final Integer taskSize;
    private final EnigmaMachine enigmaMachine;
    private final String encryptedString;
    private final Dictionary dictionary;
    private final ThreadPoolExecutor candidatesThreadPoolExecutor;
    private final BruteForceUIAdapter bruteForceUIAdapter;
    private final DecipherStatistics decipherStatistics;
    private StartingRotorPositionSector startingRotorPositions;

    public AgentTask(Integer taskSize, StartingRotorPositionSector startingRotorPositionSector, EnigmaMachine enigmaMachine, String encryptedString, Dictionary dictionary, ThreadPoolExecutor candidatesThreadPoolExecutor, BruteForceUIAdapter bruteForceUIAdapter, DecipherStatistics decipherStatistics){

        this.taskSize = taskSize;
        this.enigmaMachine = enigmaMachine;
        this.encryptedString = encryptedString;
        this.dictionary = dictionary;
        this.candidatesThreadPoolExecutor = candidatesThreadPoolExecutor;
        this.bruteForceUIAdapter = bruteForceUIAdapter;
        this.decipherStatistics = decipherStatistics;
        this.startingRotorPositions = startingRotorPositionSector;
    }


    public Integer getTaskSize() {
        return taskSize;
    }

    public EnigmaMachine getEnigmaMachine() {
        return enigmaMachine;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public ExecutorService getCandidatesThreadPoolExecutor() {
        return candidatesThreadPoolExecutor;
    }

    public BruteForceUIAdapter getBruteForceUIAdapter() {
        return bruteForceUIAdapter;
    }

    public DecipherStatistics getDecipherStatistics() {
        return decipherStatistics;
    }

    public StartingRotorPositionSector getStartingRotorPositions() {
        return startingRotorPositions;
    }

    public void setStartingRotorPositions(StartingRotorPositionSector startingRotorPositions) {
        this.startingRotorPositions = startingRotorPositions;
    }

    public Keyboard getKeyboard() {
        return enigmaMachine.getKeyboard();
    }

    public void validateWordsInDictionary(List<String> wordsToCheck) throws WordNotValidInDictionaryException {
        dictionary.validateWords(wordsToCheck);
    }

    public void addDecryptionCandidateTaskToThreadPool(Runnable decryptionCandidateTask) {
        candidatesThreadPoolExecutor.execute(decryptionCandidateTask);
    }
}
