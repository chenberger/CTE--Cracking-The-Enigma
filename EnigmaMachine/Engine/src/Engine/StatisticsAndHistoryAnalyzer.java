package Engine;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsAndHistoryAnalyzer {
    //private static final String CODE_CONFIGURATION_INDENTATION = ""
    private static final String startingMessage = "All of the Code configurations that was set in the Enigma machine," +
                                                  " with describing of the encrypted strings of each code configuration are:" + System.lineSeparator();
    private static final String startingCodeConfigurationsMessage = "Code configuration: ";
    private final Map<SettingsFormat, List<ProcessedStringsFormat>> statisticsAndHistoryFormat;
    private int messagesCounter;

    public StatisticsAndHistoryAnalyzer() {
        messagesCounter = 0;
        statisticsAndHistoryFormat = new HashMap<>();
    }


    public void addSettingConfiguration(SettingsFormat settingsFormat) {
        statisticsAndHistoryFormat.put(settingsFormat, new ArrayList<>());
    }

    public void addProcessedStringFormat(SettingsFormat settingsFormat, ProcessedStringsFormat processedStringsFormat) {
        Boolean isSettingFound = false;
        for(SettingsFormat setting : statisticsAndHistoryFormat.keySet()) {
            if(setting.equals(settingsFormat)) {
                statisticsAndHistoryFormat.get(setting).add(processedStringsFormat);
                isSettingFound = true;
                break;
            }
        }

        if(!isSettingFound) {
            statisticsAndHistoryFormat.put(settingsFormat, new ArrayList<>(Arrays.asList(processedStringsFormat)));
        }
    }

    public void clear() {
        statisticsAndHistoryFormat.clear();
    }

    @Override
    public String toString() {
        if(statisticsAndHistoryFormat.size() == 0) {
            return "As no initialization or encryption in the system was performed, there are no history or statistics to display for the machine." + System.lineSeparator();
        }
        else {
            return startingMessage + statisticsAndHistoryFormat.entrySet().stream()
                    .map(pair -> startingCodeConfigurationsMessage + pair.getKey().toString() + System.lineSeparator() + pair.getValue()
                            .stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator())
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }

    public int getMessagesCounter() {
        return messagesCounter;
    }

    public void advancedMessagesCounter() {
        this.messagesCounter++;
    }
}
