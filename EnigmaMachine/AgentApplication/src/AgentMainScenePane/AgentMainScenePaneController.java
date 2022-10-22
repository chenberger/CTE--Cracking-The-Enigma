package AgentMainScenePane;

import AgentMainScenePane.Body.AgentCandidates.AgentCandidatesPaneController;
import AgentMainScenePane.Body.AgentProgressAndStatusPane.AgentProgressAndStatusPaneController;
import AgentMainScenePane.Body.ContestAndTeamDataPane.ContestAndTeamDataPaneController;
import DTO.DataToAgentApplicationTableView;
import DTO.DataToInitializeMachine;
import DTO.TaskToAgent;
import Engine.AgentsManager.AgentThreadFactory;
import Engine.Dictionary;
import Engine.EngineManager;
import EnigmaMachine.EnigmaMachine;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;

import java.io.IOException;
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
    private CompetitionHandler competitionHandler;
    private OkHttpClient client;
    private boolean isMachineExists;
    private boolean isDictionaryExists;

    String agentName;
    private boolean participateInContest = false;
    private BlockingQueue<TaskToAgent> contestTasksQueue;
    private EngineManager engineManager;
    private ThreadPoolExecutor tasksPool;
    private BlockingQueue<Runnable> tasksQueue;
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
        isDictionaryExists = false;
        isMachineExists = false;
        client = new OkHttpClient();
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

    public void startBruteForce()  {
        try {
            checkIfParticipateInBattle();
            if (participateInContest) {
                contestTasksQueue = new LinkedBlockingQueue<>();
                tasksQueue = new LinkedBlockingQueue<>();
                tasksPool = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 5000, TimeUnit.SECONDS, tasksQueue, new AgentThreadFactory(numberOfThreads), new ThreadPoolExecutor.CallerRunsPolicy());
                getBattlesEnigmaMachine();
                getBattlesDictionary();
                startWorking();
        }
        } catch(IOException e){
            e.printStackTrace();
        }

    }



    private void getBattlesEnigmaMachine() throws IOException {
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getMachineDataForInitialize")
                .addQueryParameter("agentName", agentName)
                .build()
                .toString();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
               Gson gson = new Gson();
               String responseString = response.body().string();
               DataToInitializeMachine dataToInitializeMachine = gson.fromJson(responseString, DataToInitializeMachine.class);
               engineManager.setEnigmaMachine(new EnigmaMachine(dataToInitializeMachine.getRotors(), dataToInitializeMachine.getReflectors()
                       ,dataToInitializeMachine.getKeyboard(), dataToInitializeMachine.getAmountCurrentRotorsInUse()));
        }
        response.close();
        //HttpClientUtil.runAsync(finalUrl, new Callback() {
        //    @Override
        //    public void onFailure(okhttp3.Call call, java.io.IOException e) {
        //        e.printStackTrace();
        //    }
