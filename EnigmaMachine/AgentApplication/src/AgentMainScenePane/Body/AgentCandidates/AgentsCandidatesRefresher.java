package AgentMainScenePane.Body.AgentCandidates;

import DTO.DataToAgentApplicationTableView;
import Utils.HttpClientUtil;
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

import static AgentsServletsPaths.AgentServletsPaths.BATTLE_CANDIDATES_SERVLET;
import static Utils.Constants.ACTION;
import static Utils.Constants.GSON_INSTANCE;

public class AgentsCandidatesRefresher extends TimerTask {
    private Consumer<List<DataToAgentApplicationTableView>> dataToAgentApplicationTableViewConsumer;

    public AgentsCandidatesRefresher(Consumer<List<DataToAgentApplicationTableView>> updateCandidatesTable) {
        this.dataToAgentApplicationTableViewConsumer = updateCandidatesTable;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl.parse(BATTLE_CANDIDATES_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getAgentsCandidates")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200){
                    String responseString = response.body().string();
                    List<DataToAgentApplicationTableView> dataToAgentApplicationTableViewList = Arrays.asList(GSON_INSTANCE.fromJson(responseString, DataToAgentApplicationTableView[].class));
                    dataToAgentApplicationTableViewConsumer.accept(dataToAgentApplicationTableViewList);
                }
                response.close();
            }
        });
    }

}
