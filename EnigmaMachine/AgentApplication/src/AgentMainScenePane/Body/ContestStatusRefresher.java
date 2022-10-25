package AgentMainScenePane.Body;

import Utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static Utils.Constants.*;

public class ContestStatusRefresher extends TimerTask {
    private final Consumer<Boolean> updateContestStatus;

    public ContestStatusRefresher(Consumer<Boolean> updateContestStatus) {
        this.updateContestStatus = updateContestStatus;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(READY_MANAGER_SERVLET)
                .newBuilder()
                .addQueryParameter(TYPE, "Agent")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200){
                    String isReady = response.body().string();
                    Boolean isReadyBoolean = GSON_INSTANCE.fromJson(isReady, Boolean.class);
                    updateContestStatus.accept(isReadyBoolean);
                }
                else{
                    //System.out.println("Something went wrong with the request");
                }
                response.close();
            }
        });
    }
}
