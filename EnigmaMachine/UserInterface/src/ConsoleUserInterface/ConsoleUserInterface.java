package ConsoleUserInterface;


import Engine.EngineManager;
import Engine.OperationType;
import EnigmaMachineException.*;
import TDO.MachineDetails;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Locale;
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
        } while (!wantToQuitFromTheProgram);
    }

    private void showMenu() {
        printMenu();
        OperationType operationToActivate = getOperationFromTheUser();
        operationToActivate.activate(this);

        if(!wantToQuitFromTheProgram) {
            returnToMainMenu();
        }
    }

    private OperationType getOperationFromTheUser() {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        Optional<Integer> userInput = Optional.empty();
        do {
            try {
                userInput = Optional.of(scanner.nextInt());
                if (userInput.get() >= OperationType.getMinimumNumber().get() && userInput.get() <= OperationType.getMaximumNumber().get()) {
                    validInput = true;
                }
                else {
                    printIllegalOperationNumberInserted(userInput.get(), OperationType.getMinimumNumber().get(), OperationType.getMaximumNumber().get());
                }
            }
            catch (InputMismatchException ex) {
                System.out.println(scanner.nextLine());
                System.out.println("Error: The value that was inserted is is not a integer!");

            }

            if(!validInput) {
                System.out.println("Please try again: ");
            }
        } while(!validInput);

        return OperationType.getOperationByNumber(userInput.get());
    }

    private void printIllegalOperationNumberInserted(Integer illegalOperationNumber, Integer minOperationNumber, Integer maxOperationNumber) {
        StringBuilder illegalMessage = new StringBuilder();
        illegalMessage.append("Error: Illegal operation number was inserted!")
                      .append(System.lineSeparator())
                      .append("The operation number that was inserted is: ")
                      .append(illegalOperationNumber)
                      .append(System.lineSeparator())
                      .append("The range of the operations is between ")
                      .append(minOperationNumber)
                      .append(" to ")
                      .append(maxOperationNumber);

        System.out.println(illegalMessage);
    }

    private void printMenu() {
        StringBuilder menuMessage = new StringBuilder(getOpeningMessage());

        for(OperationType operationType : OperationType.values()) {
            menuMessage.append(operationType.getOperationNumber())
                       .append(". ")
                       .append(operationType.getDescription())
                       .append(System.lineSeparator());
        }

        System.out.println(menuMessage);
    }

    private StringBuilder getOpeningMessage() {
        StringBuilder openingMessage = new StringBuilder();
        openingMessage.append("======================================================================================")
                      .append(System.lineSeparator())
                      .append("Hello, and welcome to the enigma machine, please choose an action from the list below:")
                      .append(System.lineSeparator())
                      .append("======================================================================================")
                      .append(System.lineSeparator());

        return openingMessage;
    }

    public void exit() {
        this.wantToQuitFromTheProgram = true;
        System.out.println("Thank you for using our machine! see you next time");
    }

    public void displayMachineSpecification() {
        MachineDetails machineDetails;

        try {
            machineDetails = enigmaMachineEngine.displaySpecifications();
            printMachineSpecifications(machineDetails);
        }
        catch(MachineNotExistsException ex) {
            System.out.println(ex.getMessage() + System.lineSeparator());
        }
    }

    private void printMachineSpecifications(MachineDetails machineDetails) {
        StringBuilder machineSpecifications = new StringBuilder();
        machineSpecifications.append("The machine specifications are: ")
                             .append(System.lineSeparator())
                             .append("The total amount of rotors that can be use in the machine is: ")
                             .append(machineDetails.getAmountOfTotalRotors())
                             .append(System.lineSeparator())
                             .append("The current amount of rotors in use in the machine is: ")
                             .append(machineDetails.getAmountCurrentRotorsInUse())
                             .append(System.lineSeparator())
                             //.append("The current notch positions are : (by using the format: [rotor_number:notch_position])")
                             //.append(machineDetails.getNotchPositionsInRotorsInUse())
                             //.append(System.lineSeparator())
                             .append("The total amount of reflectors that can be use in the machine is: ")
                             .append(machineDetails.getAmountOfTotalReflectors())
                             .append(System.lineSeparator());
        if(machineDetails.isMachineSettingsInitialized()) {
            machineSpecifications.append("The current code machine settings is: ")
                                 .append(machineDetails.getMachineSettings())
                                 .append(System.lineSeparator());
        }
        else {
            machineSpecifications.append("Note: The machine settings code has not yet been defined");
        }

        machineSpecifications.append(System.lineSeparator());
        System.out.println(machineSpecifications);
    }

    private void returnToMainMenu(){
        System.out.println("Press any key to return to the main menu...");

        try {
            System.in.read();
        }
        catch(IOException e) {
            System.out.println(System.lineSeparator());
        }
    }

    public void setMachineSettingsAutomatically() {
        try {
            enigmaMachineEngine.setSettingsAutomatically();
            System.out.println("The machine settings have been successfully loaded" + System.lineSeparator());
        }
        catch (RotorsInUseSettingsException | StartingPositionsOfTheRotorException | ReflectorSettingsException |
               PluginBoardSettingsException | SettingsFormatException | CloneNotSupportedException | MachineNotExistsException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public void resetSettingsMachine() {
        try {
            enigmaMachineEngine.resetSettings();
            System.out.println("The machine settings have been reset successfully" + System.lineSeparator());
        }
        catch (RotorsInUseSettingsException | StartingPositionsOfTheRotorException | ReflectorSettingsException | PluginBoardSettingsException |
               SettingsFormatException | CloneNotSupportedException | MachineNotExistsException | IllegalArgumentException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public void displayStatisticsAndHistoryOfTheMachine() {
        try {
            System.out.println(enigmaMachineEngine.analyzeHistoryAndStatistics());
        }
        catch (MachineNotExistsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void processInput() {
        String userInputToEncrypt;
        String encryptedMessage;
        Scanner scanner = new Scanner(System.in);

        if(!enigmaMachineEngine.isMachineSettingInitialized()) {
            System.out.println("Error: The initial code configuration has not been configured for the machine, you must return to operation 3 or 4 and then return to this operation");
        }
        else {
            try {
                System.out.println("Please enter a message that you want to encrypt: ");
                userInputToEncrypt = scanner.nextLine();
                encryptedMessage = enigmaMachineEngine.processInput(userInputToEncrypt.toUpperCase(Locale.ROOT));
                System.out.println("The encrypted message is: " + encryptedMessage + System.lineSeparator());
            } catch (MachineNotExistsException  | IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