////
        //    @Override
        //    public void onResponse(Call call, Response response) throws java.io.IOException {
        //        if(response.code() == 200) {
        //            Gson gson = new Gson();
        //            String responseString = response.body().string();
        //            DataToInitializeMachine dataToInitializeMachine = gson.fromJson(responseString, DataToInitializeMachine.class);
        //            engineManager.setEnigmaMachine(new EnigmaMachine(dataToInitializeMachine.getRotors(), dataToInitializeMachine.getReflectors()
        //                    ,dataToInitializeMachine.getKeyboard(), dataToInitializeMachine.getAmountCurrentRotorsInUse()));
        //            isMachineExists = true;
        //        }
        //        response.close();
        //    }
        //});
    }

    private void  getBattlesDictionary() throws IOException {
        String finalUrl = HttpUrl.parse(DICTIONARY_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getDictionaryObject")
                .addQueryParameter("agentName", agentName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            Gson gson = new Gson();
            String responseString = response.body().string();
            Dictionary dictionary = gson.fromJson(responseString, Dictionary.class);
            engineManager.setDictionary(dictionary);
        }
        //HttpClientUtil.runAsync(finalUrl, new Callback() {
        //    @Override
        //    public void onFailure(okhttp3.Call call, java.io.IOException e) {
        //        e.printStackTrace();
        //    }
////
        //    @Override
        //    public void onResponse(Call call, Response response) throws java.io.IOException {
        //        if(response.code() == 200) {
        //            Gson gson = new Gson();
        //            String responseString = response.body().string();
        //            Engine.Dictionary dictionary = gson.fromJson(responseString, Dictionary.class);
        //            engineManager.setDictionary(dictionary);
        //            isDictionaryExists = true;
////
        //        }
        //    }
        //});
    }
    private void startWorking() {

        competitionHandler = new CompetitionHandler(new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 5000, TimeUnit.SECONDS, tasksQueue, new AgentThreadFactory(numberOfThreads), new ThreadPoolExecutor.CallerRunsPolicy())
        , engineManager, new LinkedBlockingQueue<>(), agentName, client,this, new LinkedBlockingQueue<>() );
        String threadName = competitionHandler.getName();
        competitionHandler.start();

    }

    private void checkIfParticipateInBattle() throws IOException {
        String finalUrl = HttpUrl.parse(ALLIES_OPS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "participateInBattle")
                .addQueryParameter("agentName", agentName)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            participateInContest = true;
        }
        response.close();
        //HttpClientUtil.runAsync(finalUrl, new Callback() {
        //    @Override
        //    public void onFailure(okhttp3.Call call, java.io.IOException e) {
        //        new ErrorDialog(e, "Error while trying to get participate value");
        //    }
//
        //    @Override
        //    public void onResponse(Call call, Response response) throws java.io.IOException {
        //        if (response.code() == 200) {
        //            Gson gson = new Gson();
        //            String responseString = response.body().string();
        //            //TODO: change to boolean from json
        //            participateInContest = gson.fromJson(responseString, Boolean.class);
        //        } else {
        //            new ErrorDialog(new Exception(response.body().string()), "Error while trying to get participate value");
        //        }
//
        //    }
        //});
    }

    public boolean isContestActive() {
        return contestAndTeamDataPaneController.isContestActive();
    }

    public void updateTasksCompleted(long tasksCompleted, int numberOfCandidatesFound) {
        agentProgressAndDataPaneController.updateTasksCompleted(tasksCompleted, numberOfCandidatesFound);
    }


    public void updateTasksPulled(long numberOfTasksPulled) {
        agentProgressAndDataPaneController.updateTasksPulled(numberOfTasksPulled);
    }

    public void updateCandidatesTable(List<DataToAgentApplicationTableView> agentCandidatesInformationList) {
        agentCandidatesPaneController.updateCandidatesTable(agentCandidatesInformationList);
    }


    //HttpClientUtil.runAsync(finalUrl, new Callback() {
       //    @Override
       //    public void onFailure(okhttp3.Call call, java.io.IOException e) {
       //        e.printStackTrace();
       //    }

       //    @Override
       //    public void onResponse(Call call, Response response) throws java.io.IOException {
       //        if(response.code() == 200) {
       //            Gson gson = new Gson();
       //            String responseString = response.body().string();
       //            List<TaskToAgent> tasksToAgent = Arrays.asList(gson.fromJson(responseString, TaskToAgent[].class));

       //            contestTasksQueue.addAll(tasksToAgent);
       //            try {
       //                 for(int i = 0; i < tasksToAgent.size(); i++) {
       //                     AgentTask agentTask = getAgentTaskFromTaskToAgent(tasksToAgent.get(i));
       //                     AgentWorker agent = new AgentWorker(agentTask);
       //                     tasksPool.execute(agent);
       //                 }

       //            } catch (CloneNotSupportedException e) {
       //                e.printStackTrace();
       //            }
       //        }

       //    }
       //});
}
