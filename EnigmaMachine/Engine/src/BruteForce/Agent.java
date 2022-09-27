package BruteForce;

import DesktopUserInterface.MainScene.BodyScene.BruteForce.AgentTaskData;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Keyboard;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import EnigmaMachineException.WordNotValidInDictionaryException;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class  Agent implements Runnable {

    private final String agentId;
    private Keyboard keyboard;

    private long encryptionTimeDurationInNanoSeconds;
    private AgentTask agentTask;
    private EnigmaMachine enigmaMachine;
    private StartingRotorPositionSector startingRotorPosition;
    private static int staticTaskId;
    private TasksManager tasksManager;
    private int taskId;

    private String AllieName;

    public Agent(AgentTask agentTask, TasksManager tasksManager) {
        synchronized (this) {
            this.agentTask = agentTask;
            this.agentId = Thread.currentThread().getName();
            this.keyboard = agentTask.getKeyboard();
            this.enigmaMachine = agentTask.getEnigmaMachine();
            this.startingRotorPosition = agentTask.getStartingRotorPositions();
            this.tasksManager = tasksManager;
            this.taskId = staticTaskId++;
        }
    }

    static {
        resetTaskId();
    }
    public void setAllieName(String allieName) {
        AllieName = allieName;
    }
    private static void resetTaskId() {
        staticTaskId = 1;
    }

    public static int getStaticTaskId() {
        return staticTaskId;

    }
    @Override
    public void run() {
        Instant startTaskTime = Instant.now();
        StartingRotorPositionSector currentRotorPositions = new StartingRotorPositionSector(startingRotorPosition.getElements());

        for (int i = 0; i < agentTask.getTaskSize(); i++) {
            try {
                Instant startingTime = Instant.now();

                validateAndSetStartingRotorPositions((StartingRotorPositionSector) currentRotorPositions.clone());

                String currentCodeConfigurationFormat = enigmaMachine.getCurrentSettingsFormat().toString();
                String candidateMessage = enigmaMachine.processedInput(agentTask.getEncryptedString().toUpperCase());

                //System.out.println("Agent id: " + Thread.currentThread().getName() + " code: " + currentCodeConfigurationFormat + "candidate: " + candidateMessage);

                try {

                    agentTask.validateWordsInDictionary(Arrays.asList(candidateMessage.split(" ")));
                    //System.out.println(candidateMessage + ": Agent " + Thread.currentThread().getName() + " " + enigmaMachine.getCurrentSettingsFormat().toString());//to check
                    encryptionTimeDurationInNanoSeconds = Duration.between(startingTime, Instant.now()).toNanos();
                    agentTask.addDecryptionCandidateTaskToThreadPool(new DecryptionCandidateTaskHandler(agentTask.getBruteForceUIAdapter(),
                                                                     new AgentTaskData(taskId, Thread.currentThread().getName(),
                                                                     new DecryptionCandidateFormat(candidateMessage, encryptionTimeDurationInNanoSeconds, currentCodeConfigurationFormat))));
                }
                catch (WordNotValidInDictionaryException ignored) {}

                try {
                    currentRotorPositions.setElements(keyboard.increaseRotorPositions(currentRotorPositions.getElements()));
                }
                catch (Exception e) {
                    break;
                }
            }
            catch (CloneNotSupportedException | StartingPositionsOfTheRotorException ignored) { }
        }

        long totalTaskTime = Duration.between(startTaskTime, Instant.now()).toMillis();
        agentTask.getBruteForceUIAdapter().updateExistingAgentTaskTime(new AgentTaskData(taskId, totalTaskTime));
        tasksManager.agentTaskFinished(totalTaskTime);

    }

    private void validateAndSetStartingRotorPositions(StartingRotorPositionSector currentRotorPositions) throws StartingPositionsOfTheRotorException, CloneNotSupportedException {
        currentRotorPositions.validateSector(enigmaMachine);
        currentRotorPositions.setSectorInTheMachine(enigmaMachine);
        currentRotorPositions.addSectorToSettingsFormat(enigmaMachine);
        enigmaMachine.resetSettings();
    }
}

