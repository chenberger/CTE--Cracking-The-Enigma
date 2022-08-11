package ConsoleUserInterface;


import Engine.EngineManager;
import Engine.OperationType;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUserInterface {
    private final EngineManager enigmaMachineEngine;
    private boolean wantToQuitFromTheProgram;

    public ConsoleUserInterface() {
        enigmaMachineEngine = new EngineManager();
        wantToQuitFromTheProgram = false;
    }

    public void run() {
        do {
            showMenu();
        }while (!wantToQuitFromTheProgram);
    }

    private void showMenu() {
        printMenu();
        OperationType operationToActivate = getOperationFromTheUser();
        operationToActivate.activate(this);
    }

    private OperationType getOperationFromTheUser() {
        Scanner scanner = new Scanner(System.in);
        Boolean validInput = false;
        Optional<Integer> userInput = Optional.empty();
        do {
            try {
                userInput = Optional.of(scanner.nextInt());
                if (userInput.get() >= OperationType.getMinimumNumber().get() && userInput.get() <= OperationType.getMaximumNumber().get()) {
                    validInput = true;
                }
            }
            catch (InputMismatchException ex) {
                System.out.println("Error: The value was inserted is incorrect!");
                validInput = false;
            }
        } while(!validInput);

        return OperationType.getOperationByNumber(userInput.get());
    }

    private void printMenu() {
        StringBuilder menuMessage = new StringBuilder(getOpeningMessage());

        for(OperationType operationType : OperationType.values()) {
            menuMessage.append(operationType.getOperationNumber()).append(". ").append(operationType.getDescription());
            menuMessage.append(System.lineSeparator());
        }

        System.out.println(menuMessage);
    }

    private StringBuilder getOpeningMessage() {
        StringBuilder openingMessage = new StringBuilder();
        openingMessage.append("======================================================================================");
        openingMessage.append(System.lineSeparator());
        openingMessage.append("Hello, and welcome to the enigma machine, please choose an action from the list below:");
        openingMessage.append(System.lineSeparator());
        openingMessage.append("==================================================================================");
        openingMessage.append(System.lineSeparator());

        return openingMessage;
    }
}
