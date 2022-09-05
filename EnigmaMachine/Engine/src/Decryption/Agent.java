package Decryption;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Keyboard;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.StartingPositionsOfTheRotorException;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class  Agent extends Task<List<String>> {

    private final Integer id;
    private Integer taskSize;
    private Keyboard keyboard;
    private EnigmaMachine enigmaMachine;
    private StartingRotorPositionSector fromRotorPositions;
    private String encryptedString;
    private long encryptionTimeDurationInNanoSeconds;

    private static int continuousId;

    public Agent(Integer taskSize, EnigmaMachine enigmaMachine, StartingRotorPositionSector fromRotorPositions, String encryptedString) {
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
        List<String> decryptStrings = new ArrayList<>();
        StartingRotorPositionSector currentRotorPositions = new StartingRotorPositionSector(fromRotorPositions.getElements());

        for (int i = 0; i < taskSize; i++) {
            try {
                validateAndSetStartingRotorPositions((StartingRotorPositionSector) currentRotorPositions.clone());
            } catch (StartingPositionsOfTheRotorException | CloneNotSupportedException ignored) {
            }

            String currentCodeConfigurationFormat = enigmaMachine.getCurrentSettingsFormat().toString();
            String candidateMessage = enigmaMachine.processedInput(encryptedString);
            decryptStrings.add(candidateMessage);

            try {
                currentRotorPositions.setElements(keyboard.increase(currentRotorPositions.getElements()));
            }
            catch (Exception e) {
                break;
            }
        }
        return decryptStrings;
    }

    private void validateAndSetStartingRotorPositions(StartingRotorPositionSector currentRotorPositions) throws StartingPositionsOfTheRotorException, CloneNotSupportedException {
        currentRotorPositions.validateSector(enigmaMachine);
        currentRotorPositions.setSectorInTheMachine(enigmaMachine);
        enigmaMachine.resetSettings();
    }
}
