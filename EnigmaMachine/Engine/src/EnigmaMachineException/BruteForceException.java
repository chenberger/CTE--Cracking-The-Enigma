package EnigmaMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BruteForceException extends Exception {
    private String startingMessage = "Error: Cannot decipher due to the following problem: " + System.lineSeparator();
    //private final List<Exception> exceptions;
    private Integer errorIndex;
    private final String EXCEPTION_INDENTATION = "       ";
    private final String INDEX_INDENTATION = "   ";

    public BruteForceException(String s) {
        startingMessage += s;
        errorIndex = 1;
    }

    //public boolean isExceptionNeedToBeThrown() {
    //    return exceptions.size() > 0;
    //}

    //private void addException(Exception exceptionToAdd) {
    //    exceptions.add(exceptionToAdd);
    //    errorIndex++;
    //}
    @Override
    public String getMessage() {
       return System.lineSeparator()
                + startingMessage;
    }

}

