package EnigmaMachineException;

import EnigmaMachine.RomanNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectorSettingsException extends Exception{
    private Integer errorIndex;
    private final String startingMessage = "Error: Failed to initialize the reflector settings because of the following reasons:" + System.lineSeparator();
    private boolean isExceptionNeedToBeThrown;
    private final List<Exception> exceptions;

    public ReflectorSettingsException() {
        this.isExceptionNeedToBeThrown = false;
        this.errorIndex = 1;
        this.exceptions = new ArrayList<>();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + System.lineSeparator()
                + startingMessage + exceptions.stream().map(Throwable::getMessage);
    }

    public boolean isExceptionNeedToThrown() {
        return exceptions.size() > 0;
    }

    private void addException(Exception exceptionToAdd) {
        isExceptionNeedToBeThrown = true;
        exceptions.add(exceptionToAdd);
        errorIndex++;
    }

    public void addIllegalReflectorsSize(int reflectorsSize) {
        addException(new IllegalArgumentException(errorIndex.toString()
        + ": There amount of reflectors that was inserted is illegal" + System.lineSeparator()
        + "The illegal amount was inserted is : " + reflectorsSize + System.lineSeparator()
        + "The amount that need to insert is : 1" + System.lineSeparator()));
    }

    public void addIllegalReflectorId(RomanNumber illegalReflectorId, Set<RomanNumber> allReflectorsId) {
        addException(new IllegalArgumentException(errorIndex.toString()
        + ": Illegal reflector id was inserted" + System.lineSeparator()
        + "The illegal reflector id that was inserted is: " + illegalReflectorId + System.lineSeparator()
        + "The possible reflectors id that can be insert are:" + System.lineSeparator()
        + allReflectorsId + System.lineSeparator()));
    }
}
