package EnigmaMachineException;

import EnigmaMachine.RomanNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StartingPositionsOfTheRotorException extends Exception{
    private Integer errorIndex;
    private final String startingMessage = "Error: Failed to initialize the starting positions of the rotors settings because of the following reasons:" + System.lineSeparator();
    private boolean isExceptionNeedToBeThrown;
    private final List<Exception> exceptions;

    public StartingPositionsOfTheRotorException() {
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


    public void addIllegalPositionsSize(int positionsSize, int rotorsSize) {
        addException(new IllegalArgumentException(errorIndex.toString()
        + ": There amount of the starting positions for the rotors that was inserted is not matching the amount of the rotors inserted" + System.lineSeparator()
        + "The illegal starting positions that was inserted is: " + positionsSize + System.lineSeparator()
        + "The amount of rotors that was inserted is: " + rotorsSize + System.lineSeparator()));
    }

    public void addIllegalCharacter(Character illegalStartingRightCharToWindow, Set<Character> keyboardCharacters) {
        addException(new IllegalArgumentException(errorIndex.toString()
        + ": The character that was inserted is not matching to any char from the keyboard characters" + System.lineSeparator()
        + "The illegal character that was inserted is: " + illegalStartingRightCharToWindow + System.lineSeparator()
        + "The possible characters that can be used are:" + System.lineSeparator()
        + keyboardCharacters + System.lineSeparator()));
    }
}
