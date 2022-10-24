package AllieMainScenePane.Body.ContestTabPane.CurrentContest;

import DTO.OnLineContestsDataToTable;
import DTO.OnLineContestsTable;
import Utils.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static AllieMainScenePane.AllieMainScenePaneController.onLineContestFromJson;
import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_OPS_SERVLET;
import static Utils.Constants.GET_CURRENT_CONTEST_DATA;
import static Utils.Constants.GSON_INSTANCE;

public class CurrentContestDataRefresher extends TimerTask {
    private Consumer<OnLineContestsTable> currentContestDataConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public CurrentContestDataRefresher(BooleanProperty shouldUpdate, Consumer<OnLineContestsTable> currentContestDataConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.currentContestDataConsumer = currentContestDataConsumer;
        requestNumber = 0;
    }
    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }
        final int finalRequestNumber = ++requestNumber;
       // System.out.println("About to invoke: " + " | Users Request # " + finalRequestNumber);
        String finalUrl = HttpUrl.parse(ALLIES_OPS_SERVLET)
                .newBuilder()
                .addQueryParameter("action", GET_CURRENT_CONTEST_DATA)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }
            //TODO chen: make sure that this is the syntax to transform u boat list from the server to the client
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200){
                    OnLineContestsTable onLineContestsTable = onLineContestFromJson(response.body().string());
                    currentContestDataConsumer.accept(onLineContestsTable);
                }
                else{
                    OnLineContestsTable onLineContestsTable = new OnLineContestsTable("N/A","N/A","N/A","N/A","N/A");
                    currentContestDataConsumer.accept(onLineContestsTable);
                }
            }
        });
    }
}
