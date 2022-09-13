package BruteForce;

import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import DesktopUserInterface.MainScene.MainController;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.SettingsFormat;
import EnigmaMachineException.BruteForceException;
import EnigmaMachineException.IllegalAgentsAmountException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private BruteForceUIAdapter bruteForceUIAdapter;
    private BruteForceTask bruteForceTask;
    private String decryptedMessege;
    private MainController mainController;
    private Runnable onFinish;
    private SettingsFormat decryptedSettingsFormat;
    private Thread tasksManagerThread;

    public DecryptionManager() {
        this.bruteForceUIAdapter = null;
        this.candidatesThreadPoolExecutor = Executors.newFixedThreadPool(1);
        this.decryptedMessege = null;
    }
    public DecryptionManager(EnigmaMachine enigmaMachine, Dictionary dictionary, BruteForceUIAdapter bruteForceUIAdapter, BruteForceTask bruteForceTask, String encryptedString) {
        this.bruteForceUIAdapter = bruteForceUIAdapter;
        this.bruteForceTask = bruteForceTask;
        this.enigmaMachine = enigmaMachine;
        this.dictionary = dictionary;
        this.candidatesThreadPoolExecutor = Executors.newFixedThreadPool(1);
        this.decryptedMessege = null;
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

    public void startDeciphering() throws Exception {
        try {
            if(decryptedMessege == null || enigmaMachine.containsCharNotInMAMachineKeyboard((decryptedMessege))) {
                //List<Character> lettersNotInAbc = new ArrayList<>(enigmaMachine.getCharsNotInMachineKeyboard(decryptedMessege));
                throw new IllegalArgumentException("Error: You must enter a string to process before start deciphering: " + System.lineSeparator());
            }

            tasksManager = new TasksManager(enigmaMachine, decryptedMessege, bruteForceTask, bruteForceUIAdapter,
                                            dictionary, candidatesThreadPoolExecutor, decryptedSettingsFormat,
                                            (stop) -> mainController.onTaskFinished(Optional.ofNullable(onFinish)));
            mainController.bindTaskToUIComponents(tasksManager, onFinish);
            
            tasksManagerThread = new Thread(tasksManager);
            tasksManagerThread.start();
        }catch (Exception e) {
            throw new BruteForceException("You must enter a string to process before start deciphering");//TODO chen: call task manager to start
        }


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

    public void stopBruteForceMission() {
        tasksManager.cancel();

        clearMission();
    }

    private void clearMission() {
        this.candidatesThreadPoolExecutor = Executors.newFixedThreadPool(1);
    }

    public void pauseMission() {
        tasksManagerThread.interrupt();
    }

    public void resumeMission() {
        tasksManager.resumeMission();
    }
}
