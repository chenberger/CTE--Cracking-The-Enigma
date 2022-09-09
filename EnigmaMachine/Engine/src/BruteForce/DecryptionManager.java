package BruteForce;

import DTO.BruteForceTask;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.DecryptionMessegeNotInitializedException;
import EnigmaMachineException.IllegalAgentsAmountException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecryptionManager {
    private int maxCurrentAmountOfAgents;
    private Dictionary dictionary;
    private EnigmaMachine enigmaMachine;
    private ExecutorService threadPoolExecutor;
    TasksManager tasksManager;
    private static final int MIN_AGENTS_AMOUNT = 2;
    private static final int MAX_AGENTS_AMOUNT = 50;
    private Integer taskSize;
    private BruteForceUIAdapter bruteForceUIAdapter;
    private BruteForceTask bruteForceTask;
    private DecipherStatistics decipherStatistics;
    private String decryptedMessege;

    public DecryptionManager() {
        this.bruteForceUIAdapter = null;
        this.decipherStatistics = new DecipherStatistics();
    }
    public DecryptionManager(EnigmaMachine enigmaMachine, Dictionary dictionary, BruteForceUIAdapter bruteForceUIAdapter, BruteForceTask bruteForceTask, String encryptedString) {
        this.bruteForceUIAdapter = bruteForceUIAdapter;
        this.decipherStatistics = new DecipherStatistics();
        this.bruteForceTask = bruteForceTask;
        this.enigmaMachine = enigmaMachine;
        this.dictionary = dictionary;
        this.threadPoolExecutor = Executors.newCachedThreadPool();
    }

    public void setMaxCurrentAmountOfAgents(int maxCurrentAmountOfAgents) throws IllegalAgentsAmountException {
        if(maxCurrentAmountOfAgents >= MIN_AGENTS_AMOUNT && maxCurrentAmountOfAgents <= MAX_AGENTS_AMOUNT) {
            this.maxCurrentAmountOfAgents = maxCurrentAmountOfAgents;
        }
        else {
            throw new IllegalAgentsAmountException(maxCurrentAmountOfAgents, MIN_AGENTS_AMOUNT, MAX_AGENTS_AMOUNT);
        }
    }

    public int getMaxCurrentAmountOfAgents() {
        return maxCurrentAmountOfAgents;
    }

    public void startDeciphering() {
        try {
            tasksManager = new TasksManager(enigmaMachine, decryptedMessege, bruteForceTask, bruteForceUIAdapter, dictionary, threadPoolExecutor, decryptedMessege);
            tasksManager.start();
        }//TODO chen: call task manager to start
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDecryptionCandidatesStatistics() {
        return decipherStatistics.toString();
    }

    public void setUIAdapter(BruteForceUIAdapter bruteForceUIAdapter) {
        this.bruteForceUIAdapter = bruteForceUIAdapter;
    }

    public void setBruteForceTask(BruteForceTask bruteForceTask) {
        this.bruteForceTask = bruteForceTask;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;

    }

    public static int getMinAgentsAmount() {
        return MIN_AGENTS_AMOUNT;
    }

    public static int getMaxAgentsAmount() {
        return MAX_AGENTS_AMOUNT;
    }


    public void setDecryptedMessage(String processedMessage) {
        this.decryptedMessege = processedMessage;
    }

    public void setEnigmaMachine(EnigmaMachine enigmaMachine) {
        this.enigmaMachine = enigmaMachine;
    }
}
