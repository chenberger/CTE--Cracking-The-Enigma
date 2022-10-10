package DTO;

public class AgentProgressData {
    private Long tasksPulled;
    private Long tasksProcessed;
    private Integer CandidatesFound;

    public AgentProgressData(Long tasksPulled, Long tasksProcessed, Integer CandidatesFound) {
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
    // end of Getters and Setters
}
