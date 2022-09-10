package BruteForce;

import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import DesktopUserInterface.MainScene.MainController;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.SettingsFormat;
import EnigmaMachineException.IllegalAgentsAmountException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecryptionManager {
    private int maxCurrentAmountOfAgents;
    private Dictionary dictionary;
    private EnigmaMachine enigmaMachine;
    private ExecutorService candidatesThreadPoolExecutor;
    private TasksManager tasksManager;
    private static final int MIN_AGENTS_AMOUNT = 2;
    private static final int MAX_AGENTS_AMOUNT = 50;
    private Integer taskSize;
    private BruteForceUIAdapter bruteForceUIAdapter;
    private BruteForceTask bruteForceTask;
    private DecipherStatistics decipherStatistics;
    private String decryptedMessege;
    private  MainController mainController;
    private Runnable onFinish;
    private SettingsFormat decryptedSettingsFormat;

    public DecryptionManager() {
        this.bruteForceUIAdapter = null;
        this.decipherStatistics = new DecipherStatistics();
        this.candidatesThreadPoolExecutor = Executors.newCachedThreadPool();
    }
    public DecryptionManager(EnigmaMachine enigmaMachine, Dictionary dictionary, BruteForceUIAdapter bruteForceUIAdapter, BruteForceTask bruteForceTask, String encryptedString) {
        this.bruteForceUIAdapter = bruteForceUIAdapter;
        this.decipherStatistics = new DecipherStatistics();
        this.bruteForceTask = bruteForceTask;
        this.enigmaMachine = enigmaMachine;
        this.dictionary = dictionary;
        this.candidatesThreadPoolExecutor = Executors.newCachedThreadPool();
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

    public void startDeciphering() throws IllegalArgumentException{
        try {
            if(decryptedMessege != null && enigmaMachine.containsCharNotInMAMachineKeyboard((decryptedMessege))) {
                List<Character> lettersNotInAbc = new ArrayList<>(enigmaMachine.getCharsNotInMachineKeyboard(decryptedMessege));
                throw new IllegalArgumentException("Error: The input contains char/s that are not in the machine keyboard which are: " + lettersNotInAbc + System.lineSeparator()
                        + "You can choose only from the following letters: " + enigmaMachine.getKeyboard().keySet());
            }

            tasksManager = new TasksManager(enigmaMachine, decryptedMessege, bruteForceTask, bruteForceUIAdapter, dictionary, candidatesThreadPoolExecutor, decryptedSettingsFormat);
            mainController.bindTaskToUIComponents(tasksManager, onFinish);

            new Thread(tasksManager).start();
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

    public void initialize(BruteForceTask bruteForceTask, BruteForceUIAdapter bruteForceUiAdapter, Runnable onFinish, EnigmaMachine enigmaMachine, Dictionary dictionary, MainController mainController) {
        this.bruteForceTask = bruteForceTask;
        this.enigmaMachine = enigmaMachine;
        this.dictionary = dictionary;
        this.bruteForceUIAdapter = bruteForceUiAdapter;
        this.mainController = mainController;
        this.onFinish = onFinish;
    }

    public void setCodeConfigurationBeforeProcess(SettingsFormat currentSettingsFormat) {
        this.decryptedSettingsFormat = currentSettingsFormat;
    }
}
