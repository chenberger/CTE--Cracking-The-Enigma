package EnigmaMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecryptionManagerSettingsException  extends Exception{
    private final String startingMessage = "Error: The following settings cannot be set to the decryption manager" + System.lineSeparator();
    private final List<Exception> exceptions;
    private Integer errorIndex;
    private final String EXCEPTION_INDENTATION = "       ";
    private final String INDEX_INDENTATION = "   ";

    public DecryptionManagerSettingsException() {
        exceptions = new ArrayList<>();
        errorIndex = 1;
    }

    public boolean isExceptionNeedToBeThrown() {
        return exceptions.size() > 0;
    }

    private void addException(Exception exceptionToAdd) {
        exceptions.add(exceptionToAdd);
        errorIndex++;
    }

    public void addIllegalAgentsAmount(Integer agentsAmount, int minAgentsAmount, double maxAgentsAmount) {
        addException(new IllegalArgumentException(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": There amount of agents that was inserted is illegal" + System.lineSeparator()
                + EXCEPTION_INDENTATION + INDEX_INDENTATION + "The illegal amount was inserted is : " + agentsAmount + System.lineSeparator()
                + EXCEPTION_INDENTATION + INDEX_INDENTATION + "The amount that need to insert is between " + minAgentsAmount + " to " + maxAgentsAmount + System.lineSeparator()));

    }

    @Override
    public String getMessage() {
        return System.lineSeparator()
                + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }

    public void addFailedParseAgentsAmountToInt(String agentsAMount) {
        addException(new IllegalArgumentException(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": There amount of agents that was inserted is not an integer" + System.lineSeparator()
                + EXCEPTION_INDENTATION + INDEX_INDENTATION + "The illegal amount that was inserted is : " + agentsAMount + System.lineSeparator()));
    }

    public void addIgelEmptyDifficultyLevel() {
        addException(new IllegalArgumentException(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": There is no any difficulty level was inserted" + System.lineSeparator()));
    }

    public void addIllegalTaskSize(Integer taskSize) {
        addException(new IllegalArgumentException(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": The task size that was inserted is illegal" + System.lineSeparator()
                + EXCEPTION_INDENTATION + INDEX_INDENTATION + "The illegal amount was inserted is : " + taskSize + System.lineSeparator()
                + EXCEPTION_INDENTATION + INDEX_INDENTATION + "The size that need to insert is must be integer and greater than 0" + System.lineSeparator()));
    }

    public void addFailedParseTaskSizeToInt(String taskSize) {
        addException(new IllegalArgumentException(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": The task size that was inserted is not an integer" + System.lineSeparator()
                + EXCEPTION_INDENTATION + INDEX_INDENTATION + "The illegal size that was inserted is : " + taskSize + System.lineSeparator()));
    }
}
