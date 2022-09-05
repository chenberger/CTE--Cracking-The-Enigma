package BruteForce;

import EnigmaMachine.Settings.SettingsFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DecipherStatistics {
    private Map<SettingsFormat, DecryptionCandidateFormat> decryptionCandidates;

    public DecipherStatistics() {
        this.decryptionCandidates = new HashMap<>();
    }

    public void addDecryptionCandidate(SettingsFormat settingsFormat, DecryptionCandidateFormat decryptionCandidateFormat){
        decryptionCandidates.put(settingsFormat, decryptionCandidateFormat);
    }

    @Override
    public String toString() {
        if(decryptionCandidates.size() == 0) {
            return ("No messages candidates were found during the decoding process" + System.lineSeparator());
        }
        else {
            return "Candidates found:" + System.lineSeparator() +
                    decryptionCandidates.entrySet().stream()
                            .map(pair -> pair.getValue().toString() + System.lineSeparator() +
                                    "Found at configuration: "+ pair.getKey().toString() + System.lineSeparator())
                            .collect(Collectors.joining(System.lineSeparator()));

        }
    }
}
