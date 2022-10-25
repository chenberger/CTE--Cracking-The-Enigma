package DTO;

public class AgentProgressData {
    private Long tasksPulled;
    private Long tasksProcessed;
    private Integer CandidatesFound;
    private Long numberOfTasksInQueue;

    public AgentProgressData(Long tasksPulled, Long tasksProcessed, Integer CandidatesFound, Long numberOfTasksInQueue) {
        this.tasksPulled = tasksPulled;
        this.tasksProcessed = tasksProcessed;
        this.CandidatesFound = CandidatesFound;
    }
    // Getters and Setters
    public Long getTasksPulled() {
        return tasksPulled;
    }
    public Long getTasksProcessed() {
        return tasksProcessed;
    }
    public Integer getCandidatesFound() {
        return CandidatesFound;
    }
    public void setTasksPulled(Long tasksPulled) {
        this.tasksPulled = tasksPulled;
    }
    public void setTasksProcessed(Long tasksProcessed) {
        this.tasksProcessed = tasksProcessed;
    }
    public void setCandidatesFound(Integer CandidatesFound) {
        this.CandidatesFound = CandidatesFound;
    }

    public Long getNumberOfTasksInQueue() {
        return numberOfTasksInQueue;
    }
    public void setNumberOfTasksInQueue(Long numberOfTasksInQueue) {
        this.numberOfTasksInQueue = numberOfTasksInQueue;
    }
    // end of Getters and Setters
}
