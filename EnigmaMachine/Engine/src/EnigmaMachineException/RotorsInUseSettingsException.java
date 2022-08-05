package EnigmaMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RotorsInUseSettingsException extends Exception{
    private Integer errorIndex;
    private final String startingMessage = "Error: Failed to initialize the rotors that need to be use in the machine settings because of the following reasons:" + System.lineSeparator();
    private boolean isExceptionNeedToBeThrown;
    private final List<Exception> exceptions;

    public RotorsInUseSettingsException() {
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

    public void addIllegalAmountOfRotors(int illegalRotorsAmount, int maximumRotorsAmount) {
        addException(new IllegalArgumentException(errorIndex.toString()
        + ": The amount of the rotors that was inserted is bigger then the maximum" + System.lineSeparator()
        + "The illegal amount that was inserted is : " + illegalRotorsAmount + System.lineSeparator()
        + "The maximum amount of rotors that can be insert is: " + maximumRotorsAmount + System.lineSeparator()));
    }

    public void addRotorIdDuplicates(Integer rotorId) {
        addException(new IllegalArgumentException(errorIndex.toString()
        + ": Rotor cannot insert more then one time" + System.lineSeparator()
        + "There rotor that was inserted more then one time is: " + rotorId + System.lineSeparator()));
    }

    public void addIllegalRotorId(Integer rotorId, Set<Integer> allRotorsId) {
        addException(new IllegalArgumentException(errorIndex.toString()
        + ": Illegal rotor id was inserted" + System.lineSeparator()
        + "The rotor id that was inserted is: "+ rotorId + System.lineSeparator()
        + "The rotors id that can be insert are: " + System.lineSeparator()
        + allRotorsId + System.lineSeparator()));
    }
}
