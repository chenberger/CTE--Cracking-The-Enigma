package BruteForce;

import java.util.concurrent.ThreadFactory;

public class AgentThreadFactory implements ThreadFactory {

    private static int continuousId;

    static {
        resetContinuousId();
    }

    public static void resetContinuousId() {
        continuousId = 1;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, String.valueOf(continuousId++));
    }
}
