package EnigmaMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginBoardSettingsException extends Exception{
    private final String EXCEPTION_IDENTATION = "       ";
    private final String INDEX_IDENTATION = "   ";
    private Integer errorIndex;
    private final String startingMessage = "Error: Failed to initialize the plugin board settings because of the following reasons:" + System.lineSeparator();
    private final List<Exception> exceptions;

    public PluginBoardSettingsException() {
        this.errorIndex = 1;
        this.exceptions = new ArrayList<>();
    }


    public void addIllegalPairsSize(int pluggedPairsSize, int maximumPairs) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                + ": The amount of pairs that was inserted is higher then the maximum!" + System.lineSeparator()
                + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The amount of pairs was inserted is: "
                + pluggedPairsSize + System.lineSeparator()
                + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The max amount of pairs is: "
                + maximumPairs + System.lineSeparator()));
    }

    public void addValuePluggedToHimself(Character character) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
                + ": The character " + character + " cannot plugged to himself" + System.lineSeparator()));
    }

    public void clear() {
        exceptions.clear();
    }

    public void addValuePluggedToMoreThenOneChar(Character character) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": The character " + character + " cannot plugged to more then one character" + System.lineSeparator()));
    }

    public void addIllegalCharNotFromTheKeyboard(Character character, Set<Character> keyboardCharacters) {
        addException(new IllegalArgumentException(EXCEPTION_IDENTATION + errorIndex.toString()
        + ": The character " + character + " is illegal and not from the characters keyboard" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + "The possible characters that can be used are:" + System.lineSeparator()
        + EXCEPTION_IDENTATION + INDEX_IDENTATION + keyboardCharacters + System.lineSeparator()));
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
}
