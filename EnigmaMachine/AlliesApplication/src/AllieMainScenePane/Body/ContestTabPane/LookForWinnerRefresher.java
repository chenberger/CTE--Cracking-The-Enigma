package AllieMainScenePane.Body.ContestTabPane;

import DesktopUserInterface.MainScene.ErrorDialog;
import Utils.HttpClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Utils.Constants.*;

public class LookForWinnerRefresher extends TimerTask {
    private Consumer<String> updateWinnerFound;
    public LookForWinnerRefresher(Consumer<String>  notifyIfWinnerFound) {
        this.updateWinnerFound = notifyIfWinnerFound;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(TASKS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "checkIfContestIsOver")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    new ErrorDialog(new Exception("Something went wrong with the request"), "Something went wrong with the request");
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == HttpServletResponse.SC_OK){
                    String winnerFound = GSON_INSTANCE.fromJson(response.body().string(), String.class);
                    updateWinnerFound.accept(winnerFound);
                }
                response.close();
            }
        });
    }
}
