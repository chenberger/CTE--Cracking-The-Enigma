package EnigmaMachineException;

public class MachineNotExistsException extends Exception {
    private final String startingMessage = "Error: There is no machine existing in the system." + System.lineSeparator();
    private final StringBuilder extraInfo;

    public MachineNotExistsException() {
        extraInfo = new StringBuilder();
        extraInfo.append("Please load an enigma machine to the system");
        extraInfo.append(" and then run this operation again").append(System.lineSeparator());
    }

    @Override
    public String getMessage() {
        return startingMessage + extraInfo;
    }
}
