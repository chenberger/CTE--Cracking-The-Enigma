package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Reflector;
import EnigmaMachine.RomanNumber;
import EnigmaMachine.Rotor;
import EnigmaMachineException.*;
import Jaxb.Schema.Generated.*;
import javafx.util.Pair;
import DTO.MachineDetails;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


public class EngineManager implements MachineOperations, Serializable {

    //region private data members
    private EnigmaMachine enigmaMachine;
    private StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer;



    //endregion

    public EngineManager(){
        this.statisticsAndHistoryAnalyzer = new StatisticsAndHistoryAnalyzer();
        this.enigmaMachine = null;
    }

    //region JAXB Translation


    public void setMachineDetailsFromXmlFile(String machineDetailsXmlFilePath) throws GeneralEnigmaMachineException, JAXBException, NotXmlFileException, FileNotFoundException {
        // TODO implement here also validation.(the file exist)
        JaxbToMacineTransformer jaxbToMachineTransformer = new JaxbToMacineTransformer();
        try {
            InputStream inputStream = new FileInputStream(new File(machineDetailsXmlFilePath));
            if (!machineDetailsXmlFilePath.endsWith(".xml")) {
                throw new NotXmlFileException();
            }
            CTEEnigma CteEnigma = jaxbToMachineTransformer.deserializeFrom(inputStream);
            enigmaMachine = jaxbToMachineTransformer.transformJAXBClassesToEnigmaMachine(CteEnigma);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }catch (JAXBException e){
            throw new RuntimeException();
        }
    }

    //endregion

