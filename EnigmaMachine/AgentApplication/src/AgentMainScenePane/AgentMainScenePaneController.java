package AgentMainScenePane;

import AgentMainScenePane.Body.AgentCandidates.AgentCandidatesPaneController;
import AgentMainScenePane.Body.AgentProgressAndStatusPane.AgentProgressAndStatusPaneController;
import AgentMainScenePane.Body.ContestAndTeamDataPane.ContestAndTeamDataPaneController;
import DTO.DataToInitializeMachine;
import DTO.TaskToAgent;
import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.AgentsManager.AgentTask;
import Engine.AgentsManager.AgentThreadFactory;
import Engine.AgentsManager.AgentWorker;
import Engine.Dictionary;
import Engine.EngineManager;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.Settings.ReflectorIdSector;
import EnigmaMachine.Settings.Sector;
import EnigmaMachine.Settings.StartingRotorPositionSector;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_OPS_SERVLET;
import static UBoatServletsPaths.UBoatsServletsPaths.DICTIONARY_SERVLET;
import static UBoatServletsPaths.UBoatsServletsPaths.GET_MACHINE_CONFIG_SERVLET;
import static Utils.Constants.ACTION;

public class AgentMainScenePaneController {
    String agentName;
    private boolean participateInContest = false;
    private BlockingQueue<TaskToAgent> contestTasksQueue;
    private EngineManager engineManager;
    private ThreadPoolExecutor tasksPool;
    BlockingQueue<Runnable> tasksQueue;
    private int numberOfThreads;
    private Long tasksPullingInterval;

    @FXML private AnchorPane agentCandidatesPane;
    @FXML private AgentCandidatesPaneController agentCandidatesPaneController;
    @FXML private AnchorPane agentProgressAndDataPane;
    @FXML private AgentProgressAndStatusPaneController agentProgressAndDataPaneController;
    @FXML private AnchorPane contestAndTeamDataPane;
    @FXML private ContestAndTeamDataPaneController contestAndTeamDataPaneController;
    @FXML public void initialize() {
        if (agentCandidatesPaneController != null) {
            agentCandidatesPaneController.setAgentMainSceneController(this);
        }
        if(agentProgressAndDataPaneController != null) {
            agentProgressAndDataPaneController.setAgentMainSceneController(this);
        }
        if(contestAndTeamDataPaneController != null) {
            contestAndTeamDataPaneController.setAgentMainSceneController(this);
        }
        engineManager = new EngineManager();
    }
    public void setActive() {
        agentCandidatesPaneController.startRefreshing();
        agentProgressAndDataPaneController.startRefreshing();
        contestAndTeamDataPaneController.startRefreshing();
    }
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }
    public void setTasksPullingInterval(Long tasksPullingInterval) {
        this.tasksPullingInterval = tasksPullingInterval;
    }

    public void onLogOutButtonClicked(ActionEvent actionEvent) {
    }

    public void startContest() {
    }

    public void startBruteForce() {
        getBattlesEnigmaMachine();
        getBattlesDictionary();
        startWorking();
    }



    private void getBattlesEnigmaMachine() {
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getMachineDataForInitialize")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                if(response.code() == 200) {
                    Gson gson = new Gson();
                    String responseString = response.body().string();
                    DataToInitializeMachine dataToInitializeMachine = gson.fromJson(responseString, DataToInitializeMachine.class);
                    engineManager.setEnigmaMachine(new EnigmaMachine(dataToInitializeMachine.getRotors(), dataToInitializeMachine.getReflectors()
                            ,dataToInitializeMachine.getKeyboard(), dataToInitializeMachine.getAmountCurrentRotorsInUse()));
                }
                
            }
        });
    }

    private void  getBattlesDictionary(){
        String finalUrl = HttpUrl.parse(DICTIONARY_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getDictionaryObject")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                if(response.code() == 200) {
                    Gson gson = new Gson();
                    String responseString = response.body().string();
                    Engine.Dictionary dictionary = gson.fromJson(responseString, Dictionary.class);
                    engineManager.setDictionary(dictionary);

                }
            }
        });
    }
    private void startWorking() {
        checkIfParticipateInBattle();
        if(participateInContest) {
            contestTasksQueue = new LinkedBlockingQueue<>();
            tasksQueue = new LinkedBlockingQueue<>();
            tasksPool = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 5000, TimeUnit.SECONDS, tasksQueue, new AgentThreadFactory(numberOfThreads), new ThreadPoolExecutor.CallerRunsPolicy());
            while (contestAndTeamDataPaneController.isContestActive()) {
                try {
                    if (tasksQueue.size() == 0) {
                        getTaskFromServer();
                    } else {
                        tasksPool.execute(tasksQueue.take());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void checkIfParticipateInBattle() {
        String finalUrl = HttpUrl.parse(ALLIES_OPS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "participateInBattle")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                new ErrorDialog(e, "Error while trying to get participate value");
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    String responseString = response.body().string();
                    participateInContest = gson.fromJson(responseString, Boolean.class);
                } else {
                    new ErrorDialog(new Exception(response.body().string()), "Error while trying to get participate value");
                }

            }
        });
    }

    private void getTaskFromServer() {
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getTasksInterval")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                if(response.code() == 200) {
                    Gson gson = new Gson();
                    String responseString = response.body().string();
                    List<TaskToAgent> tasksToAgent = Arrays.asList(gson.fromJson(responseString, TaskToAgent[].class));

                    contestTasksQueue.addAll(tasksToAgent);
                    try {
                         for(int i = 0; i < tasksToAgent.size(); i++) {
                             AgentTask agentTask = getAgentTaskFromTaskToAgent(tasksToAgent.get(i));
                             AgentWorker agent = new AgentWorker(agentTask);
                             tasksPool.execute(agent);
                         }

                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private AgentTask getAgentTaskFromTaskToAgent(TaskToAgent taskToAgent) throws CloneNotSupportedException {
        int taskSize = (int)taskToAgent.getTaskSize();
        StartingRotorPositionSector startingRotorPositionSector = taskToAgent.getCurrentStartingRotorsPositions();
        EnigmaMachine enigmaMachine = engineManager.getEnigmaMachine().cloneMachine();
        List<Sector> sectors = taskToAgent.getSectorsCodeAsJson().getSectors();
        sectors.get(0).setSectorInTheMachine(enigmaMachine);
        sectors.get(2).setSectorInTheMachine(enigmaMachine);
        sectors.get(3).setSectorInTheMachine(enigmaMachine);
        String encryptedMessage = taskToAgent.getEncryptedMessage();
        Dictionary dictionary = engineManager.getDictionaryObject();
        return new AgentTask(taskSize, startingRotorPositionSector, enigmaMachine, encryptedMessage, dictionary,agentName);

    }
}
