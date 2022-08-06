package EnigmaMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RotorsInUseSettingsException extends Exception{
    private final String EXCEPTION_IDENTATION = "       ";
    private final String INDEX_IDENTATION = "   ";
    private Integer errorIndex;
    private final String startingMessage = "Error: Failed to initialize the rotors that need to be use in the machine settings because of the following reasons:" + System.lineSeparator();
    private final List<Exception> exceptions;

    public RotorsInUseSettingsException() {
        this.errorIndex = 1;
        this.exceptions = new ArrayList<>();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + System.lineSeparator()
                + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }

    public boolean isExceptionNeedToThrown() {
        return exceptions.size() > 0;
    }

    private void addException(Exception exceptionToAdd) {
        exceptions.add(exceptionToAdd);
        errorIndex++;
    }

    public void addIllegalAmountOfRotors(int illegalRotorsAmount, int maximumRotorsAmount) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": The amount of the rotors that was inserted is bigger then the maximum" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The illegal amount that was inserted is : " + illegalRotorsAmount + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The maximum amount of rotors that can be insert is: " + maximumRotorsAmount + System.lineSeparator()));
    }

    public void addRotorIdDuplicates(Integer rotorId) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": Rotor cannot insert more then one time" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "There rotor that was inserted more then one time is: " + rotorId + System.lineSeparator()));
    }

    public void addIllegalRotorId(Integer rotorId, Set<Integer> allRotorsId) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": Illegal rotor id was inserted" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The rotor id that was inserted is: "+ rotorId + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The rotors id that can be insert are: " + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + allRotorsId + System.lineSeparator()));
    }
}
