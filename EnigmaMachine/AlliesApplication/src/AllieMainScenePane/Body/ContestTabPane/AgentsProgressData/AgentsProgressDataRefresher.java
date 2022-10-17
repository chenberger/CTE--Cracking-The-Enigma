package AllieMainScenePane.Body.ContestTabPane.AgentsProgressData;

import DTO.AgentProgressDataToTable;
import DTO.AgentsProgressAndDataTable;
import DTO.AlliesTasksProgressToLabels;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_OPS_SERVLET;
import static Utils.Constants.ACTION;

public class AgentsProgressDataRefresher extends TimerTask {
    private Consumer<List<AgentsProgressAndDataTable>> updateAgentsProgressDataTable;
    private Consumer<AlliesTasksProgressToLabels> updateAlliesTasksProgressToLabels;

    public AgentsProgressDataRefresher(Consumer<List<AgentsProgressAndDataTable>> updateAgentsProgressDataTable, Consumer<AlliesTasksProgressToLabels> updateAlliesTasksProgressToLabels) {
        this.updateAgentsProgressDataTable = updateAgentsProgressDataTable;
        this.updateAlliesTasksProgressToLabels = updateAlliesTasksProgressToLabels;
    }

    @Override
    public void run() {
        getAlliesTasksProgressToLabels();
        getAgentsProgressDataToTable();
    }

    private void getAgentsProgressDataToTable() {

        String finalUrl = HttpUrl.parse(ALLIES_OPS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getAgentsProgressDataToTable")
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonAgentsProgressDataToTable = response.body().string();
                    Gson gson = new Gson();
                    List<AgentsProgressAndDataTable> agentProgressDataToTables = Arrays.asList(gson.fromJson(jsonAgentsProgressDataToTable, AgentsProgressAndDataTable[].class));
                    updateAgentsProgressDataTable.accept(agentProgressDataToTables);
                }
                else{
                    //System.out.println("Something went wrong with the request");
                }
                response.close();
            }
        });

    }

    private void getAlliesTasksProgressToLabels() {
        String finalUrl = HttpUrl.parse(ALLIES_OPS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getAlliesTasksProgressToLabels")
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonAlliesTasksProgressToLabels = response.body().string();
                    Gson gson = new Gson();
                    AlliesTasksProgressToLabels alliesTasksProgressToLabels = gson.fromJson(jsonAlliesTasksProgressToLabels, AlliesTasksProgressToLabels.class);
                    updateAlliesTasksProgressToLabels.accept(alliesTasksProgressToLabels);
                }
                else{
                    System.out.println("Something went wrong with the request");
                }
                response.close();
            }
        });
    }
}
