package Engine.AgentsManager;

import DTO.AgentCandidatesInformation;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Keyboard;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import EnigmaMachineException.WordNotValidInDictionaryException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AgentWorker implements Runnable{
    private List<AgentCandidatesInformation> agentCandidatesInformationList;
    CountDownLatch countDownLatch;
    private long numberOfTasksPulled;
    private final String agentId;
    private final String agentName;
    private Keyboard keyboard;

    private long encryptionTimeDurationInNanoSeconds;
    private AgentTask agentTask;
    private EnigmaMachine enigmaMachine;
    private StartingRotorPositionSector startingRotorPosition;
    private static int staticTaskId;

    private int taskId;

    private String AllieName;

    public AgentWorker(AgentTask agentTask, String AgentName, long numberOfTasksPulled, List<AgentCandidatesInformation> agentCandidatesInformationList, CountDownLatch countDownLatch) {
        synchronized (this) {
            this.agentTask = agentTask;
            this.agentId = Thread.currentThread().getName();
            this.keyboard = agentTask.getKeyboard();
            this.enigmaMachine = agentTask.getEnigmaMachine();
            this.enigmaMachine.setTheInitialCodeDefined(true);
            this.startingRotorPosition = agentTask.getStartingRotorPositions();
            this.agentName = agentTask.getAgentName();
            this.taskId = staticTaskId++;
            this.numberOfTasksPulled = numberOfTasksPulled;
            this.agentCandidatesInformationList = agentCandidatesInformationList;
            this.countDownLatch = countDownLatch;

        }
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
        //Instant startTaskTime = Instant.now();
        StartingRotorPositionSector currentRotorPositions = new StartingRotorPositionSector(startingRotorPosition.getElements());

        for (int i = 0; i < agentTask.getTaskSize(); i++) {
            try {
                //Instant startingTime = Instant.now();

                validateAndSetStartingRotorPositions((StartingRotorPositionSector) currentRotorPositions.clone());

                String currentCodeConfigurationFormat = enigmaMachine.getCurrentSettingsFormat().toString();
                String candidateMessage = enigmaMachine.processedInput(agentTask.getEncryptedString().toUpperCase());
                System.out.println(currentRotorPositions.toString() + " " + candidateMessage);
                //System.out.println("Agent id: " + Thread.currentThread().getName() + " code: " + currentCodeConfigurationFormat + "candidate: " + candidateMessage);

                try {

                    agentTask.validateWordsInDictionary(Arrays.asList(candidateMessage.split(" ")));
                    System.out.println("Agent id: " + Thread.currentThread().getName() + " code: " + currentCodeConfigurationFormat + "candidate: " + candidateMessage);

                    AgentCandidatesInformation agentCandidatesInformation = getAgentCandidatesInformation(currentCodeConfigurationFormat, candidateMessage);
                    agentCandidatesInformationList.add(agentCandidatesInformation);
                    //System.out.println(candidateMessage + ": Agent " + Thread.currentThread().getName() + " " + enigmaMachine.getCurrentSettingsFormat().toString());//to check
                    //encryptionTimeDurationInNanoSeconds = Duration.between(startingTime, Instant.now()).toNanos();
                    //agentTask.addDecryptionCandidateTaskToThreadPool(new DecryptionCandidateTaskHandler(agentTask.getBruteForceUIAdapter(),
                          //  new AgentTaskData(taskId, Thread.currentThread().getName(),
                                 //   new DecryptionCandidateFormat(candidateMessage, encryptionTimeDurationInNanoSeconds, currentCodeConfigurationFormat))));
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
        //TODO: add all the candidate information into list and send it to the Server in the end of the task.
        //long totalTaskTime = Duration.between(startTaskTime, Instant.now()).toMillis();
        countDownLatch.countDown();
    }

    private AgentCandidatesInformation getAgentCandidatesInformation(String currentCodeConfigurationFormat, String candidateString) {
        return new AgentCandidatesInformation(candidateString, taskId,
                currentCodeConfigurationFormat, agentName);
    }

    private void validateAndSetStartingRotorPositions(StartingRotorPositionSector currentRotorPositions) throws StartingPositionsOfTheRotorException, CloneNotSupportedException {
        currentRotorPositions.validateSector(enigmaMachine);
        currentRotorPositions.setSectorInTheMachine(enigmaMachine);
        currentRotorPositions.addSectorToSettingsFormat(enigmaMachine);
        enigmaMachine.resetSettings();
    }
}
