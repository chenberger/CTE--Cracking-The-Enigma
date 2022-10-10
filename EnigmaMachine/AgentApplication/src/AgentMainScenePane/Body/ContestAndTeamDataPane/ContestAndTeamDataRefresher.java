package AgentMainScenePane.Body.ContestAndTeamDataPane;

import DTO.AgentContestAndTeamData;
import Utils.HttpClientUtil;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static AgentsServletsPaths.AgentServletsPaths.DATA_TO_AGENT_SERVLET;
import static Utils.Constants.ACTION;
import static Utils.Constants.GSON_INSTANCE;

public class ContestAndTeamDataRefresher extends TimerTask {

    private Consumer<AgentContestAndTeamData> contestAndTeamDataConsumer;
    public ContestAndTeamDataRefresher(Consumer<AgentContestAndTeamData> contestAndTeamDataConsumer) {
        this.contestAndTeamDataConsumer = contestAndTeamDataConsumer;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl.parse(DATA_TO_AGENT_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getContestAndTeamData")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                AgentContestAndTeamData agentContestAndTeamData = GSON_INSTANCE.fromJson(responseString, AgentContestAndTeamData.class);
                contestAndTeamDataConsumer.accept(agentContestAndTeamData);
            }
        });
    }
}