    //region Operations implements
    @Override
    public void setSettingsAutomatically() throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, PluginBoardSettingsException, SettingsFormatException, CloneNotSupportedException, MachineNotExistsException, SettingsNotInitializedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE_SPECIFICATION, OperationType.LOAD_MACHINE_FROM_FILE);
        }

        RandomSettingsGenerator randomSettingsGenerator = new RandomSettingsGenerator(enigmaMachine);
        List<Sector> randomSectors = randomSettingsGenerator.getRandomSectorSettings();

        validateMachineSettings(randomSectors);
        initializeSettings(randomSectors);
    }

    //region set Settings
    public void initializeSettings(List<Sector> settingsSector) throws MachineNotExistsException, RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException, SettingsFormatException, SettingsNotInitializedException {
        setMachineSettings(settingsSector);
        enigmaMachine.setTheInitialCodeDefined(true);
        resetSettings();
        setSettingsFormat(settingsSector);
        checkIfTheSettingsFormatInitialized();
    }

    private void setSettingsFormat(List<Sector> settingsSector) {
        enigmaMachine.clearSettings();
        settingsSector.forEach(sector -> sector.addSectorToSettingsFormat(enigmaMachine));

        if (enigmaMachine.isPluginBoardSet()) {
            enigmaMachine.getOriginalSettingsFormat().setIfPluginBoardSet(true);
        } else {
            enigmaMachine.getOriginalSettingsFormat().setIfPluginBoardSet(false);
        }
    }

    private void setMachineSettings(List<Sector> settingsSector) throws CloneNotSupportedException, MachineNotExistsException {
        clearSettings();
        settingsSector.forEach(sector -> {
            try {
                sector.setSectorInTheMachine(enigmaMachine);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void validateMachineSettings(List<Sector> sectorSettings) throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException {
        sectorSettings.forEach(sector -> {
            try {
                sector.validateSector(enigmaMachine);
            } catch (RotorsInUseSettingsException | ReflectorSettingsException | PluginBoardSettingsException |
                     StartingPositionsOfTheRotorException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void clearSettings() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE_SPECIFICATION, OperationType.LOAD_MACHINE_FROM_FILE);
        }

        enigmaMachine.clearSettings();
    }
    public void checkIfTheSettingsFormatInitialized() throws SettingsFormatException, CloneNotSupportedException {
        if(!enigmaMachine.isTheInitialCodeDefined()) {
            throw new SettingsFormatException(enigmaMachine.getOriginalSettingsFormat());
        }

        statisticsAndHistoryAnalyzer.addSettingConfiguration((SettingsFormat) enigmaMachine.getOriginalSettingsFormat().clone());
    }
    //endregion

    @Override
    public void resetSettings() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException, SettingsNotInitializedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE_SPECIFICATION, OperationType.LOAD_MACHINE_FROM_FILE);
        }
        if(!enigmaMachine.isTheInitialCodeDefined()) {
            throw new SettingsNotInitializedException(OperationType.SET_SETTINGS_MANUAL, OperationType.SET_SETTINGS_AUTOMATIC);
        }

        enigmaMachine.resetSettings();
    }

    @Override
    public MachineDetails displaySpecifications() throws MachineNotExistsException, CloneNotSupportedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE_SPECIFICATION, OperationType.LOAD_MACHINE_FROM_FILE);
       }

       return new MachineDetails(enigmaMachine.getAllRotors(),
                                           enigmaMachine.getCurrentRotorsInUse(),
                                           enigmaMachine.getAllReflectors(),
                                           enigmaMachine.getCurrentReflectorInUse(),
                                           enigmaMachine.getKeyboard(),
                                           enigmaMachine.getPluginBoard(),
                                           statisticsAndHistoryAnalyzer.getMessagesCounter(),
                                           enigmaMachine.getOriginalSettingsFormat(),
                                           enigmaMachine.getCurrentSettingsFormat());
    }

    @Override
    public String analyzeHistoryAndStatistics() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE_SPECIFICATION, OperationType.LOAD_MACHINE_FROM_FILE);
        }

        return statisticsAndHistoryAnalyzer.toString();
    }

    @Override
    public String processInput(String inputToProcess) throws MachineNotExistsException, IllegalArgumentException {
        OriginalStringFormat originalStringFormat = new OriginalStringFormat(inputToProcess.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        Instant start = Instant.now();
        String encryptedString = getProcessedInput(inputToProcess);
        Instant end = Instant.now();
        long durationEncryptedTimeInNanoSeconds = Duration.between(start, end).toNanos();
        EncryptedStringFormat encryptedStringFormat = new EncryptedStringFormat(encryptedString.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        ProcessedStringsFormat processedStringsFormat = new ProcessedStringsFormat(new ArrayList<>(Arrays.asList(originalStringFormat, encryptedStringFormat)),
                durationEncryptedTimeInNanoSeconds, enigmaMachine.getOriginalSettingsFormat().getIndexFormat());
        enigmaMachine.getOriginalSettingsFormat().advanceIndexFormat();
        statisticsAndHistoryAnalyzer.addProcessedStringFormat(enigmaMachine.getOriginalSettingsFormat(), processedStringsFormat);
        statisticsAndHistoryAnalyzer.advancedMessagesCounter();

        return encryptedString;
    }

    @Override
    public void saveStateMachineToFile(String path) throws IOException, MachineNotExistsException {
        MachineState.saveStateMachineToFile(path, this);
    }

    @Override
    public void loadStateMachineFromFile(String path) throws IOException, ClassNotFoundException {
        MachineState.loadStateMachineFromFile(path, this);
    }

    private String getProcessedInput(String inputToProcess) throws IllegalArgumentException{
        //TODO chen: throw exception with more info: what are the illegal char and send the legal keyboard chars
        if(containsCharNotInMAMachineKeyboard(inputToProcess)){
            throw new IllegalArgumentException("The input contains char/s that are not in the machine keyboard");
        }
        String processedInput = "";
        for(char letter: inputToProcess.toCharArray()){
            processedInput += enigmaMachine.decode(letter);
        }
        return processedInput;
    }

    private boolean containsCharNotInMAMachineKeyboard(String inputToProcess) {
        for(char letter: inputToProcess.toCharArray()){
            if(!enigmaMachine.getKeyboard().contains(letter)){
                return true;
            }
        }
        return false;
    }
    //endregion

    public boolean isMachineExists() {
        return enigmaMachine != null;
    }

    public boolean isMachineSettingInitialized() {
        return enigmaMachine.isTheInitialCodeDefined();
    }

    public int getAmountOfActiveRotors() {
        return enigmaMachine.getNumOfActiveRotors();
    }

    public EnigmaMachine getCurrentEnigmaMachine() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException(OperationType.LOAD_MACHINE_SPECIFICATION, OperationType.LOAD_MACHINE_FROM_FILE);
        }

        return enigmaMachine;
    }

    public StatisticsAndHistoryAnalyzer getCurrentStatisticsAndHistory() {
        return statisticsAndHistoryAnalyzer;
    }

    public void setCurrentMachine(EnigmaMachine enigmaMachine) {
        this.enigmaMachine = enigmaMachine;
    }

    public void setCurrentStatisticsAndHistory(StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer) {
        this.statisticsAndHistoryAnalyzer = statisticsAndHistoryAnalyzer;
    }
}