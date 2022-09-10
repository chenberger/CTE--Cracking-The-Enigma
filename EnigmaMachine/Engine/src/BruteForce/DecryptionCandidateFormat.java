package BruteForce;

import java.io.Serializable;

public class DecryptionCandidateFormat implements Serializable {
    private String candidate;
    private String currentCodeConfigurationFormat;
    private long timeInNanoseconds;

    public DecryptionCandidateFormat(String candidate, long timeInNanoseconds, String currentCodeConfigurationFormat) {
        this.candidate = candidate;
        this.timeInNanoseconds = timeInNanoseconds;
        this.currentCodeConfigurationFormat = currentCodeConfigurationFormat;
    }

    @Override public String toString() {
        return  "Code configuration: " + currentCodeConfigurationFormat + System.lineSeparator() +
                "Candidate message: " + candidate + System.lineSeparator() +
                "Found in: "+ timeInNanoseconds + " nano seconds" + System.lineSeparator();
    }
}
