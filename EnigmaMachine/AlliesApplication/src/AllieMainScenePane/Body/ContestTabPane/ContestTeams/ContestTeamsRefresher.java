package AllieMainScenePane.Body.ContestTabPane.ContestTeams;

import DTO.AlliesToTable;
import Utils.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.BooleanProperty;
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

import static UBoatServletsPaths.UBoatsServletsPaths.U_BOATS_LIST_SERVLET;
import static Utils.Constants.ACTION;
import static Utils.Constants.GSON_INSTANCE;

public class ContestTeamsRefresher extends TimerTask {
    private final Consumer<AlliesToTable> updateTeamsTable;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public ContestTeamsRefresher(BooleanProperty shouldUpdate/*, Consumer<String> httpRequestLoggerConsumer*/, Consumer<AlliesToTable> updateTeamsTable) {
        this.shouldUpdate = shouldUpdate;
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.updateTeamsTable = updateTeamsTable;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        //httpRequestLoggerConsumer.accept("About to invoke: " + U_BOATS_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        //System.out.println("About to invoke: " + U_BOATS_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        String finalUrl = HttpUrl.parse(U_BOATS_LIST_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getTeamsInBattle")
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
                synchronized (this){
                    String jsonAlliesToTable = response.body().string();
                    //System.out.println("Users Request # " + finalRequestNumber + " | Response: " + jsonAlliesToTable);
                    if(jsonAlliesToTable.trim().equals("[]") || jsonAlliesToTable.trim().equals("") || response.code() != 200){
                        //System.out.println("No battles are currently in progress");
                        updateTeamsTable.accept(null);
                    }
                    else {
                        AlliesToTable alliesToTable = extractAlliesToTableFromJson(jsonAlliesToTable);
                        //System.out.println("UBoats in the battle: " + jsonAlliesToTable);
                        updateTeamsTable.accept(alliesToTable);
                    }
                    response.close();
                }
            }
        });
    }

    private AlliesToTable extractAlliesToTableFromJson(String jsonAlliesToTable) {
        JsonObject jsonObject = JsonParser.parseString(jsonAlliesToTable).getAsJsonObject();
        List<String> alliesNames = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("teams"), String[].class));
        List<Integer> numberOfAgentsForEachAllie = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("numberOfAgentsForEachAllie"), Integer[].class));
        List<Long> tasksSize = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("TasksSize"), Long[].class));
        String uBoatName = jsonObject.get("boatName").getAsString();
        return new AlliesToTable(alliesNames, numberOfAgentsForEachAllie, tasksSize, uBoatName);
    }
}
