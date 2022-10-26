package AllieMainScenePane.Body.DashBoardTabPane.ContestData;

import BruteForce.DifficultyLevel;
import DTO.AgentsToTable;
import DTO.OnLineContestsDataToTable;
import Utils.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_LIST_SERVLET;
import static UBoatServletsPaths.UBoatsServletsPaths.U_BOATS_LIST_SERVLET;
import static Utils.Constants.ACTION;
import static Utils.Constants.GSON_INSTANCE;

public class ContestsDataRefresher extends TimerTask {
    private final Consumer<OnLineContestsDataToTable> ContestsConsumer;

    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public ContestsDataRefresher(BooleanProperty shouldUpdate, Consumer<OnLineContestsDataToTable> ContestsConsumer) {
        this.shouldUpdate = shouldUpdate;
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.ContestsConsumer = ContestsConsumer;
        requestNumber = 0;

    }

    @Override
    public void run() {

        //if (!shouldUpdate.get()) {
        //    return;
        //}

        final int finalRequestNumber = ++requestNumber;
        //httpRequestLoggerConsumer.accept("About to invoke: " + U_BOATS_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        //System.out.println("About to invoke: " + ALLIES_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        String finalUrl = HttpUrl.parse(U_BOATS_LIST_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getCurrentOnlineContests")
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
                    synchronized (this){
                    String jsonOnlineContestsDataToTable = response.body().string();
                    //System.out.println("Users Request # " + finalRequestNumber + " | Response: " + jsonOnlineContestsDataToTable);
                    if(jsonOnlineContestsDataToTable.trim().equals("[]") || jsonOnlineContestsDataToTable.trim().equals("") || response.code() != 200){
                        //System.out.println("No Allies in the battle");
                    }
                    else {
                        OnLineContestsDataToTable onLineContestsDataToTable = extractOnlineContestToTableFromJson(jsonOnlineContestsDataToTable);
                        //System.out.println("Allies in the battle: " + jsonOnlineContestsDataToTable);
                        ContestsConsumer.accept(onLineContestsDataToTable);
                    }
                }
                }
                else {
                    ContestsConsumer.accept(null);
                }
                response.close();
            }
        });
    }

    private OnLineContestsDataToTable extractOnlineContestToTableFromJson(String jsonOnlineContestsDataToTable) {
        JsonObject jsonObject = JsonParser.parseString(jsonOnlineContestsDataToTable).getAsJsonObject();
        List<String> battlesNames = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("battleNames"), String[].class));
        List<String> uBoatsNames = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("uBoatNames"), String[].class));
        List<DifficultyLevel> difficultyLevels = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("difficultyLevels"), DifficultyLevel[].class));
        Map<String, String>  contestsStatus = GSON_INSTANCE.fromJson(jsonObject.get("contestsStatus"), Map.class);
        Type numberOfTeamsRegisteredToEachContestType = new TypeToken<Map<String, Integer>>(){}.getType();
        Map<String, Integer> numberOfTeamsRegisteredToEachContest = GSON_INSTANCE.fromJson(jsonObject.get("numberOfTeamsRegisteredToEachContest"), numberOfTeamsRegisteredToEachContestType);

        List<Integer> numberOfTeamsNeededToEachContest = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("numberOfTeamsNeededToEachContest"), Integer[].class));
        return new OnLineContestsDataToTable(battlesNames, uBoatsNames, difficultyLevels, contestsStatus, numberOfTeamsRegisteredToEachContest, numberOfTeamsNeededToEachContest);

    }
}
