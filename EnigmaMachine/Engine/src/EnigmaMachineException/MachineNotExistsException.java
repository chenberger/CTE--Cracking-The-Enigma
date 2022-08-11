package EnigmaMachineException;

public class MachineNotExistsException extends Exception {
    private final String startingMessage = "Error: There is no machine existing in the system. ";
    private final String extraInfo;

    public MachineNotExistsException(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String getMessage() {
        return startingMessage + extraInfo;
    }
}
