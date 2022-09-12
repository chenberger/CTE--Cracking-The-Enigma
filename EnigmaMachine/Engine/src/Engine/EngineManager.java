package Engine;

import BruteForce.DecryptionManager;
import DTO.BruteForceTask;
import DTO.MachineDetails;
import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceUIAdapter;
import DesktopUserInterface.MainScene.MainController;
import Engine.StatisticsAndHistory.EncryptedStringFormat;
import Engine.StatisticsAndHistory.OriginalStringFormat;
import Engine.StatisticsAndHistory.ProcessedStringsFormat;
import Engine.StatisticsAndHistory.StatisticsAndHistoryAnalyzer;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.Sector;
import EnigmaMachine.Settings.SettingsFormat;
import EnigmaMachineException.*;
import Events.EventHandler;
import Jaxb.Schema.Generated.CTEEnigma;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

// the purpose of the EngineManager is to manage the Enigma Machine, and it's operations.
public class EngineManager implements MachineOperations, Serializable {

    //region private data members
    private EnigmaMachine enigmaMachine;
    private Dictionary dictionary;
    private DecryptionManager decryptionManager;
    private StatisticsAndHistoryAnalyzer statisticsAndHistoryAnalyzer;
    private MainController mainController;
    //endregion

    public EventHandler<String> statisticsAndHistoryHandler;
    public EventHandler<String> currentCodeConfigurationHandler;
    public EventHandler<MachineDetails> machineDetailsHandler;
    public EventHandler<Dictionary> dictionaryChangedHandler;
    public EventHandler<Set<Character>> keyboardChangedHandler;
    public EventHandler<String> decryptionCandidateEventHandler;
    public EventHandler<Integer> maxAgentsAmountChangedHandler;
    //string to test the dm
    private String encryptedString = "";

    public EngineManager(){
        this.statisticsAndHistoryAnalyzer = new StatisticsAndHistoryAnalyzer();
        this.dictionary = new Dictionary();
        this.enigmaMachine = null;
        this.decryptionManager = new DecryptionManager();

        initializeEventHandlers();
    }

    //region events
    private void initializeEventHandlers() {
        this.statisticsAndHistoryHandler = new EventHandler<>();
        this.currentCodeConfigurationHandler = new EventHandler<>();
        this.machineDetailsHandler = new EventHandler<>();
        this.dictionaryChangedHandler = new EventHandler<>();
        this.keyboardChangedHandler = new EventHandler<>();
        this.decryptionCandidateEventHandler = new EventHandler<>();
        this.maxAgentsAmountChangedHandler = new EventHandler<>();
    }

    private void onMaxAgentsAmountChanged(){
        maxAgentsAmountChangedHandler.invoke(this, decryptionManager.getMaxCurrentAmountOfAgents());
    }

    private void onDecryptionMangerFinished(){
        decryptionCandidateEventHandler.invoke(this, decryptionManager.getDecryptionCandidatesStatistics());
    }
    private void onDictionaryChanged() {
        dictionaryChangedHandler.invoke(this, dictionary);
    }

