package Decryption;

import java.io.Serializable;

public class DecryptionCandidateFormat implements Serializable {
    private String candidate;
    private int timeInNanoseconds;
    private int agentId;

    public DecryptionCandidateFormat(String candidate, int timeInNanoseconds, int agentId) {
        this.candidate = candidate;
        this.timeInNanoseconds = timeInNanoseconds;
        this.agentId = agentId;
    }

    @Override public String toString() {
        return "Candidate: " + candidate + "." +System.lineSeparator()
                + "Found in: "+ timeInNanoseconds + " nano seconds."+
                "Found by agent: "+ agentId + "." + System.lineSeparator();
    }
}
