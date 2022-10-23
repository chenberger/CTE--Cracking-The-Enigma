package BruteForce;

import DTO.BruteForceTask;
import DTO.TaskToAgent;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import DesktopUserInterface.MainScene.MainController;
import Engine.AlliesManager.Allie;
import Engine.Dictionary;
import Engine.EngineManager;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.SettingsFormat;
import EnigmaMachineException.BruteForceException;
import EnigmaMachineException.IllegalAgentsAmountException;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecryptionManager {
    private Allie allie;
    private TasksProducer tasksProducer;
    private int maxCurrentAmountOfAgents;
    private Dictionary dictionary;
    private EnigmaMachine enigmaMachine;
    private ExecutorService candidatesThreadPoolExecutor;
    private TasksManager tasksManager;
    private static final int MIN_AGENTS_AMOUNT = 2;
    private static final int MAX_AGENTS_AMOUNT = 50;
    private BruteForceUIAdapter bruteForceUIAdapter;
    private BruteForceTask bruteForceTask;
    private String decryptedMessage;
    private MainController mainController;
    private Runnable onFinish;
    private SettingsFormat decryptedSettingsFormat;
    private Thread tasksProducerThread;
    private Boolean isMissionOnProgress;

    public DecryptionManager() {
        //this.bruteForceUIAdapter = null;
        //this.candidatesThreadPoolExecutor = Executors.newFixedThreadPool(1);
        this.decryptedMessage = null;
        this.isMissionOnProgress = false;
    }

    public void setAllie(Allie allie) {
        this.allie = allie;
    }

    public DecryptionManager(EnigmaMachine enigmaMachine, Dictionary dictionary, BruteForceUIAdapter bruteForceUIAdapter, BruteForceTask bruteForceTask, String encryptedString) {
        this.bruteForceUIAdapter = bruteForceUIAdapter;
        this.bruteForceTask = bruteForceTask;
        this.enigmaMachine = enigmaMachine;
        this.dictionary = dictionary;
        this.candidatesThreadPoolExecutor = Executors.newFixedThreadPool(1);
        this.decryptedMessage = null;
        this.isMissionOnProgress = false;
    }

    public void setMaxCurrentAmountOfAgents(int maxCurrentAmountOfAgents) throws IllegalAgentsAmountException {
        if (maxCurrentAmountOfAgents >= MIN_AGENTS_AMOUNT && maxCurrentAmountOfAgents <= MAX_AGENTS_AMOUNT) {
            this.maxCurrentAmountOfAgents = maxCurrentAmountOfAgents;
        } else {
            throw new IllegalAgentsAmountException(maxCurrentAmountOfAgents, MIN_AGENTS_AMOUNT, MAX_AGENTS_AMOUNT);
        }
    }

    public int getMaxCurrentAmountOfAgents() {
        return maxCurrentAmountOfAgents;
    }

    //public void startDeciphering() throws Exception {
    //    try {
    //        if(decryptedMessage == null || enigmaMachine.containsCharNotInMAMachineKeyboard((decryptedMessage))) {
    //            //List<Character> lettersNotInAbc = new ArrayList<>(enigmaMachine.getCharsNotInMachineKeyboard(decryptedMessege));
    //            throw new IllegalArgumentException("Error: You must enter a string to process before start deciphering: " + System.lineSeparator());
    //        }

    //        tasksManager = new TasksManager(enigmaMachine, decryptedMessage, bruteForceTask, bruteForceUIAdapter,
    //                                        dictionary, candidatesThreadPoolExecutor, decryptedSettingsFormat,
    //                                        (stop) -> mainController.onTaskFinished(Optional.ofNullable(onFinish)));
    //        //mainController.bindTaskToUIComponents(tasksManager, onFinish);
    //        tasksManager.valueProperty().addListener((observable, oldValue, newValue) -> {isMissionOnProgress = false; });
    //        isMissionOnProgress = true;
    //
    //        tasksManagerThread = new Thread(tasksManager);
    //        tasksManagerThread.start();
    //    }catch (Exception e) {
    //        throw new BruteForceException("You must enter a string to process before start deciphering");
    //    }
    //}


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
        this.decryptedMessage = processedMessage;
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
        this.isMissionOnProgress = false;
        tasksManager.cancel();

        clearMission();
    }

    private void clearMission() {
        this.candidatesThreadPoolExecutor = Executors.newFixedThreadPool(1);
    }

    //public void pauseMission() {
    //    System.out.println("=== try to pause ");
    //    tasksManagerThread.interrupt();
    //}

    //public void resumeMission() {
    //    tasksManager.resumeMission();
    //}

    public boolean onProgress() {
        return isMissionOnProgress;
    }

    public void startDeciphering(String processedMessage, Long taskSize, DifficultyLevel level, EngineManager engineManager) {
        try {
            setRelevantDecipheringData(processedMessage, taskSize, level, engineManager);
            if (decryptedMessage == null || enigmaMachine.containsCharNotInMAMachineKeyboard((decryptedMessage))) {
                //List<Character> lettersNotInAbc = new ArrayList<>(enigmaMachine.getCharsNotInMachineKeyboard(decryptedMessege));
                throw new IllegalArgumentException("Error: You must enter a string to process before start deciphering: " + System.lineSeparator());
            }

            tasksProducer = new TasksProducer(bruteForceTask, decryptedMessage, dictionary, enigmaMachine, decryptedSettingsFormat, allie);
            isMissionOnProgress = true;
            tasksProducer.setContestIsOn();

            tasksProducerThread = new Thread(tasksProducer);
            tasksProducerThread.start();

        } catch (CloneNotSupportedException | IllegalArgumentException e) {
            new Exception("Error: Failed to clone enigma machine");
        }

    }

    private void setRelevantDecipheringData(String processedMessage, Long taskSize, DifficultyLevel level, EngineManager engineManager) throws CloneNotSupportedException {
        this.decryptedMessage = processedMessage;
        this.bruteForceTask = new BruteForceTask(level, taskSize.intValue());
        this.dictionary = engineManager.getDictionaryObject();
        this.enigmaMachine = engineManager.getEnigmaMachine();
        setCodeConfigurationBeforeProcess(this.enigmaMachine.getCurrentSettingsFormat());
    }

    public TasksProducer getTasksProducer() {
        return tasksProducer;
    }

    public void stopDeciphering() {
        this.isMissionOnProgress = false;
        try {
            tasksProducerThread.interrupt();
            tasksProducer.setContestIsOff();
            //System.out.println("=== stop deciphering");
        } catch (Exception e) {
            System.out.println("Error: Failed to stop deciphering");
        }
    }

}
