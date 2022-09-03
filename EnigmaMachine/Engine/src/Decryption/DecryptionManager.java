package Decryption;

import EnigmaMachineException.IllegalAgentsAmountException;

public class DecryptionManager {
    private int numberOfAgents;
    private static final int MIN_AGENTS_AMOUNT = 2;
    private static final int MAX_AGENTS_AMOUNT = 50;

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public void setNumberOfAgents(int numberOfAgents) throws IllegalAgentsAmountException {
        if(numberOfAgents >= MIN_AGENTS_AMOUNT && numberOfAgents <= MAX_AGENTS_AMOUNT) {
            this.numberOfAgents = numberOfAgents;
        }
        else {
            throw new IllegalAgentsAmountException(numberOfAgents, MIN_AGENTS_AMOUNT, MAX_AGENTS_AMOUNT);
        }
    }
}
