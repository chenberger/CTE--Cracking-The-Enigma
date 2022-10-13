package DTO;

public class AlliesTasksProgressToLabels {
    private Long totalTasks;
    private Long tasksDone;
    private Long tasksPulled;

    public AlliesTasksProgressToLabels(Long totalTasks, Long tasksDone, Long tasksPulled) {
        this.totalTasks = totalTasks;
        this.tasksDone = tasksDone;
        this.tasksPulled = tasksPulled;
    }
    // Getters and Setters
    public Long getTotalTasks() {
        return totalTasks;
    }
    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }
    public Long getTasksDone() {
        return tasksDone;
    }
    public void setTasksDone(long tasksDone) {
        this.tasksDone = tasksDone;
    }
    public Long getTasksPulled() {
        return tasksPulled;
    }
    public void setTasksPulled(long tasksPulled) {
        this.tasksPulled = tasksPulled;
    }
    // End of Getters and Setters
}
