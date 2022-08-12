package EnigmaMachineException;

import EnigmaMachine.RomanNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectorSettingsException extends Exception{
    private final String EXCEPTION_IDENTATION = "       ";
    private final String INDEX_IDENTATION = "   ";
    private Integer errorIndex;
    private final String startingMessage = "Error: Failed to initialize the reflector settings because of the following reasons:" + System.lineSeparator();
    private final List<Exception> exceptions;

    public ReflectorSettingsException() {
        this.errorIndex = 1;
        this.exceptions = new ArrayList<>();
    }

    @Override
    public String getMessage() {
        return System.lineSeparator()
                + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }

    public boolean isExceptionNeedToThrown() {
        return exceptions.size() > 0;
    }

    private void addException(Exception exceptionToAdd) {
        exceptions.add(exceptionToAdd);
        errorIndex++;
    }

    public void addIllegalReflectorsSize(int reflectorsSize) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": There amount of reflectors that was inserted is illegal" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The illegal amount was inserted is : " + reflectorsSize + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The amount that need to insert is : 1" + System.lineSeparator()));
    }

    public void addIllegalReflectorId(RomanNumber illegalReflectorId, Set<RomanNumber> allReflectorsId) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": Illegal reflector id was inserted" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The illegal reflector id that was inserted is: " + illegalReflectorId + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The possible reflectors id that can be insert are:" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + allReflectorsId + System.lineSeparator()));
    }
}
