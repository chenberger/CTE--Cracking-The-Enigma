package BruteForce;

import DTO.BruteForceTask;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import BruteForce.AgentThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import EnigmaMachine.Reflector;
import EnigmaMachine.Rotor;
import EnigmaMachine.Settings.RotorIDSector;
import EnigmaMachine.Settings.StartingRotorPositionSector;

public class TasksManager implements Runnable{

    private ExecutorService TasksPool;
    private Integer totalTasksSize;
    private ExecutorService outputTasksPool;
    private BruteForceUIAdapter bruteForceUIAdapter;
    private String encryptedString;
    private Dictionary dictionary;
    private Integer amountOfAgents;
    private List<Character> startingRotorsPositions;
    private Integer taskSize;
    private EnigmaMachine enigmaMachine;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;

    public TasksManager(EnigmaMachine enigmaMachine, String encryptedString, BruteForceTask bruteForceTask,BruteForceUIAdapter UIAdapter, Dictionary dictionary) throws Exception {
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = bruteForceTask.getDifficultTaskLevel();
        this.encryptedString = encryptedString;
        this.bruteForceUIAdapter = UIAdapter;
        this.amountOfAgents = bruteForceTask.getAmountOfAgents();
        this.taskSize = bruteForceTask.getTaskSize();
        this.dictionary = dictionary;
        this.totalTasksSize = 0;
        this.startingRotorsPositions = setAllRotorsToFirstLetterAtStart();
        this.TasksPool = new ThreadPoolExecutor(2, 50, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new AgentThreadFactory());
        this.outputTasksPool = new ThreadPoolExecutor(2, 50, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    private List<Character> setAllRotorsToFirstLetterAtStart() {
        List<Character> startingRotorsPositions = new ArrayList<>();
        for (int i = 0; i < enigmaMachine.getNumOfActiveRotors(); i++) {
            startingRotorsPositions.add(enigmaMachine.getKeyboard().getFirstCharacter());
        }
        return startingRotorsPositions;
    }


    public void setImpossibleTasks() throws Exception {
        totalTasksSize*= binomial(enigmaMachine.getNumOfActiveRotors(), enigmaMachine.getAllRotors().size());
        List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors = getAllPossibleRotorsCombinationsFromAllPossibleRotorsExist();
        for (List<Integer> rotorCombination : allPossibleRotorsCombinationsFromAllRotors) {
            enigmaMachine.setRotorsInUseSettings((RotorIDSector) rotorCombination);
            setHardTasks();
            }
        }



    private List<List<Integer>> getAllPossibleRotorsCombinationsFromAllPossibleRotorsExist() {
        List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors = new ArrayList<>();
        List<Integer> allPossibleRotors = enigmaMachine.getAllRotors().entrySet().stream().map(rotor -> rotor.getKey()).collect(Collectors.toList());
        calculateAllPossibleRotorsIdsCombinations(allPossibleRotors, allPossibleRotorsCombinationsFromAllRotors, 0);
        return allPossibleRotorsCombinationsFromAllRotors;
    }

    private void calculateAllPossibleRotorsIdsCombinations(List<Integer> allPossibleRotors, List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors, int maxNumOfRotors) {
        for (int j = maxNumOfRotors; j < allPossibleRotors.size(); j++) {
            List<Integer> currentCombination = new ArrayList<>();
            if(!currentCombination.contains(allPossibleRotors.get(j))) {
                currentCombination.add(allPossibleRotors.get(j));
                if(currentCombination.size() == enigmaMachine.getNumOfActiveRotors()) {
                    allPossibleRotorsCombinationsFromAllRotors.add(currentCombination);
                }
                else {
                    calculateAllPossibleRotorsIdsCombinations(allPossibleRotors, allPossibleRotorsCombinationsFromAllRotors, j + 1);
                }
            }
        }
    }


    public void setHardTasks() throws Exception {
       List<List<Integer>> allPossibleRotorsCombinationsFromCurrentRotors = getAllPossibleRotorsCombinations();
       for(List<Integer> rotorsCombination : allPossibleRotorsCombinationsFromCurrentRotors) {
           enigmaMachine.setRotorsInUseSettings((RotorIDSector) rotorsCombination);
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
        totalTasksSize *= factorial(currentRotorsInUse.size());
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

    public void setMediumTasks() throws Exception {
        totalTasksSize *= enigmaMachine.getAllReflectors().size();
        for (Reflector reflector : Reflector.class.getEnumConstants()) {
            enigmaMachine.setReflector(reflector);
            setEasyTasks();
        }
    }

    public void setEasyTasks() throws Exception {
        int numOfPossibleRotorsPositions = (int) Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors());
        totalTasksSize = numOfPossibleRotorsPositions;
        StartingRotorPositionSector currentStartingRotorsPositions = new StartingRotorPositionSector(startingRotorsPositions);
        while(numOfPossibleRotorsPositions > 0) {
            //Agent agent = new Agent
            //TasksPool.execute(agent);
            //TODO: implement the thread pool of the agentas
            numOfPossibleRotorsPositions -= taskSize;
            currentStartingRotorsPositions.setElements(enigmaMachine.getKeyboard().increaseRotorPositions(currentStartingRotorsPositions.getElements(), taskSize));
        }

    }


    @Override
    public void run() {
        ExecutorService exService = new ThreadPoolExecutor(2, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(4), new AgentThreadFactory());
    }

}
