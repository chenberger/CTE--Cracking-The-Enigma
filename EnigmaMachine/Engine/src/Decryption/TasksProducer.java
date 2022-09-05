package Decryption;

import javax.swing.event.ListDataEvent;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TasksProducer implements Runnable {

    private final BlockingQueue<List<List<String>>> Tasksqueue;

    @Override
    public void run() {
        enterTasksToQueue();
    }

    private void enterTasksToQueue() {
    }


    public TasksProducer(BlockingQueue<List<List<String>>> tasksqueue) {
        this.Tasksqueue = tasksqueue;
    }
}