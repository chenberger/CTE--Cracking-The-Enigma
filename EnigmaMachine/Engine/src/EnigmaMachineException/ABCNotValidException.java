package EnigmaMachineException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ABCNotValidException extends Exception {

    private final String EXCEPTION_IDENTATION = "       ";
    private final String INDEX_IDENTATION = "   ";
    private Integer errorIndex = 1;
    private List<Exception> exceptions = new ArrayList<>();
    private final String startingMessage = "Alphabet Sector:" + System.lineSeparator();
    private boolean oddLength = false;
    private List<Character> ABCduplicateChars = new ArrayList<>();
    private boolean isABCempty = false;
    @Override
    public String getMessage() {
        return  System.lineSeparator()
                + startingMessage + exceptions.stream().map(Throwable::getMessage).collect(Collectors.joining(""));
    }

    public boolean shouldThrowException(){
        return exceptions.size() > 0;
    }
    public void addExceptionsToTheList(){
        addIsAlphabetEmptyException();
        addOddLengthException();
    }
//region add Exceptions
    private void addIsAlphabetEmptyException() {
        if (isABCempty) {
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The alphabet is empty" + System.lineSeparator()));
            errorIndex++;
        }
    }

    private void addOddLengthException() {
        if (oddLength) {
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The alphabet has an odd length" + System.lineSeparator()));
            errorIndex++;
        }
    }

    private void addDuplicateCharsException() {
        if (!ABCduplicateChars.isEmpty()) {
            exceptions.add(new Exception(EXCEPTION_IDENTATION + errorIndex.toString()
                    + ": The alphabet contains duplicate characters which are: "+ System.lineSeparator() + ABCduplicateChars + System.lineSeparator()));
            errorIndex++;
        }
    }
    //endregion
    public ABCNotValidException() {
        this.errorIndex = 1;
    }

    public void setABCempty(){
        isABCempty = true;
    }

    public void setIsOddLength() {
        this.oddLength = true;
    }


    public void addCharToDuplicateChars(Character letter) {
        ABCduplicateChars.add(letter);
    }
}
