package BruteForce;

import DesktopUserInterface.MainScene.BodyScene.BruteForce.AgentTaskData;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Keyboard;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.StartingPositionsOfTheRotorException;

import java.time.Duration;
import java.time.Instant;

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

    public Agent(AgentTask agentTask, TasksManager tasksManager) {
        this.agentTask = agentTask;
        //Thread.currentThread().setName("1");
        this.agentId = (Thread.currentThread().getName());
        this.keyboard = agentTask.getKeyboard();
        this.enigmaMachine = agentTask.getEnigmaMachine();
        this.startingRotorPosition = agentTask.getStartingRotorPositions();
        this.tasksManager = tasksManager;
        this.taskId = staticTaskId++;
    }

    static {
        resetTaskId();
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
            System.out.println("Agent " + agentId + " is working");//to check
            try {
                Instant startingTime = Instant.now();
                validateAndSetStartingRotorPositions((StartingRotorPositionSector) currentRotorPositions.clone());

                try {
                    String currentCodeConfigurationFormat = enigmaMachine.getCurrentSettingsFormat().toString();
                    String candidateMessage = enigmaMachine.processedInput(agentTask.getEncryptedString().toUpperCase());


                    //agentTask.validateWordsInDictionary(Arrays.asList(candidateMessage.split(" ")));
                    encryptionTimeDurationInNanoSeconds = Duration.between(startingTime, Instant.now()).toNanos();
                    synchronized (this) {
                        agentTask.addDecryptionCandidateTaskToThreadPool(new DecryptionCandidateTaskHandler(agentTask.getBruteForceUIAdapter(),
                                new AgentTaskData(taskId, agentId,
                                        new DecryptionCandidateFormat(candidateMessage, encryptionTimeDurationInNanoSeconds, currentCodeConfigurationFormat))));
                    }

                }
                catch (IllegalArgumentException ignored) {}

                try {
                    synchronized (this) {
                        currentRotorPositions.setElements(keyboard.increaseRotorPositions(currentRotorPositions.getElements()));
                    }
                }
                catch (Exception e) {
                    break;
                }
            }
            catch (StartingPositionsOfTheRotorException | CloneNotSupportedException ignored) {}
        }

        long totalTaskTime = Duration.between(startTaskTime, Instant.now()).toMillis();
        synchronized (this) {
            agentTask.getBruteForceUIAdapter().updateExistingAgentTaskTime(new AgentTaskData(taskId, totalTaskTime));
            tasksManager.agentTaskFinished(totalTaskTime);
        }
    }

    private void validateAndSetStartingRotorPositions(StartingRotorPositionSector currentRotorPositions) throws StartingPositionsOfTheRotorException, CloneNotSupportedException {
        currentRotorPositions.validateSector(enigmaMachine);
        currentRotorPositions.setSectorInTheMachine(enigmaMachine);
        enigmaMachine.resetSettings();
    }
}
