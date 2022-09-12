package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import java.util.function.Consumer;

public class BruteForceFinished implements Runnable{
    private Consumer<String> pauseCancelLabel;
    private Consumer<String> startStopLabel;
    private Consumer<Boolean> isBruteForceFinished;
    private String startLabelToSetWhenFinished;
    private String stopLabelToSetWhenFinished;

    public BruteForceFinished(Consumer<String> pauseCancelLabel, Consumer<String> startStopLabel, Consumer<Boolean> isBruteForceFinished, String startLabelToSetWhenFinished, String stopLabelToSetWhenFinished) {
        this.pauseCancelLabel = pauseCancelLabel;
        this.startStopLabel = startStopLabel;
        this.isBruteForceFinished = isBruteForceFinished;
        this.startLabelToSetWhenFinished = startLabelToSetWhenFinished;
        this.stopLabelToSetWhenFinished = stopLabelToSetWhenFinished;
    }

    @Override
    public void run() {
        startStopLabel.accept(startLabelToSetWhenFinished);
        pauseCancelLabel.accept(stopLabelToSetWhenFinished);
        isBruteForceFinished.accept(false);
    }
}
