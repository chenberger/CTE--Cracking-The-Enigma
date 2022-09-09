package BruteForce;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Keyboard;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import EnigmaMachineException.WordNotValidInDictionaryException;
import javafx.concurrent.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  Agent extends Task<List<String>> {

    private final String id;
    private Keyboard keyboard;

    private long encryptionTimeDurationInNanoSeconds;
    private AgentTask agentTask;
    private EnigmaMachine enigmaMachine;
    private StartingRotorPositionSector startingRotorPosition;

    public Agent(AgentTask agentTask) {
        this.agentTask = agentTask;
        //Thread.currentThread().setName("1");
        this.id = (Thread.currentThread().getName());
        this.keyboard = agentTask.getKeyboard();
        this.enigmaMachine = agentTask.getEnigmaMachine();
        this.startingRotorPosition = agentTask.getStartingRotorPositions();
        System.out.println("Agent " + id + " created");//to check
    }

    @Override
    protected List<String> call() throws Exception {
        List<String> decryptStrings = new ArrayList<>();
        StartingRotorPositionSector currentRotorPositions = new StartingRotorPositionSector(startingRotorPosition.getElements());

        for (int i = 0; i < agentTask.getTaskSize(); i++) {
            System.out.println("Agent " + id + " is working");//to check
            try {
                Instant startingTime = Instant.now();
                validateAndSetStartingRotorPositions((StartingRotorPositionSector) currentRotorPositions.clone());

                String currentCodeConfigurationFormat = enigmaMachine.getCurrentSettingsFormat().toString();
                String candidateMessage = enigmaMachine.processedInput(agentTask.getEncryptedString().toUpperCase());

                try {
                    agentTask.validateWordsInDictionary(Arrays.asList(candidateMessage.split(" ")));
                    encryptionTimeDurationInNanoSeconds = Duration.between(startingTime, Instant.now()).toNanos();
                    agentTask.addDecryptionCandidateTaskToThreadPool(new DecryptionCandidateTaskHandler(agentTask.getBruteForceUIAdapter(),
                            new DecryptionCandidateFormat(candidateMessage, encryptionTimeDurationInNanoSeconds, id, currentCodeConfigurationFormat)));

                    decryptStrings.add(candidateMessage);
                } catch (WordNotValidInDictionaryException ignored) {}

                try {
                    currentRotorPositions.setElements(keyboard.increaseRotorPositions(currentRotorPositions.getElements()));
                }
                catch (Exception e) {
                    break;
                }
            } catch (StartingPositionsOfTheRotorException | CloneNotSupportedException ignored) {}
        }

        return decryptStrings;
    }

    private void validateAndSetStartingRotorPositions(StartingRotorPositionSector currentRotorPositions) throws StartingPositionsOfTheRotorException, CloneNotSupportedException {
        currentRotorPositions.validateSector(enigmaMachine);
        currentRotorPositions.setSectorInTheMachine(enigmaMachine);
        enigmaMachine.resetSettings();
    }
}
