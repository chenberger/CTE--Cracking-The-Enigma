package BruteForce;

import Engine.Dictionary;
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
import java.util.concurrent.*;

public class  Agent extends Task<List<String>> {

    private final Instant id;
    private Integer taskSize;
    private Keyboard keyboard;
    private EnigmaMachine enigmaMachine;
    private StartingRotorPositionSector fromRotorPositions;
    private String encryptedString;
    private Dictionary dictionary;
    private long encryptionTimeDurationInMiliSeconds;
    private BlockingQueue candidateMessagesQueue;

    public Agent(Integer taskSize, EnigmaMachine enigmaMachine, StartingRotorPositionSector fromRotorPositions, String encryptedString, Dictionary dictionary, BlockingQueue candidateMessagesQueue) {
        this.dictionary = dictionary;
        this.candidateMessagesQueue = candidateMessagesQueue;
        this.id = Instant.parse(Thread.currentThread().getName());
        this.taskSize = taskSize;
        this.keyboard = enigmaMachine.getKeyboard();
        this.enigmaMachine = enigmaMachine;
        this.fromRotorPositions = fromRotorPositions;
        this.encryptedString = encryptedString;
    }

    @Override
    protected List<String> call() throws Exception {
        List<String> decryptStrings = new ArrayList<>();
        StartingRotorPositionSector currentRotorPositions = new StartingRotorPositionSector(fromRotorPositions.getElements());

        for (int i = 0; i < taskSize; i++) {
            try {
                Instant startingTime = Instant.now();
                validateAndSetStartingRotorPositions((StartingRotorPositionSector) currentRotorPositions.clone());

                String currentCodeConfigurationFormat = enigmaMachine.getCurrentSettingsFormat().toString();
                String candidateMessage = enigmaMachine.processedInput(encryptedString);

                try {
                    dictionary.validateWords(Arrays.asList(candidateMessage.split(" ")));
                    encryptionTimeDurationInMiliSeconds = Duration.between(startingTime, Instant.now()).toMillis();
                    candidateMessagesQueue.put();
                    //TODO erez: add to blocking queue
                    decryptStrings.add(candidateMessage);
                } catch (WordNotValidInDictionaryException ignored) {}

                try {
                    currentRotorPositions.setElements(keyboard.increase(currentRotorPositions.getElements()));
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



        ExecutorService exService = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(4));


    }
}
