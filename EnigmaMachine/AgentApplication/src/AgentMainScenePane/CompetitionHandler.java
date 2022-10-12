package AgentMainScenePane;

import AgentMainScenePane.Body.ContestAndTeamDataPane.ContestAndTeamDataPaneController;
import DTO.AgentCandidatesInformation;
import DTO.TaskToAgent;
import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.AgentsManager.AgentTask;
import Engine.AgentsManager.AgentWorker;
import Engine.Dictionary;
import Engine.EngineManager;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.Sector;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static AgentsServletsPaths.AgentServletsPaths.BATTLE_CANDIDATES_SERVLET;
import static AgentsServletsPaths.AgentServletsPaths.TASKS_SERVLET;
import static Utils.Constants.ACTION;

public class CompetitionHandler extends Thread {
    List<AgentCandidatesInformation> agentCandidatesInformationList;
    private long tasksCompleted;
    long numberOfTasksPulled;
    private ThreadPoolExecutor tasksPool;
    private BlockingQueue<TaskToAgent> contestTasksQueue;
    private EngineManager engineManager;
    private ContestAndTeamDataPaneController contestAndTeamDataPaneController;
    private OkHttpClient client;
    private String agentName;
    private BlockingQueue<Runnable> tasksQueue;


    public CompetitionHandler() {

    }

    public CompetitionHandler(ThreadPoolExecutor tasksPool, EngineManager engineManager, BlockingQueue<TaskToAgent> contestTasksQueue, String agentName, OkHttpClient client, ContestAndTeamDataPaneController contestAndTeamDataPaneController, BlockingQueue<Runnable> tasksQueue) {
        this.tasksPool = tasksPool;
        this.engineManager = engineManager;
        this.contestTasksQueue = contestTasksQueue;
        this.agentName = agentName;
        this.client = client;
        this.contestAndTeamDataPaneController = contestAndTeamDataPaneController;
        this.tasksQueue = tasksQueue;
        this.numberOfTasksPulled = 0;
        this.tasksCompleted = 0;
        this.agentCandidatesInformationList = new ArrayList<>();
    }


    @Override
    public void start() {
        while (contestAndTeamDataPaneController.isContestActive()) {
            try {
                if (tasksQueue.isEmpty()) {
                    getTaskFromServer();
                } else {
                    Thread.sleep(400);
                    sendCandidateInformationToServer();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Contest is over");
    }

    private void sendCandidateInformationToServer() {
        Gson gson = new Gson();
        if(agentCandidatesInformationList.size() > 0) {
            String json = gson.toJson(agentCandidatesInformationList);
            String finalUrl = HttpUrl.parse(BATTLE_CANDIDATES_SERVLET)
                    .newBuilder()
                    .addQueryParameter(ACTION, "sendCandidates")
                    .addQueryParameter("candidatesList", json)
                    .build().toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    new ErrorDialog(new Exception("Failed to send candidates to server"), "Failed to send candidates to server");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        new ErrorDialog(new Exception("Failed to send candidates to server: " + responseBody), "Failed to send candidates to server");
                    }
                    response.close();
                }
            });
        }
    }

    private void getTaskFromServer() throws IOException {
        String finalUrl = HttpUrl.parse(TASKS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getTasksInterval")
                .addQueryParameter("agentName", agentName)
                .build()
                .toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            Gson gson = new Gson();
            String responseString = response.body().string();
            List<TaskToAgent> tasksToAgent = Arrays.asList(gson.fromJson(responseString, TaskToAgent[].class));
            numberOfTasksPulled += tasksToAgent.size();

            //contestTasksQueue.addAll(tasksToAgent);
            try {
                for (int i = 0; i < tasksToAgent.size(); i++) {
                    AgentTask agentTask = getAgentTaskFromTaskToAgent(tasksToAgent.get(i));
                    AgentWorker agent = new AgentWorker(agentTask,agentName,numberOfTasksPulled,agentCandidatesInformationList);
                    tasksPool.execute(agent);
                }

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
    private AgentTask getAgentTaskFromTaskToAgent(TaskToAgent taskToAgent) throws CloneNotSupportedException {
        int taskSize = (int)taskToAgent.getTaskSize();
        EnigmaMachine enigmaMachine = new EnigmaMachine(engineManager.getEnigmaMachine().cloneRotors(), engineManager.getEnigmaMachine().cloneReflectors()
                , engineManager.getEnigmaMachine().cloneKeyboard(), engineManager.getEnigmaMachine().getNumOfActiveRotors());

        List<Sector> sectors = taskToAgent.getSectorsCodeAsJson().getSectors();
        sectors.get(0).setSectorInTheMachine(enigmaMachine);
        sectors.get(2).setSectorInTheMachine(enigmaMachine);
        sectors.get(3).setSectorInTheMachine(enigmaMachine);
        String encryptedMessage = taskToAgent.getEncryptedMessage();
        Dictionary dictionary = engineManager.getDictionaryObject();

        return new AgentTask(taskSize, (StartingRotorPositionSector) sectors.get(1), enigmaMachine, encryptedMessage, dictionary,agentName);

    }
}
