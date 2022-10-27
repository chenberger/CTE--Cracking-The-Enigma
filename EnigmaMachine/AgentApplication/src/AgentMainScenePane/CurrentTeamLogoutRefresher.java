package AgentMainScenePane;

import Utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Utils.Constants.ACTION;
import static Utils.Constants.LOGOUT_SERVLET;

public class CurrentTeamLogoutRefresher extends TimerTask {
    private final Consumer<Boolean> registrationStatusConsumer;

    public CurrentTeamLogoutRefresher(Consumer<Boolean> registerationStatusConsumer) {
        this.registrationStatusConsumer = registerationStatusConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(LOGOUT_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "checkIfAgentTeamLoggedOut")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                registrationStatusConsumer.accept(false);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    registrationStatusConsumer.accept(true);
                }
                else{
                    registrationStatusConsumer.accept(false);
                }
                response.close();
            }
        });
    }
}
