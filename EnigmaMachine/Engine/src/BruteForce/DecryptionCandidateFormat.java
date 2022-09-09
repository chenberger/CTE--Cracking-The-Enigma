package BruteForce;

import java.io.Serializable;

public class DecryptionCandidateFormat implements Serializable {
    private String candidate;
    private String currentCodeConfigurationFormat;
    private long timeInNanoseconds;
    private String agentId;

    public DecryptionCandidateFormat(String candidate, long timeInNanoseconds, String agentId, String currentCodeConfigurationFormat) {
        this.candidate = candidate;
        this.timeInNanoseconds = timeInNanoseconds;
        this.agentId = agentId;
        this.currentCodeConfigurationFormat = currentCodeConfigurationFormat;
    }

    @Override public String toString() {
        return  "Agent id: " + agentId + System.lineSeparator() +
                "Code configuration: " + currentCodeConfigurationFormat +
                "Candidate message: " + candidate + System.lineSeparator() +
                "Found in: "+ timeInNanoseconds + " nano seconds";
    }
}
