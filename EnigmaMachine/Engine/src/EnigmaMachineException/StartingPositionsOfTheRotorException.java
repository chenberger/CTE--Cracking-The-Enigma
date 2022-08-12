package EnigmaMachineException;

import EnigmaMachine.RomanNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StartingPositionsOfTheRotorException extends Exception{
    private final String EXCEPTION_IDENTATION = "       ";
    private final String INDEX_IDENTATION = "   ";
    private Integer errorIndex;
    private final String startingMessage = "Error: Failed to initialize the starting positions of the rotors settings because of the following reasons:" + System.lineSeparator();
    private final List<Exception> exceptions;

    public StartingPositionsOfTheRotorException() {
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


    public void addIllegalPositionsSize(int positionsSize, int rotorsSize) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": There amount of the starting positions for the rotors that was inserted is not matching the amount of the rotors inserted" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The amount of starting positions that was inserted is: " + positionsSize + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The amount of rotors that was inserted is: " + rotorsSize + System.lineSeparator()));
    }

    public void addIllegalCharacter(Character illegalStartingRightCharToWindow, Set<Character> keyboardCharacters) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": The character that was inserted is not matching to any char from the keyboard characters" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The illegal character that was inserted is: " + illegalStartingRightCharToWindow + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The possible characters that can be used are:" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + keyboardCharacters + System.lineSeparator()));
    }
}
