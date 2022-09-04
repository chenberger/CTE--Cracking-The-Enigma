package Decryption;

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

public class Agent extends Task<List<String>> {

    private final Integer id;
    private Integer taskSize;
    private Keyboard keyboard;
    private EnigmaMachine enigmaMachine;
    private StartingRotorPositionSector fromRotorPositions;
    private String encryptedString;
    private Dictionary dictionary;
    private long encryptionTimeDurationInMiliSeconds;

    private static int continuousId;

    public Agent(Integer taskSize, EnigmaMachine enigmaMachine, StartingRotorPositionSector fromRotorPositions, String encryptedString, Dictionary dictionary) {
        this.dictionary = dictionary;
        this.id = continuousId++;
        this.taskSize = taskSize;
        this.keyboard = enigmaMachine.getKeyboard();
        this.enigmaMachine = enigmaMachine;
        this.fromRotorPositions = fromRotorPositions;
        this.encryptedString = encryptedString;
    }

    static {
        resetContinuousId();
    }

    public static void resetContinuousId() {
        continuousId = 1;
    }

    @Override
    protected List<String> call() throws Exception {
        Instant startingTime = Instant.now();
        List<String> decryptStrings = new ArrayList<>();
        StartingRotorPositionSector currentRotorPositions = new StartingRotorPositionSector(fromRotorPositions.getElements());

        for (int i = 0; i < taskSize; i++) {
            try {
                validateAndSetStartingRotorPositions((StartingRotorPositionSector) currentRotorPositions.clone());

                String currentCodeConfigurationFormat = enigmaMachine.getCurrentSettingsFormat().toString();
                String candidateMessage = enigmaMachine.processedInput(encryptedString);

                try {
                    dictionary.validateWords(Arrays.asList(candidateMessage.split(" ")));
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

        //TODO erez : notify to ui
        encryptionTimeDurationInMiliSeconds = Duration.between(startingTime, Instant.now()).toMillis();
        return decryptStrings;
    }

    private void validateAndSetStartingRotorPositions(StartingRotorPositionSector currentRotorPositions) throws StartingPositionsOfTheRotorException, CloneNotSupportedException {
        currentRotorPositions.validateSector(enigmaMachine);
        currentRotorPositions.setSectorInTheMachine(enigmaMachine);
        enigmaMachine.resetSettings();
    }
}
