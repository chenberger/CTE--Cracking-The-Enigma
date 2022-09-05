package DTO;

import BruteForce.DifficultyLevel;

public class BruteForceTask {
    private int amountOfAgents;
    private DifficultyLevel difficultTaskLevel;
    private int taskSize;

    public BruteForceTask(int amountOfAgents, DifficultyLevel difficultTaskLevel, int taskSize) {
        this.amountOfAgents = amountOfAgents;
        this.difficultTaskLevel = difficultTaskLevel;
        this.taskSize = taskSize;
    }

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public DifficultyLevel getDifficultTaskLevel() {
        return difficultTaskLevel;
    }

    public int getTaskSize() {
        return taskSize;
    }
}
