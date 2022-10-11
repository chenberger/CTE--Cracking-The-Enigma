package BruteForce;

public enum DifficultyLevel implements SetTask {
    EASY(){
        @Override
        public void setTask(TasksProducer tasksManager) throws Exception {
            tasksManager.setEasyTasks();
        }
    },
    MEDIUM(){
        @Override
        public void setTask(TasksProducer tasksManager) throws Exception {
            tasksManager.setMediumTasks();
        }
    },
    HARD(){
        @Override
        public void setTask(TasksProducer tasksManager) throws Exception {
            tasksManager.setHardTasks();
        }
    },
    IMPOSSIBLE(){
        @Override
        public void setTask(TasksProducer tasksManager) throws Exception {
            tasksManager.setImpossibleTasks();
        }
    };

}
