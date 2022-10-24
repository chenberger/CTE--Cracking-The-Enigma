package BruteForce;

import DTO.BruteForceTask;
import DTO.TaskToAgent;
import Engine.AlliesManager.Allie;
import Engine.Dictionary;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.*;
import EnigmaMachine.*;
import EnigmaMachineException.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TasksProducer implements Runnable {
    private boolean noMoreTasks;
    Allie allie;

    private BlockingQueue<TaskToAgent> tasksQueue;
    private static final int  MAX_QUEUE_SIZE = 1000;

    private Double totalCombinations;

    private String encryptedString;
    private Dictionary dictionary;
    private long taskSize;
    private List<Character> startingRotorsPositions;
    private EnigmaMachine enigmaMachine;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    private long totalTasksSize;
    private long currentTaskSize;
    private long numberOfTasksProduced;

    private SettingsFormat settingsFormat;
    private boolean contestIsOn;

    public TasksProducer(BruteForceTask bruteForceTask, String encryptedString, Dictionary dictionary, EnigmaMachine enigmaMachine, SettingsFormat settingsFormat, Allie allie) {
        this.tasksQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        this.encryptedString = encryptedString;
        this.dictionary = dictionary;
        this.difficultyLevel = bruteForceTask.getDifficultTaskLevel();
        this.enigmaMachine = enigmaMachine;
        this.settingsFormat = settingsFormat;
        this.startingRotorsPositions = setAllRotorsToFirstLetterAtStart();
        this.noMoreTasks = false;
        this.taskSize= bruteForceTask.getTaskSize();
        this.allie = allie;
        this.contestIsOn = false;
        this.numberOfTasksProduced = 0;
        initializeMachineSettings();
        calcMissionSize();
    }
    public boolean isNoMoreTasks() {
        return noMoreTasks;
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

    private List<Character> setAllRotorsToFirstLetterAtStart() {
        List<Character> startingRotorsPositions = new ArrayList<>();

        for (int i = 0; i < enigmaMachine.getNumOfActiveRotors(); i++) {
            startingRotorsPositions.add(enigmaMachine.getKeyboard().getFirstCharacter());
        }
        return startingRotorsPositions;
    }


    public void setImpossibleTasks() throws Exception {
        if(!noMoreTasks) {
            List<List<Integer>> allPossibleRotorsCombinationsFromAllRotors = getAllPossibleRotorsCombinationsFromAllPossibleRotorsExist();

            for (List<Integer> rotorCombination : allPossibleRotorsCombinationsFromAllRotors) {
                RotorIDSector rotorIDSector = new RotorIDSector(rotorCombination);
                setSectorInMachine(rotorIDSector);
                setHardTasks();
            }
            noMoreTasks = true;
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

        // I iterate from left to n. First time
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
        if(!noMoreTasks) {
            List<List<Integer>> allPossibleRotorsCombinationsFromCurrentRotors = getAllPossibleRotorsCombinations();
            for (List<Integer> rotorsCombination : allPossibleRotorsCombinationsFromCurrentRotors) {
                RotorIDSector rotorIDSector = new RotorIDSector(rotorsCombination);
                setSectorInMachine(rotorIDSector);
                setMediumTasks();
            }
            if (difficultyLevel.equals(DifficultyLevel.HARD)) {
                noMoreTasks = true;
            }
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
            totalTasksSize = (long) (totalCombinations / Math.pow(enigmaMachine.getKeyboard().size(), enigmaMachine.getNumOfActiveRotors()));
        }
        else {
            totalTasksSize = (long) (totalCombinations / taskSize);
        }
        allie.setTotalNumberOfTasks(totalTasksSize);
    }

    public void setMediumTasks() throws Exception {
        if(!noMoreTasks) {
            for (Reflector reflector : enigmaMachine.getAllReflectors().values()) {
                ReflectorIdSector reflectorIdSector = new ReflectorIdSector(Arrays.asList(reflector.id()));
                setSectorInMachine(reflectorIdSector);
                setEasyTasks();
            }
            if (difficultyLevel.equals(DifficultyLevel.MEDIUM)) {
                noMoreTasks = true;
            }
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

        while(numOfPossibleRotorsPositions > 0 && contestIsOn) {
            //if (isCancelled()) {
//
            //    throw new TaskIsCanceledException();
            //}

            if(!noMoreTasks) {
                EnigmaMachine clonedEnigmaMachine = enigmaMachine.cloneMachine();
                TaskToAgent agentTask = new TaskToAgent(taskSize, (StartingRotorPositionSector) currentStartingRotorsPositions.clone(), clonedEnigmaMachine, encryptedString);

                tasksQueue.put(agentTask);
            }

            numOfPossibleRotorsPositions -= taskSize;
            if (numOfPossibleRotorsPositions <= 0 && difficultyLevel.equals(DifficultyLevel.EASY)) {
                noMoreTasks = true;
            }
            try {
                currentStartingRotorsPositions.setElements(enigmaMachine.getKeyboard().increaseRotorPositions(currentStartingRotorsPositions.getElements(), (int) taskSize));
                if(!noMoreTasks) {
                    ++numberOfTasksProduced;
                    allie.increaseTasksProduced(numberOfTasksProduced);
                }
            }
            catch (Exception e) {
                break;
            }
        }

    }
    public BlockingQueue<TaskToAgent> getTasksQueue() {
        return tasksQueue;
    }


    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                difficultyLevel.setTask(this);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Producer is done");
    }
    public void setContestIsOn(){
        contestIsOn = true;
    }
    public void setContestIsOff(){
        contestIsOn = false;
    }

}