    private void onKeyboardChanged() {
        keyboardChangedHandler.invoke(this, enigmaMachine.getKeyboardCharacters());
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
    public void setMachineDetailsFromXmlFile(String machineDetailsXmlFilePath) throws GeneralEnigmaMachineException, JAXBException, NotXmlFileException, FileNotFoundException, IllegalAgentsAmountException {
        JaxbToMachineTransformer jaxbToMachineTransformer = new JaxbToMachineTransformer();
        try {
            InputStream inputStream = new FileInputStream(machineDetailsXmlFilePath);
            if (!machineDetailsXmlFilePath.endsWith(".xml")) {
                throw new NotXmlFileException();
            }
            CTEEnigma CteEnigma = jaxbToMachineTransformer.deserializeFrom(inputStream);
            enigmaMachine = jaxbToMachineTransformer.transformJAXBClassesToEnigmaMachine(CteEnigma, decryptionManager, dictionary);

            if(statisticsAndHistoryAnalyzer != null) {
                statisticsAndHistoryAnalyzer.clear();
            }

            onMachineDetailsChanged();
            onDictionaryChanged();
            onKeyboardChanged();
            onMaxAgentsAmountChanged();
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
        catch (JAXBException | MachineNotExistsException | CloneNotSupportedException e){
            throw new RuntimeException();
        } catch (IllegalAgentsAmountException e) {
            throw e;
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
        resetMachine();
        setSettingsFormat(settingsSector);
        checkIfTheSettingsFormatInitialized();

        onCurrentCodeConfigurationChanged();
        onMachineDetailsChanged();
        onStatisticsAndHistoryChanged();
    }

    @Override
    public void startBruteForceDeciphering(BruteForceTask bruteForceTask, BruteForceUIAdapter bruteForceUiAdapter, Runnable onFinish) throws IllegalArgumentException {
        decryptionManager.initialize(bruteForceTask, bruteForceUiAdapter, onFinish, enigmaMachine, dictionary, mainController);
        decryptionManager.startDeciphering();
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

        enigmaMachine.setTheInitialCodeDefined(true);
    }
    private EnigmaMachine cloneMachine() throws CloneNotSupportedException {
        EnigmaMachine clonedMachine = new EnigmaMachine(enigmaMachine.cloneRotors(), enigmaMachine.cloneReflectors(), enigmaMachine.cloneKeyboard(), enigmaMachine.getNumOfActiveRotors());
        return clonedMachine;
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
    public void insertingInputFinished() throws MachineNotExistsException, CloneNotSupportedException {
        statisticsAndHistoryAnalyzer.addFullEncryptedAndOriginalStringsToProcessedStrings();
        ProcessedStringsFormat processedStringsFormat = statisticsAndHistoryAnalyzer.cloneProcessedStringsFormat();
        processedStringsFormat.addIndexFormat(enigmaMachine.getOriginalSettingsFormat().getIndexFormat());
        statisticsAndHistoryAnalyzer.addProcessedStringFormat(enigmaMachine.getOriginalSettingsFormat(), processedStringsFormat);
        enigmaMachine.getOriginalSettingsFormat().advanceIndexFormat();
        statisticsAndHistoryAnalyzer.advancedMessagesCounter();
        onStatisticsAndHistoryChanged();
        statisticsAndHistoryAnalyzer.clearProcessedStringsFormat();
    }
    @Override
    public String processInput(String inputToProcess) throws MachineNotExistsException, IllegalArgumentException, CloneNotSupportedException {
        if(inputToProcess.length() == 0) {
            throw new IllegalArgumentException("You must enter a message to process!!!");
        }
        OriginalStringFormat originalStringFormat = new OriginalStringFormat(inputToProcess.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        Instant start = Instant.now();
        String encryptedString = enigmaMachine.processedInput(inputToProcess);
        Instant end = Instant.now();
        long durationEncryptedTimeInNanoSeconds = Duration.between(start, end).toNanos();
        EncryptedStringFormat encryptedStringFormat = new EncryptedStringFormat(encryptedString.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));
        statisticsAndHistoryAnalyzer.addToOriginalAndEncryptedStringsAndTime(originalStringFormat,encryptedStringFormat, durationEncryptedTimeInNanoSeconds);
        statisticsAndHistoryAnalyzer.setIndexFormat(enigmaMachine.getOriginalSettingsFormat().getIndexFormat());
        onMachineDetailsChanged();
        onCurrentCodeConfigurationChanged();

        return encryptedString;
    }

    public String processInputsFromDictionary(String inputToProcess) throws Exception {
        List<String> inputToProcessAfterCleanFromExcludeChars;

        if(dictionary != null) {
            decryptionManager.setCodeConfigurationBeforeProcess(enigmaMachine.getCurrentSettingsFormat());
            inputToProcessAfterCleanFromExcludeChars = dictionary.validateWordsAfterCleanExcludeChars(Arrays.asList(inputToProcess.toUpperCase().split(" ")));
            String processedMessege = processInput(String. join(" ", inputToProcessAfterCleanFromExcludeChars));
            decryptionManager.setDecryptedMessage(processedMessege);

            return processedMessege;
        }
        else {
            throw new Exception("Error : There is no any dictionary in the machine");
        }
    }

    @Override
    public void saveStateMachineToFile(String path) throws IOException, MachineNotExistsException {
        MachineState.saveStateMachineToFile(path, this);
    }

    @Override
    public void loadStateMachineFromFile(String path) throws IOException, ClassNotFoundException {
        MachineState.loadStateMachineFromFile(path, this);
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

    public Set<String> getDictionary() {
        return dictionary.getDictionary();
    }

    public int getMaxAmountOfAgents() {
        return decryptionManager.getMaxCurrentAmountOfAgents();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void clearCurrentProccessedWord() {
        statisticsAndHistoryAnalyzer.clearCurrentOriginalAndEncryptedStrings();
    }
}