package EnigmaMachineException;

import java.util.*;
import java.util.stream.Collectors;

public class GeneralEnigmaMachineException extends Exception {
    private final String startingMessage = "Hello, you have the following errors in the xml file: " + System.lineSeparator();
    List<Exception> XmlExceptionList = new ArrayList<>();
    public void addException(Exception exceptionToAdd) {
        XmlExceptionList.add(exceptionToAdd);
    }
    public boolean isExceptionNeedToThrown() {
        return XmlExceptionList.size() > 0;
    }
    @Override
    public String getMessage() {
        return System.lineSeparator() + startingMessage + XmlExceptionList.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }

    public boolean shouldExceptionBeThrown() {
        return XmlExceptionList.size() > 0;
    }

    public boolean shouldThrowException() {
        return XmlExceptionList.size() > 0;
    }
}