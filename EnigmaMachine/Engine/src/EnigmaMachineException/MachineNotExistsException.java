package EnigmaMachineException;

import Engine.OperationType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MachineNotExistsException extends Exception {
    private final String startingMessage = "Error: There is no machine existing in the system." + System.lineSeparator();
    private final StringBuilder extraInfo;

    public MachineNotExistsException(OperationType... operations) {
        extraInfo = new StringBuilder();
        extraInfo.append("Go back to operation ");
        extraInfo.append((Arrays.stream(operations).map(OperationType::getOperationNumber).map(Object::toString).collect(Collectors.joining(" or "))));
        extraInfo.append(" and then run this operation again").append(System.lineSeparator());
    }

    @Override
    public String getMessage() {
        return startingMessage + extraInfo;
    }
}
