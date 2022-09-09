package BruteForce;

import DTO.BruteForceTask;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Reflector;
import EnigmaMachine.Rotor;
import EnigmaMachine.Settings.RotorIDSector;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TasksManager extends Task<Boolean> {
    //TODO chen : if the task size is bigger then the mission size, ceil it to the mission size.
    private static final int  MAX_QUEUE_SIZE = 1000;
    private ThreadPoolExecutor tasksPool;
    private DecipherStatistics decipherStatistics;
    private Double totalCombinations;
    private ExecutorService outputTasksPool;
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


    public TasksManager(EnigmaMachine enigmaMachine, String encryptedString, BruteForceTask bruteForceTask, BruteForceUIAdapter UIAdapter, Dictionary dictionary, ExecutorService candidatesPool, String decryptedMessege) throws Exception {
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = bruteForceTask.getDifficultTaskLevel();
        this.encryptedString = encryptedString;
        this.bruteForceUIAdapter = UIAdapter;
        this.amountOfAgents = bruteForceTask.getAmountOfAgents();
        this.taskSize = bruteForceTask.getTaskSize();
        this.dictionary = dictionary;
        this.startingRotorsPositions = setAllRotorsToFirstLetterAtStart();
        this.tasksPool = new ThreadPoolExecutor(amountOfAgents, Integer.MAX_VALUE, 5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(MAX_QUEUE_SIZE));
        tasksPool.setThreadFactory(new AgentThreadFactory());
        decipherStatistics = new DecipherStatistics();
        this.outputTasksPool = new ThreadPoolExecutor(2, 50, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        this.totalAgentTasksTime = 0;
        this.totalAgentTasksAverageTime = 0;

        calcMissionSize();
    }
    public void start() throws Exception {
        difficultyLevel.setTask(this);
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
            enigmaMachine.setRotorsInUseSettings(new RotorIDSector(rotorCombination));
            setHardTasks();
        }
    }



    private List<List<Integer>> getAllPossibleRotorsCombinationsFromAllPossibleRotorsExist() {
        List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors = new ArrayList<>();
        List<Integer> allPossibleRotors = new ArrayList<>();
        calculateAllPossibleRotorsIdsCombinations(enigmaMachine.getAllRotors().size(),1, enigmaMachine.getNumOfActiveRotors(), allPossibleRotors, allPossibleRotorsCombinationsFromAllRotors);
        return allPossibleRotorsCombinationsFromAllRotors;
    }

   // private void calculateAllPossibleRotorsIdsCombinations(List<Integer> allPossibleRotors, List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors, int maxNumOfRotors) {
   //     List<Integer> currentCombination = new ArrayList<>();
   //     for (int j = maxNumOfRotors; j < allPossibleRotors.size(); j++) {
   //         if(!currentCombination.contains(allPossibleRotors.get(j))) {
   //             currentCombination.add(allPossibleRotors.get(j));
   //             if(currentCombination.size() == enigmaMachine.getNumOfActiveRotors()) {
   //                 allPossibleRotorsCombinationsFromAllRotors.add(currentCombination);
   //             }
   //             else {
   //                 calculateAllPossibleRotorsIdsCombinations(allPossibleRotors, allPossibleRotorsCombinationsFromAllRotors, j + 1);
   //             }
   //         }
   //     }
   // }
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
           enigmaMachine.setRotorsInUseSettings(new RotorIDSector(rotorsCombination));
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

        totalTaskSize = (long) (totalCombinations / taskSize);
        currentTaskSize = 0;
        bruteForceUIAdapter.updateTotalAgentsTasks(totalTaskSize);
        bruteForceUIAdapter.updateTotalProcessedAgentTasks(currentTaskSize);

        updateProgress(currentTaskSize, totalTaskSize);
    }

    public void setMediumTasks() throws Exception {

        for (Reflector reflector : enigmaMachine.getAllReflectors().values()) {
            enigmaMachine.setReflector(reflector);
            setEasyTasks();
        }
    }

    public void setEasyTasks() throws Exception {
        boolean isFirstAgent = true;
        int numOfPossibleRotorsPositions = (int) Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors());
        StartingRotorPositionSector currentStartingRotorsPositions = new StartingRotorPositionSector(startingRotorsPositions);

        while(numOfPossibleRotorsPositions > 0) {
            bruteForceUIAdapter.updateTotalProcessedAgentTasks(++currentTaskSize);
            updateProgress(currentTaskSize, totalTaskSize);

            AgentTask agentTask = new AgentTask(taskSize, currentStartingRotorsPositions, enigmaMachine,encryptedString, dictionary, tasksPool, bruteForceUIAdapter, decipherStatistics);
            Agent agent = new Agent(agentTask, this);

            new Thread(agent).start();
           //if(isFirstAgent) {
           //    tasksPool.execute(agent);
           //    isFirstAgent = false;
           //}
            //tasksPool.getThreadFactory().newThread(agent).start();
            //tasksPool.getQueue().put(agent);

            numOfPossibleRotorsPositions -= taskSize;

            currentStartingRotorsPositions.setElements(enigmaMachine.getKeyboard().increaseRotorPositions(currentStartingRotorsPositions.getElements(), taskSize));
        }

    }
    @Override
    protected Boolean call() throws Exception {
        try {
            difficultyLevel.setTask(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Boolean.TRUE;
    }

    public void agentTaskFinished(long agentTaskTimeDuration) {
        totalAgentTasksTime+= agentTaskTimeDuration;
        totalAgentTasksAverageTime = totalAgentTasksTime / currentTaskSize;
        bruteForceUIAdapter.updateAverageTaskTime(totalAgentTasksAverageTime);
        bruteForceUIAdapter.updateMissionTotalTime(totalAgentTasksTime);
    }
}
