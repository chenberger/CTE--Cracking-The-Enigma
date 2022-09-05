package BruteForce;

import javafx.concurrent.Task;

public class DecryptionCandidateTaskHandler extends Task<Void> {
    private BruteForceUIAdapter bruteForceUIAdapter;
    private DecryptionCandidateFormat decryptionCandidateFormat;

    public DecryptionCandidateTaskHandler(BruteForceUIAdapter bruteForceUIAdapter, DecryptionCandidateFormat decryptionCandidateFormat) {
        this.bruteForceUIAdapter = bruteForceUIAdapter;
        this.decryptionCandidateFormat = decryptionCandidateFormat;
    }

    @Override
    protected Void call() throws Exception {
        bruteForceUIAdapter.addDecryptionCandidate(decryptionCandidateFormat);
        return null;
    }
}
