package BruteForce;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DecipherStatistics {
    private Map<Integer, DecryptionCandidateFormat> decryptionCandidates;

    public DecipherStatistics() {
        this.decryptionCandidates = new HashMap<>();
    }

    public void addDecryptionCandidate(Integer taskId, DecryptionCandidateFormat decryptionCandidateFormat){
        decryptionCandidates.put(taskId, decryptionCandidateFormat);
    }

    @Override
    public String toString() {
        if(decryptionCandidates.size() == 0) {
            return ("No messages candidates were found during the decoding process" + System.lineSeparator());
        }
        else {
            AtomicInteger index = new AtomicInteger(1);
            return decryptionCandidates.entrySet().stream()
                            .map(pair -> index.getAndIncrement() + ") " + pair.getValue().toString() + System.lineSeparator())
                            .collect(Collectors.joining(System.lineSeparator()));

        }
    }
}
