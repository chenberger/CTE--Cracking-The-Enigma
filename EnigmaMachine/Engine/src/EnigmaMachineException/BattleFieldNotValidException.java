package EnigmaMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BattleFieldNotValidException extends Exception{
    private String startingMessage = "Error: There are problems with your battlefield which are: " + System.lineSeparator();
    private final List<Exception> exceptions;
    private Integer errorIndex;
    private final String EXCEPTION_INDENTATION = "       ";
    private final String INDEX_INDENTATION = "   ";
    @Override
    public String getMessage() {
        return System.lineSeparator() + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }

    public BattleFieldNotValidException() {
        exceptions = new ArrayList<>();
        errorIndex = 1;
    }
    public boolean shouldThrowException() {
        return exceptions.size() > 0;
    }
    // add exception to the list of exceptions region
    public void addIllegalNumberOfAlliesException() {
        exceptions.add(new Exception(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": The number of allies must be greater then 0!!!! "+ System.lineSeparator()));
        errorIndex++;
    }
    public void addIllegalBattleFieldNameException() {
        exceptions.add(new Exception(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": The battle field name must be at least 1 character long!!!! "+ System.lineSeparator()));
        errorIndex++;
    }
    public void addIllegalLevelException() {
        exceptions.add(new Exception(EXCEPTION_INDENTATION + errorIndex.toString()
                + ": The level must be one of the following: EASY, MEDIUM, HARD, IMPOSSIBLE!!!! "+ System.lineSeparator()));
        errorIndex++;
    }

}
