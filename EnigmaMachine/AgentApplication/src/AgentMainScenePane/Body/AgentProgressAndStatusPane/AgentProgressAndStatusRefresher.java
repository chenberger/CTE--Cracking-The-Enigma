package AgentMainScenePane.Body.AgentProgressAndStatusPane;

import DTO.AgentProgressData;
import Utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static AgentsServletsPaths.AgentServletsPaths.BATTLE_CANDIDATES_SERVLET;
import static Utils.Constants.ACTION;
import static Utils.Constants.GSON_INSTANCE;

public class AgentProgressAndStatusRefresher extends TimerTask {
    private Consumer<AgentProgressData> agentProgressDataConsumer;

    public AgentProgressAndStatusRefresher(Consumer<AgentProgressData> agentProgressDataConsumer) {
        this.agentProgressDataConsumer = agentProgressDataConsumer;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(BATTLE_CANDIDATES_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getAgentsTasksProgress")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               if(response.code() == 200){
                    String body = response.body().string();
                    AgentProgressData agentProgressData = GSON_INSTANCE.fromJson(body, AgentProgressData.class);
                    agentProgressDataConsumer.accept(agentProgressData);
                }
                response.close();
            }
        });
    }

}
