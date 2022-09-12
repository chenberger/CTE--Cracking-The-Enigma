package ConsoleUserInterface;


import DTO.MachineDetails;
import Engine.EngineManager;
import Engine.OperationType;
import EnigmaMachine.RomanNumber;
import EnigmaMachine.Settings.PluginBoardSector;
import EnigmaMachine.Settings.ReflectorIdSector;
import EnigmaMachine.Settings.RotorIDSector;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import EnigmaMachineException.*;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                    System.out.println(System.lineSeparator());
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

    //region Operations implementation
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
        catch(MachineNotExistsException  | CloneNotSupportedException ex) {
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
                             .append("The total amount of reflectors that can be use in the machine is: ")
                             .append(machineDetails.getAmountOfTotalReflectors())
                             .append(System.lineSeparator())
                             .append("The total amount of messages have been processed by the machine so far, in total, since the file was loaded, for all the codes defined in it is: ")
                             .append(machineDetails.getMessagesCounter())
                             .append(System.lineSeparator());
        if(machineDetails.isMachineSettingsInitialized()) {
            machineSpecifications.append("The original code machine settings is: ")
                                 .append(machineDetails.getOriginalMachineSettings())
                                 .append(System.lineSeparator())
                                 .append("The current code machine settings is:  ")
                                 .append(machineDetails.getCurrentMachineSettings())
                                 .append(System.lineSeparator());
        }
        else {
            machineSpecifications.append("Note: The machine settings code has not been defined yet");
        }

        machineSpecifications.append(System.lineSeparator());
        System.out.println(machineSpecifications);
    }

    public void setMachineSettingsAutomatically() {
        try {
            enigmaMachineEngine.setSettingsAutomatically();
            System.out.println("The machine settings have been successfully loaded");
            System.out.println("The random machine settings that was generated are: ");
            System.out.println(enigmaMachineEngine.getCurrentEnigmaMachine().getOriginalSettingsFormat() + System.lineSeparator());
        } catch (RotorsInUseSettingsException | StartingPositionsOfTheRotorException | ReflectorSettingsException |
                 SettingsNotInitializedException |
                 PluginBoardSettingsException | SettingsFormatException | CloneNotSupportedException |
                 MachineNotExistsException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public void resetSettingsMachine() {
        try {
            enigmaMachineEngine.resetSettings();
            System.out.println("The machine settings have been reset successfully" + System.lineSeparator());
        }
        catch (RotorsInUseSettingsException | StartingPositionsOfTheRotorException | ReflectorSettingsException |
               PluginBoardSettingsException | SettingsFormatException | CloneNotSupportedException |
               MachineNotExistsException | IllegalArgumentException | SettingsNotInitializedException ex) {

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
        boolean continueOperation = false;

        try {
            if (!enigmaMachineEngine.isMachineExists()) {
                throw new MachineNotExistsException();
            }

            if(!enigmaMachineEngine.isMachineSettingInitialized()) {
                throw new SettingsNotInitializedException(OperationType.SET_SETTINGS_MANUAL, OperationType.SET_SETTINGS_AUTOMATIC);
            }
            do {
                try {
                    System.out.println("Please enter a message that you want to encrypt/decrypt: ");
                    userInputToEncrypt = scanner.nextLine();
                    encryptedMessage = enigmaMachineEngine.processInput(userInputToEncrypt.toUpperCase(), false);
                    continueOperation = false;
                    System.out.println("The message have been processed successfully" + System.lineSeparator() +
                            "The processed message is: " + encryptedMessage + System.lineSeparator());

                } catch (MachineNotExistsException | IllegalArgumentException | CloneNotSupportedException ex) {
                    System.out.println(ex.getMessage());
                    continueOperation = shouldContinueInOperation();
                    if (continueOperation) {
                        System.out.println("Please try again:");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } while (continueOperation);
        }

        catch(MachineNotExistsException  | SettingsNotInitializedException ex)  {
            System.out.println(ex.getMessage());
        }
    }

    public void loadMachineFromXML() {
        Scanner scanner = new Scanner(System.in);
        String path;
        boolean operationSuccessful = false;
        boolean continueOperation = true;
        while (!operationSuccessful && continueOperation) {
            System.out.println("Please enter the path of the XML file that you want to load the machine from: ");
            path = scanner.nextLine();
            try {
                enigmaMachineEngine.setMachineDetailsFromXmlFile(path);
                System.out.println("The machine has been successfully loaded from the XML file" + System.lineSeparator());
                operationSuccessful = true;
            } catch (GeneralEnigmaMachineException | NotXmlFileException | IllegalAgentsAmountException ex) {
                System.out.println(ex.getMessage());
            }catch (FileNotFoundException | JAXBException ex){
                System.out.println("The file (from the path you inputted) was not found! ");;
            }finally {
                if(!operationSuccessful) {
                    continueOperation = shouldContinueInOperation();
                }
            }
        }
    }

    public boolean shouldContinueInOperation() {
        Scanner scanner = new Scanner(System.in);
        boolean continueOperation = false;
        boolean validInput = false;
        do {
            System.out.println("To continue and try again this operation, please press 1, to return to the main operations menu, please press 2: ");
            String userInput = scanner.nextLine();
            if (userInput.equals("1")) {
                continueOperation = true;
                validInput = true;
            } else if (userInput.equals("2")) {
                validInput = true;
            } else {
                System.out.println("Invalid input, please try again...");
            }
        } while (!validInput);
        return continueOperation;
    }

    //region set settings manually
    public void setMachineSettingsManually() {
       try {
            if(!enigmaMachineEngine.isMachineExists()) {
                throw new MachineNotExistsException();
            }

            Optional<RotorIDSector> rotorIDSector = Optional.ofNullable(getRotorIDSector());
            rotorIDSector.orElseThrow(ExceptionInInitializerError::new);

            Optional<StartingRotorPositionSector> startingRotorPositionSector = Optional.ofNullable(getInitialRotorPositionForTheRotorsSector(rotorIDSector.get()));
            startingRotorPositionSector.orElseThrow(ExceptionInInitializerError::new);

            Optional<ReflectorIdSector> reflectorIdSector = Optional.ofNullable(getReflectorIdSector());
            reflectorIdSector.orElseThrow(ExceptionInInitializerError::new);

            Optional<PluginBoardSector> pluginBoardSector = Optional.ofNullable(getPluginPairSector());
            pluginBoardSector.orElseThrow(ExceptionInInitializerError::new);

            enigmaMachineEngine.initializeSettings(new ArrayList<>(Arrays.asList(rotorIDSector.get(), startingRotorPositionSector.get(), reflectorIdSector.get(), pluginBoardSector.get())));
           System.out.println("The machine settings have been successfully loaded" + System.lineSeparator());
        }
        catch (MachineNotExistsException  | SettingsFormatException | CloneNotSupportedException  |RotorsInUseSettingsException | SettingsNotInitializedException |
               StartingPositionsOfTheRotorException | ReflectorSettingsException | PluginBoardSettingsException ex) {

            System.out.println(ex.getMessage());
        }
        catch (ExceptionInInitializerError ignored) {
        }
    }

    private PluginBoardSector getPluginPairSector() {
        PluginBoardSector pluginBoardSector = null;
        String userInput;
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        boolean continueOperation = true;

        System.out.println("Please enter characters that make up all the plugin pairs in the machine." + System.lineSeparator() +
                          "The pairs will appear close together, without any separator of one kind or another. When finished, type enter to complete the insertion of the string." + System.lineSeparator() +
                          "(Note: you must make sure that the length of the string is even, in addition an empty string is a valid input - it means that there are no plugin pairs at all in the machine." + System.lineSeparator() +
                          "Also, do not use the same character in more than one mapping pair, and there is no character mapping to itself.)");

        do {
            userInput = scanner.nextLine();
            if(isLengthIsEven(userInput.length())) {
                try {
                    pluginBoardSector = convertStringToPluginPairsSector(userInput.toUpperCase());
                    pluginBoardSector.validateSector(enigmaMachineEngine.getCurrentEnigmaMachine());
                    validInput = true;
                    System.out.println("The plugin board settings have been successfully loaded" + System.lineSeparator());
                }
                catch(PluginBoardSettingsException | MachineNotExistsException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else {
                System.out.println("Error: The length of the characters that was inserted is odd!" + System.lineSeparator() +
                                   "(Reminder: Due to the fact that the characters represents are pairs, the length of the string must be even)");
            }

            if(!validInput) {
                pluginBoardSector = null;
                continueOperation = shouldContinueInOperation();

                if(continueOperation) {
                    System.out.println("Please try again:");
                }
            }

        } while(!validInput && continueOperation);

        return pluginBoardSector;
    }

    private PluginBoardSector convertStringToPluginPairsSector(String stringToConvert) {
        List<Pair<Character, Character>> pluginPairs = new ArrayList<>();

        for (int i = 0; i < stringToConvert.length(); i+=2) {
            pluginPairs.add(new Pair<>(stringToConvert.charAt(i), stringToConvert.charAt(i + 1)));
        }

        return new PluginBoardSector(pluginPairs);
    }

    private boolean isLengthIsEven(int length) {
        return length % 2 == 0;
    }

    private ReflectorIdSector getReflectorIdSector() {
        int userInput;
        Scanner scanner = new Scanner(System.in);
        ReflectorIdSector reflectorIdSector = null;
        boolean validInput = false;
        boolean continueOperation = true;

        System.out.println("Please select the reflector that will be in the Enigma machine" + System.lineSeparator() +
                           "(Note: choose a number between " + RomanNumber.minRomanValue() + " to " + RomanNumber.maxRomanValue() + ")");

        for(RomanNumber romanNumber : RomanNumber.values()) {
            System.out.println(romanNumber.getDecimalRepresentation() + ". " + romanNumber);
        }

        do{
            try {
                userInput = scanner.nextInt();
                if(userInput >= RomanNumber.minRomanValue() && userInput <= RomanNumber.maxRomanValue()) {
                    reflectorIdSector = new ReflectorIdSector(new ArrayList<>(Arrays.asList(RomanNumber.convertIntToRomanNumber(userInput))));
                    reflectorIdSector.validateSector(enigmaMachineEngine.getCurrentEnigmaMachine());
                    validInput = true;
                    System.out.println("The reflector settings have been successfully loaded" + System.lineSeparator());
                }
                else {
                    System.out.println("Error: the number was inserted is out of range!" + System.lineSeparator()
                                       + "The correct range is between " + RomanNumber.minRomanValue() + " to " + RomanNumber.maxRomanValue());
                }
            }
            catch(InputMismatchException ex) {
                System.out.println("Error: The value that was inserted is is not a integer!");
                scanner.nextLine();
            }
            catch(ReflectorSettingsException | MachineNotExistsException ex) {
                System.out.println(ex.getMessage());
            }

            if(!validInput) {
                reflectorIdSector = null;
                continueOperation = shouldContinueInOperation();

                if(continueOperation) {
                    System.out.println("Please try again:");
                }
            }
        } while(!validInput && continueOperation);

        return reflectorIdSector;
    }

    private StartingRotorPositionSector getInitialRotorPositionForTheRotorsSector(RotorIDSector rotorIDSector) {
        StartingRotorPositionSector startingRotorPositionSector = null;
        List<Character> rotorsPositionsAsList;
        Scanner scanner = new Scanner(System.in);
        String userInput;
        boolean validInput = false;
        boolean continueOperation = true;

        System.out.println("Please enter the starting positions of the rotors without any separator" + System.lineSeparator() +
                "(Note: you have to insert " + rotorIDSector.getElements().size() + " positions exactly!" +
                " In addition, note that the positions you will enter are characters from the system's ABC)");

        do {
            userInput = scanner.nextLine();
            rotorsPositionsAsList = userInput.toUpperCase().chars().mapToObj(character -> (char)character).collect(Collectors.toList());
            try {
                Collections.reverse(rotorsPositionsAsList);
                startingRotorPositionSector = new StartingRotorPositionSector(rotorsPositionsAsList);
                startingRotorPositionSector.validateSector(enigmaMachineEngine.getCurrentEnigmaMachine());
                validInput = true;
                System.out.println("The rotor starting positions were moved successfully according to the right column" + System.lineSeparator());
            }
            catch(StartingPositionsOfTheRotorException  | MachineNotExistsException ex) {
                System.out.println(ex.getMessage());
                startingRotorPositionSector = null;
                continueOperation = shouldContinueInOperation();

                if(continueOperation) {
                    System.out.println("Please try again:");
                }
            }

        } while(!validInput && continueOperation);

        return startingRotorPositionSector;
    }

    private RotorIDSector getRotorIDSector() {
        RotorIDSector rotorIDSector = null;
        List<Integer> rotorsIdAsIntList;
        List<String> rotorsIdAsStringList;
        Scanner scanner = new Scanner(System.in);
        String userInput;
        boolean validInput = false;
        boolean continueOperation = true;

        System.out.println("Please enter the numbers id of rotors that will be in the Enigma machine separated by a comma." + System.lineSeparator() +
                "(Note: you have to insert " + enigmaMachineEngine.getAmountOfActiveRotors() + " rotors exactly!)");

        do {
            userInput = scanner.nextLine();
            rotorsIdAsStringList = Stream.of(userInput.split(",")).collect(Collectors.toList());
            try {
                rotorsIdAsIntList = rotorsIdAsStringList.stream().map(Integer::parseInt).collect(Collectors.toList());
                Collections.reverse(rotorsIdAsIntList);
                rotorIDSector = new RotorIDSector(rotorsIdAsIntList);
                rotorIDSector.validateSector(enigmaMachineEngine.getCurrentEnigmaMachine());
                validInput = true;
                System.out.println("The selected rotors have been successfully inserted into the machine" + System.lineSeparator());
            }
            catch(NumberFormatException ex) {
                System.out.println("Error: Failed to initialize the rotors that need to be use in the machine settings because of the following reasons: "
                                + System.lineSeparator() + "       1.Could not convert the rotor number " + ex.getMessage() + " to integer");
            }
            catch(RotorsInUseSettingsException  | MachineNotExistsException ex) {
                System.out.println(ex.getMessage());
            }

            if(!validInput) {
                rotorIDSector = null;
                continueOperation = shouldContinueInOperation();

                if(continueOperation) {
                    System.out.println("Please try again:");
                }
            }

        } while(!validInput && continueOperation);

        return rotorIDSector;
    }
    //endregion

    public void saveMachineStateToFile() {
        String path;
        Scanner scanner = new Scanner(System.in);
        boolean operationSuccessful = false;
        boolean continueOperation = true;

        try {
            if (!enigmaMachineEngine.isMachineExists()) {
                throw new MachineNotExistsException();

            }
            do {
                System.out.println("Please enter the path (include the file name without the extension) of the file that you want to save to it the current state of the machine to: ");
                path = path = scanner.nextLine();
                try {
                    enigmaMachineEngine.saveStateMachineToFile(path);
                    System.out.println("The machine has been successfully saved to the file" + System.lineSeparator());
                    operationSuccessful = true;
                } catch (IOException ex) {
                    System.out.println("The file (from the path you inputted) was not found! ");
                    ;
                } catch (MachineNotExistsException e) {
                    System.out.println(e.getMessage());
                } finally {
                    if (!operationSuccessful) {
                        continueOperation = shouldContinueInOperation();
                    }
                }
            } while (!operationSuccessful && continueOperation);
        }
        catch (MachineNotExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadMachineFromFile() {
        String path;
        Scanner scanner = new Scanner(System.in);
        boolean operationSuccessful = false;
        boolean continueOperation = true;
        do{
            System.out.println("Please enter the path (include the file name without the extension) of the file that you want to load the machine from: ");
            path = scanner.nextLine();
            try {
                enigmaMachineEngine.loadStateMachineFromFile(path);
                System.out.println("The machine has been successfully loaded" + System.lineSeparator());
                operationSuccessful = true;
            }
            catch (IOException ex){
                System.out.println("The file (from the path you inputted) was not found! ");;
            }
            catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            finally {
                if(!operationSuccessful) {
                    continueOperation = shouldContinueInOperation();
                }
            }
        }while (!operationSuccessful && continueOperation);
    }
    //endregion

}