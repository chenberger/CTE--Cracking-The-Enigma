package DTO;

import BruteForce.DifficultyLevel;

public class BruteForceTask {
    private int amountOfAgents;
    private DifficultyLevel difficultTaskLevel;
    private int taskSize;

    public BruteForceTask(DifficultyLevel difficultTaskLevel, int taskSize, int amountOfAgents) {
        this.amountOfAgents = amountOfAgents;
        this.difficultTaskLevel = difficultTaskLevel;
        this.taskSize = taskSize;
    }

    public BruteForceTask(DifficultyLevel difficultTaskLevel, int taskSize) {
        this.difficultTaskLevel = difficultTaskLevel;
        this.taskSize = taskSize;

    }
    public BruteForceTask(){}

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public DifficultyLevel getDifficultTaskLevel() {
        return difficultTaskLevel;
    }

    public int getTaskSize() {
        return taskSize;
    }

    //public void setAmountOfAgents(int amountOfAgents) {
    //    this.amountOfAgents = amountOfAgents;
    //}

    public void setDifficultTaskLevel(DifficultyLevel difficultTaskLevel) {
        this.difficultTaskLevel = difficultTaskLevel;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }
}
