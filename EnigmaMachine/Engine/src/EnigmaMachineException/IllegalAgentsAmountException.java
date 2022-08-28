package EnigmaMachineException;

public class IllegalAgentsAmountException extends Exception {
    private String startingMessage = "Error: Failed to initialize the amount of agents because of the following reasons:";
    private final int agentsAmount;
    private final int minAgentsAmount;
    private final int maxAgentsAmount;
    public IllegalAgentsAmountException(int agentsAmount, int minAgentsAmount, int maxAgentsAmount) {
        this.agentsAmount = agentsAmount;
        this.minAgentsAmount = minAgentsAmount;
        this.maxAgentsAmount = maxAgentsAmount;
    }

    public String getMessage() {
        return startingMessage + System.lineSeparator() +
                "The amount of the agents that was inserted is illegal." + System.lineSeparator() +
                "The amount of agents that was inserted is: " + agentsAmount + System.lineSeparator() +
                "The min amount of agents that can be inserted is: " + minAgentsAmount + System.lineSeparator() +
                "The max amount of agents that can be inserted is: " + maxAgentsAmount + System.lineSeparator();
    }
}
