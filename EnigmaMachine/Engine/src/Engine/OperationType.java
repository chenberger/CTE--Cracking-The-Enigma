package Engine;

import ConsoleUserInterface.ConsoleUserInterface;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum OperationType implements ActivateOperation {
    LOAD_MACHINE(1, "Load machine specification from file") {
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            //consoleUserInterface.loadMachineFromXML();
        }
    },
    DISPLAY_MACHINE_SPECIFICATION(2, "Display machine specification"){
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            consoleUserInterface.displayMachineSpecification();
        }
    },
    SET_SETTINGS_MANUAL(3, "Set machine settings manually ") {
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            consoleUserInterface.setMachineSettingsManually();
        }
    },
    SET_SETTINGS_AUTOMATIC(4, "Set machine settings automatically ") {
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            consoleUserInterface.setMachineSettingsAutomatically();
        }
    },
    PROCESS_INPUT(5, "Encrypt message") {
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            consoleUserInterface.processInput();
        }
    },
    RESET_SETTINGS(6, "Reset machine settings") {
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            consoleUserInterface.resetSettingsMachine();
        }
    },
    DISPLAY_STATISTICS_AND_HISTORY(7, "Display statistics and history about the machine") {
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            consoleUserInterface.displayStatisticsAndHistoryOfTheMachine();
        }
    },
    EXIT(8, "Exit") {
        @Override
        public void activate(ConsoleUserInterface consoleUserInterface) {
            consoleUserInterface.exit();
        }
    };

    private final int operationNumber;
    private final String description;

    OperationType(int operationNumber, String description) {
        this.operationNumber = operationNumber;
        this.description = description;
    }

    public static OperationType getOperationByNumber(int number) throws IllegalArgumentException {
        for(OperationType operationType : OperationType.values()) {
            if(operationType.operationNumber == number) {
                return operationType;
            }
        }

        throw new IllegalArgumentException("Error: There is no any operation type matching");
    }

    public String getDescription() {
        return description;
    }

    public int getOperationNumber() {
        return operationNumber;
    }

    public static Optional<Integer> getMinimumNumber() {
        return Arrays.stream(OperationType.values()).map(op -> op.operationNumber).collect(Collectors.toList()).stream().min(Integer::compareTo);
    }

    public static Optional<Integer> getMaximumNumber() {
        return Arrays.stream(OperationType.values()).map(op -> op.operationNumber).collect(Collectors.toList()).stream().max(Integer::compareTo);
    }
}