package Decryption;

import EnigmaMachineException.IllegalAgentsAmountException;

public class DecryptionManager {
    private int agentsAmount;
    private static final int MIN_AGENTS_AMOUNT = 2;
    private static final int MAX_AGENTS_AMOUNT = 50;

    public int getAgentsAmount() {
        return agentsAmount;
    }

    public void setAgentsAmount(int agentsAmount) throws IllegalAgentsAmountException {
        if(agentsAmount >= MIN_AGENTS_AMOUNT && agentsAmount <= MAX_AGENTS_AMOUNT) {
            this.agentsAmount = agentsAmount;
        }
        else {
            throw new IllegalAgentsAmountException(agentsAmount, MIN_AGENTS_AMOUNT, MAX_AGENTS_AMOUNT);
        }
    }
}
