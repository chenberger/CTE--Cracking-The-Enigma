package ConsoleUserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
/*        ConsoleUserInterface consoleUserInterface = new ConsoleUserInterface();
        consoleUserInterface.run();*/

        ThreadPoolExecutor threadExecutor = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
        List<PrintTask> tasksList = new ArrayList<>();
        for (int i=0 ; i < 60 ; i++) {
            tasksList.add(new PrintTask("task" + (i+1), new Random().nextInt(5000)));
        }

        System.out.println("Starting threads");

        // start threads and place in runnable state
        for (int i = 0; i < tasksList.size(); i++) {
            System.out.println("Adding task " + tasksList.get(i).getName() + " to thread pool.");

            try {
                threadExecutor.getQueue().put(tasksList.get(i));
            }
            catch(RejectedExecutionException e) {
                System.out.println("Error!!!");
                i--;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class PrintTask implements Runnable {

    private int sleepTime;
    private String name;

    // assign name to thread
    public PrintTask(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = 3000 + sleepTime;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            System.out.println("==================================================================================================" + System.lineSeparator() +
                    "Thread: " + Thread.currentThread().getName() + " " + name + " going to sleep for " + sleepTime + " milliseconds." + System.lineSeparator() +
                            "==================================================================================================");
            Thread.sleep(sleepTime);
        } // if thread interrupted while sleeping, print stack trace
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
