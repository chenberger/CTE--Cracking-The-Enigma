package CompetitionPane;

import Engine.UBoatManager.UBoat;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static UBoatServletsPaths.UBoatsServletsPaths.U_BOATS_LIST_SERVLET;
import static Utils.Constants.GSON_INSTANCE;

public class AlliesInBattleRefresher extends TimerTask {

    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<UBoat>> uBoatListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public AlliesInBattleRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<UBoat>> uBoatsListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.uBoatListConsumer = uBoatsListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        httpRequestLoggerConsumer.accept("About to invoke: " + U_BOATS_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(U_BOATS_LIST_SERVLET, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }
            //TODO chen: make sure that this is the syntax to transform u boat list from the server to the client
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUBoats = response.body().string();
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUBoats);
                UBoat[] usersNames = new Gson().fromJson(jsonArrayOfUBoats, UBoat[].class);
                uBoatListConsumer.accept(Arrays.asList(usersNames));
            }
        });
    }
}
