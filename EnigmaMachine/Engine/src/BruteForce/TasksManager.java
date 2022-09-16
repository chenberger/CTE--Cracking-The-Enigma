package BruteForce;

import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Reflector;
import EnigmaMachine.Rotor;
import EnigmaMachine.Settings.*;
import EnigmaMachineException.*;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TasksManager extends Task<Boolean> {
    private static final int  MAX_QUEUE_SIZE = 1000;
    private ThreadPoolExecutor tasksPool;
    private DecipherStatistics decipherStatistics;
    private Double totalCombinations;
    private BruteForceUIAdapter bruteForceUIAdapter;
    private String encryptedString;
    private Dictionary dictionary;
    private Integer amountOfAgents;
    private List<Character> startingRotorsPositions;
    private ExecutorService candidatesPool;
    private Integer taskSize;
    private EnigmaMachine enigmaMachine;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    private long totalTaskSize;
    private long currentTaskSize;
    private long totalAgentTasksTime;
    private long totalAgentTasksAverageTime;
    private SettingsFormat settingsFormat;
    private BlockingQueue<Runnable> blockingQueue;
    private Consumer<Runnable> onCancel;
    private Boolean isPaused;


    public TasksManager(EnigmaMachine enigmaMachine, String encryptedString, BruteForceTask bruteForceTask, BruteForceUIAdapter UIAdapter, Dictionary dictionary, ExecutorService candidatesPool, SettingsFormat decryptedSettingsFormat, Consumer<Runnable> onCancel) throws Exception {
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = bruteForceTask.getDifficultTaskLevel();
        this.encryptedString = encryptedString;
        this.bruteForceUIAdapter = UIAdapter;
        this.amountOfAgents = bruteForceTask.getAmountOfAgents();
        this.taskSize = bruteForceTask.getTaskSize();
        this.dictionary = dictionary;
        this.candidatesPool = candidatesPool;
        this.settingsFormat = decryptedSettingsFormat;
        this.onCancel = onCancel;
        this.startingRotorsPositions = setAllRotorsToFirstLetterAtStart();
        this.blockingQueue = new ArrayBlockingQueue<Runnable>(MAX_QUEUE_SIZE);
        this.tasksPool = new ThreadPoolExecutor(amountOfAgents, amountOfAgents, 5000, TimeUnit.SECONDS, blockingQueue ,new AgentThreadFactory(amountOfAgents), new ThreadPoolExecutor.CallerRunsPolicy());
        decipherStatistics = new DecipherStatistics();

        initializeMachineSettings();
        initializeTaskData();
        calcMissionSize();

    }

    private void initializeMachineSettings() {
        enigmaMachine.clearSettings();

        settingsFormat.getSettingsFormat().forEach(sector -> {
            try {
                sector.setSectorInTheMachine(enigmaMachine);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });

        enigmaMachine.setTheInitialCodeDefined(true);
        enigmaMachine.resetSettings();

        enigmaMachine.clearSettings();
        settingsFormat.getSettingsFormat().forEach(sector -> sector.addSectorToSettingsFormat(enigmaMachine));

        if (enigmaMachine.isPluginBoardSet()) {
            enigmaMachine.getOriginalSettingsFormat().setIfPluginBoardSet(true);
        } else {
            enigmaMachine.getOriginalSettingsFormat().setIfPluginBoardSet(false);
        }
    }

    private void initializeTaskData() {
        this.totalAgentTasksTime = 0;
        this.totalAgentTasksAverageTime = 0;
        this.currentTaskSize = 0;
        this.bruteForceUIAdapter.updateTotalProcessedAgentTasks(currentTaskSize);
        this.bruteForceUIAdapter.updateAverageTaskTime(totalAgentTasksAverageTime);
        this.bruteForceUIAdapter.updateMissionTotalTime(totalAgentTasksTime);
    }

    private List<Character> setAllRotorsToFirstLetterAtStart() {
        List<Character> startingRotorsPositions = new ArrayList<>();

        for (int i = 0; i < enigmaMachine.getNumOfActiveRotors(); i++) {
            startingRotorsPositions.add(enigmaMachine.getKeyboard().getFirstCharacter());
        }
        return startingRotorsPositions;
    }


    public void setImpossibleTasks() throws Exception {
        List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors = getAllPossibleRotorsCombinationsFromAllPossibleRotorsExist();

        for (List<Integer> rotorCombination : allPossibleRotorsCombinationsFromAllRotors) {
            RotorIDSector rotorIDSector = new RotorIDSector(rotorCombination);
            setSectorInMachine(rotorIDSector);
            setHardTasks();
        }
    }

    private List<List<Integer>> getAllPossibleRotorsCombinationsFromAllPossibleRotorsExist() {
        List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors = new ArrayList<>();
        List<Integer> allPossibleRotors = new ArrayList<>();
        calculateAllPossibleRotorsIdsCombinations(enigmaMachine.getAllRotors().size(),1, enigmaMachine.getNumOfActiveRotors(), allPossibleRotors, allPossibleRotorsCombinationsFromAllRotors);
        return allPossibleRotorsCombinationsFromAllRotors;
    }

    public void calculateAllPossibleRotorsIdsCombinations(int totalNumberOfRotors, int left, int numberOfRotorsInUse, List<Integer> combination, List<List<Integer>> combinations) {
        // Pushing this vector to a vector of vector
        if (numberOfRotorsInUse == 0) {
            combinations.add(new ArrayList<>(combination.stream().collect(Collectors.toList())));
            return;
        }

        // i iterates from left to n. First time
        // left will be 1
        for (int i = left; i <= totalNumberOfRotors; ++i)
        {
            combination.add(i);
            calculateAllPossibleRotorsIdsCombinations(totalNumberOfRotors, i + 1, numberOfRotorsInUse - 1, combination, combinations);

            // Popping out last inserted element
            // from the vector
            combination.remove(combination.size() - 1);
        }

    }


    public void setHardTasks() throws Exception {
       List<List<Integer>> allPossibleRotorsCombinationsFromCurrentRotors = getAllPossibleRotorsCombinations();
       for(List<Integer> rotorsCombination : allPossibleRotorsCombinationsFromCurrentRotors) {
           RotorIDSector rotorIDSector = new RotorIDSector(rotorsCombination);
           setSectorInMachine(rotorIDSector);
           setMediumTasks();
       }
    }

    private List<List<Integer>> getAllPossibleRotorsCombinations() {
        List<List<Integer>> allPossibleRotorsCombinations = new ArrayList<>();
        List<Integer> currentRotorsInUse = enigmaMachine.getCurrentRotorsInUse().stream().map(Rotor::id).collect(Collectors.toList());
        calcPossibleRotorsCombinations(allPossibleRotorsCombinations, currentRotorsInUse, new ArrayList<>(), 0);


        return allPossibleRotorsCombinations;
    }

    private void calcPossibleRotorsCombinations(List<List<Integer>> allPossibleRotorsCombinations, List<Integer> currentRotorsInUse, List<Integer> currentRotors, int i) {
        for(int j = 0; j < currentRotorsInUse.size(); j++) {
            List<Integer> newCurrentRotors = new ArrayList<>(currentRotors);
            if(!newCurrentRotors.contains(currentRotorsInUse.get(j))) {
                newCurrentRotors.add(currentRotorsInUse.get(j));
                if(newCurrentRotors.size() == currentRotorsInUse.size()) {
                    allPossibleRotorsCombinations.add(newCurrentRotors);
                } else {
                    calcPossibleRotorsCombinations(allPossibleRotorsCombinations, currentRotorsInUse, newCurrentRotors, i + 1);
                }
            }
        }
    }
    private Integer binomial(int numOfActiveRotors, int numOfAllRotors) {
        return factorial(numOfAllRotors) / (factorial(numOfActiveRotors) * factorial(numOfAllRotors - numOfActiveRotors));
    }

    private Integer factorial(int size) {
        if(size == 0) {
            return 1;
        }
        return size * factorial(size - 1);
    }
    private void calcMissionSize(){
        switch (difficultyLevel) {
            case EASY:
                totalCombinations = Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors());
                break;
            case MEDIUM:
                totalCombinations = Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors());
                totalCombinations *= enigmaMachine.getAllReflectors().size();
                break;
            case HARD:
                totalCombinations = Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors());
                totalCombinations *= enigmaMachine.getAllReflectors().size();
                totalCombinations *= factorial(enigmaMachine.getNumOfActiveRotors());
                break;
            case IMPOSSIBLE:
                totalCombinations = Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors());
                totalCombinations *= enigmaMachine.getAllReflectors().size();
                totalCombinations *= factorial(enigmaMachine.getNumOfActiveRotors());
                totalCombinations *= binomial(enigmaMachine.getNumOfActiveRotors(), enigmaMachine.getAllRotors().size());
                break;
        }

        if(taskSize > Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors())) {
            totalTaskSize = (long) (totalCombinations / Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors()));
        }
        else {
            totalTaskSize = (long) (totalCombinations / taskSize);
        }
        bruteForceUIAdapter.updateTotalAgentsTasks(totalTaskSize);
    }

    public void setMediumTasks() throws Exception {
        for (Reflector reflector : enigmaMachine.getAllReflectors().values()) {
            ReflectorIdSector reflectorIdSector = new ReflectorIdSector(Arrays.asList(reflector.id()));
            setSectorInMachine(reflectorIdSector);
            setEasyTasks();
        }
    }

    private void setSectorInMachine(Sector<?> sectorToSet) throws ReflectorSettingsException, RotorsInUseSettingsException, PluginBoardSettingsException, StartingPositionsOfTheRotorException, CloneNotSupportedException {
        sectorToSet.validateSector(enigmaMachine);
        sectorToSet.setSectorInTheMachine(enigmaMachine);
        sectorToSet.addSectorToSettingsFormat(enigmaMachine);
    }

    public void setEasyTasks() throws Exception {
        int numOfPossibleRotorsPositions = (int) Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors());
        StartingRotorPositionSector currentStartingRotorsPositions = new StartingRotorPositionSector(startingRotorsPositions);

        while(numOfPossibleRotorsPositions > 0) {
            if (isCancelled()) {
                System.out.println("==== cancelled !!!!");
                throw new TaskIsCanceledException();
            }

            if(Thread.currentThread().isInterrupted()) {
                System.out.println("==== interrupt !!!!");
                pauseMission();
            }

            EnigmaMachine clonedEnigmaMachine = enigmaMachine.cloneMachine();
            AgentTask agentTask = new AgentTask(taskSize, (StartingRotorPositionSector) currentStartingRotorsPositions.clone(), clonedEnigmaMachine , encryptedString, dictionary, candidatesPool, bruteForceUIAdapter, decipherStatistics);
            Agent agent = new Agent(agentTask,this);

            try {
                blockingQueue.put(agent);
            }
            catch (InterruptedException ex) {
                System.out.println("==== interrupt2 !!!!");
                pauseMission();
                blockingQueue.put(agent);
            }
            numOfPossibleRotorsPositions -= taskSize;
            try {
                currentStartingRotorsPositions.setElements(enigmaMachine.getKeyboard().increaseRotorPositions(currentStartingRotorsPositions.getElements(), taskSize));
            }
            catch (Exception e) {
                break;
            }
        }
    }

    synchronized private void pauseMission() {
        isPaused = true;
        System.out.println("==== going to sleep for while, isPause = " + isPaused.toString());
        while (isPaused) {
            try{
                this.wait();
                System.out.println("===== still sleeping, isPause = " + isPaused.toString());
            }
            catch (InterruptedException e) { }
        }

        System.out.println("==== finally continue work");
    }

    synchronized public void resumeMission() {
        isPaused = false;
        this.notifyAll();
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            tasksPool.prestartAllCoreThreads();
            difficultyLevel.setTask(this);

            while(blockingQueue.size() > 0) {}
            candidatesPool.awaitTermination(8, TimeUnit.SECONDS);
        }
        catch(TaskIsCanceledException  | InterruptedException ex) {
            Platform.runLater(() -> {
                onCancel.accept(null);
            });

            candidatesPool.awaitTermination(8, TimeUnit.MILLISECONDS);
            tasksPool.awaitTermination(8, TimeUnit.MILLISECONDS);
            System.out.println("==== cancel or interrupt exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (Exception  e) {
            System.out.println(e.getMessage());
        }

        //TODO erez: what happen if not finished?
        return Boolean.TRUE;
    }

    synchronized public void agentTaskFinished(long agentTaskTimeDuration) {
        totalAgentTasksTime+= agentTaskTimeDuration;
        bruteForceUIAdapter.updateTotalProcessedAgentTasks(++currentTaskSize);
        totalAgentTasksAverageTime = totalAgentTasksTime / currentTaskSize ;
        bruteForceUIAdapter.updateAverageTaskTime(totalAgentTasksAverageTime);
        bruteForceUIAdapter.updateMissionTotalTime(totalAgentTasksTime);
        updateProgress(currentTaskSize, totalTaskSize);
    }
}
