package EnigmaMachineException;

import Engine.OperationType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SettingsNotInitializedException extends Exception{
    private String startingMessage = "Error: The initial code configuration has not been configured for the machine." + System.lineSeparator();

    private final StringBuilder extraInfo;

    public SettingsNotInitializedException(OperationType... operations) {
        extraInfo = new StringBuilder();
        extraInfo.append("You must initialize the code configuration first.").append(System.lineSeparator());
    }

    @Override
    public String getMessage() {
        return startingMessage + extraInfo;
    }
}
