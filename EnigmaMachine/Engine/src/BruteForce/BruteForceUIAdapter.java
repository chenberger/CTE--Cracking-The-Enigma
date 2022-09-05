package BruteForce;

import javafx.application.Platform;

import java.util.function.Consumer;

public class BruteForceUIAdapter {
    private Consumer<DecryptionCandidateFormat> introduceNewCandidate;
    public void addDecryptionCandidate(DecryptionCandidateFormat decryptionCandidateFormat) {
        Platform.runLater(
                () -> {
                    introduceNewCandidate.accept(decryptionCandidateFormat);
                }
        );
    }
}
