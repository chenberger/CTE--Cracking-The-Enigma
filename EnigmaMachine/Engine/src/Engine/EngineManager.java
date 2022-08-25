package Engine;

import DTO.MachineDetails;
import Events.EventHandler;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.*;
import Jaxb.Schema.Generated.CTEEnigma;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

// the purpose of the EngineManager is to manage the Enigma Machine, and it's operations.
public class EngineManager implements MachineOperations, Serializable {

    //region private data members
    private EnigmaMachine enigmaMachine;
    private StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer;
    //endregion

    public EventHandler<String> statisticsAndHistoryHandler;
    public EventHandler<String> currentCodeConfigurationHandler;
    public EventHandler<MachineDetails> machineDetailsHandler;

    public EngineManager(){
        this.statisticsAndHistoryAnalyzer = new StatisticsAndHistoryAnalyzer();
        this.enigmaMachine = null;

        initializeEventHandlers();
    }

    //region events
    private void initializeEventHandlers() {
        this.statisticsAndHistoryHandler = new EventHandler<>();
        this.currentCodeConfigurationHandler = new EventHandler<>();
        this.machineDetailsHandler = new EventHandler<>();
    }

    private void onCurrentCodeConfigurationChanged() throws MachineNotExistsException, CloneNotSupportedException {
        currentCodeConfigurationHandler.invoke(this, displaySpecifications().getCurrentMachineSettings());
    }

    private void onMachineDetailsChanged() throws MachineNotExistsException, CloneNotSupportedException {
        machineDetailsHandler.invoke(this, displaySpecifications());
    }

    private void onStatisticsAndHistoryChanged() throws MachineNotExistsException, CloneNotSupportedException {
        statisticsAndHistoryHandler.invoke(this, statisticsAndHistoryAnalyzer.toString());
    }
    //endregion


    //region JAXB Translation
    public void setMachineDetailsFromXmlFile(String machineDetailsXmlFilePath) throws GeneralEnigmaMachineException, JAXBException, NotXmlFileException, FileNotFoundException {
        JaxbToMacineTransformer jaxbToMachineTransformer = new JaxbToMacineTransformer();
        try {
            InputStream inputStream = new FileInputStream(machineDetailsXmlFilePath);
            if (!machineDetailsXmlFilePath.endsWith(".xml")) {
                throw new NotXmlFileException();
            }
            CTEEnigma CteEnigma = jaxbToMachineTransformer.deserializeFrom(inputStream);
            enigmaMachine = jaxbToMachineTransformer.transformJAXBClassesToEnigmaMachine(CteEnigma);
            if(statisticsAndHistoryAnalyzer != null) {
                statisticsAndHistoryAnalyzer.clear();
            }

            onCurrentCodeConfigurationChanged();
            onMachineDetailsChanged();
            onStatisticsAndHistoryChanged();
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        catch (JAXBException | MachineNotExistsException | CloneNotSupportedException e){
            throw new RuntimeException();
        }
    }

    //endregion

    //region Operations implements
    @Override
    public void setSettingsAutomatically() throws RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, PluginBoardSettingsException, SettingsFormatException, CloneNotSupportedException, MachineNotExistsException, SettingsNotInitializedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException();
        }

        RandomSettingsGenerator randomSettingsGenerator = new RandomSettingsGenerator(enigmaMachine);
        List<Sector> randomSectors = randomSettingsGenerator.getRandomSectorSettings();

        initializeSettings(randomSectors);
    }

    //region set Settings
    public void initializeSettings(List<Sector> settingsSector) throws MachineNotExistsException, RotorsInUseSettingsException, StartingPositionsOfTheRotorException, ReflectorSettingsException, CloneNotSupportedException, PluginBoardSettingsException, SettingsFormatException, SettingsNotInitializedException {
        validateMachineSettings(settingsSector);
        setMachineSettings(settingsSector);
        enigmaMachine.setTheInitialCodeDefined(true);
        resetMachine();
        setSettingsFormat(settingsSector);
        checkIfTheSettingsFormatInitialized();

        onCurrentCodeConfigurationChanged();
        onMachineDetailsChanged();
        onStatisticsAndHistoryChanged();
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
        AtomicReference<Boolean> needToThrowException = new AtomicReference<>(false);
        StringBuilder exceptionMessage = new StringBuilder();

        sectorSettings.forEach(sector -> {
            try {
                sector.validateSector(enigmaMachine);
            } catch (RotorsInUseSettingsException | ReflectorSettingsException | PluginBoardSettingsException |
                     StartingPositionsOfTheRotorException e) {
                exceptionMessage.append(e.getMessage() + System.lineSeparator());
                needToThrowException.set(true);
            }
        });

        if(needToThrowException.get()) {
            throw new RuntimeException(exceptionMessage.toString());
        }
    }

    public void clearSettings() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException();
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

    private void resetMachine() throws MachineNotExistsException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException, SettingsNotInitializedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException();
        }
        if(!enigmaMachine.isTheInitialCodeDefined()) {
            throw new SettingsNotInitializedException(OperationType.SET_SETTINGS_MANUAL, OperationType.SET_SETTINGS_AUTOMATIC);
        }

        enigmaMachine.resetSettings();
    }

    @Override
    public void resetSettings() throws MachineNotExistsException, CloneNotSupportedException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, SettingsNotInitializedException, StartingPositionsOfTheRotorException, PluginBoardSettingsException {
        resetMachine();
        onCurrentCodeConfigurationChanged();
    }

    @Override
    public MachineDetails displaySpecifications() throws MachineNotExistsException, CloneNotSupportedException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException();
       }

       return new MachineDetails(enigmaMachine,
                                 statisticsAndHistoryAnalyzer.getMessagesCounter(),
                                 enigmaMachine.getOriginalSettingsFormat(),
                                 enigmaMachine.getCurrentSettingsFormat());
    }

    @Override
    public String analyzeHistoryAndStatistics() throws MachineNotExistsException {
        if(!isMachineExists()) {
            throw new MachineNotExistsException();
        }

        return statisticsAndHistoryAnalyzer.toString();
    }

    @Override
    public String processInput(String inputToProcess) throws MachineNotExistsException, IllegalArgumentException, CloneNotSupportedException {
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

        onMachineDetailsChanged();
        onStatisticsAndHistoryChanged();

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
        if(containsCharNotInMAMachineKeyboard(inputToProcess)){
            List<Character> lettersNotInAbc = new ArrayList<>(getCharsNotInMachineKeyboard(inputToProcess));
            throw new IllegalArgumentException("Error: The input contains char/s that are not in the machine keyboard which are: " + lettersNotInAbc + System.lineSeparator()
                    + "You can choose only from the following letters: " + enigmaMachine.getKeyboard());
        }
        String processedInput = "";
        for(char letter: inputToProcess.toCharArray()){
            processedInput += enigmaMachine.decode(letter);
        }
        return processedInput;
    }

    private List<Character> getCharsNotInMachineKeyboard(String inputToProcess) {
        return inputToProcess.chars().mapToObj(inputtedChar -> (char)inputtedChar).filter(inputtedChar -> !enigmaMachine.getKeyboard().contains(inputtedChar)).collect(Collectors.toList());
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
            throw new MachineNotExistsException();
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