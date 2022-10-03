package Body.DashBoardTabPane.TeamAgentsData;

import DTO.AgentsToTable;
import DTO.AlliesToTable;
import Utils.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_LIST_SERVLET;
import static Utils.Constants.GSON_INSTANCE;

public class TeamAgentsListRefresher extends TimerTask {
    private final Consumer<AgentsToTable> agentsToTableConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public TeamAgentsListRefresher(BooleanProperty shouldUpdate, Consumer<AgentsToTable> agentsToTableConsumer) {
        this.shouldUpdate = shouldUpdate;
    //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.agentsToTableConsumer = agentsToTableConsumer;
        requestNumber = 0;

}

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        //httpRequestLoggerConsumer.accept("About to invoke: " + U_BOATS_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        System.out.println("About to invoke: " + ALLIES_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(ALLIES_LIST_SERVLET, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }
            //TODO chen: make sure that this is the syntax to transform u boat list from the server to the client
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                synchronized (this){
                    String jsonAgentsToTable = response.body().string();
                    System.out.println("Users Request # " + finalRequestNumber + " | Response: " + jsonAgentsToTable);
                    if(jsonAgentsToTable.trim().equals("[]") || jsonAgentsToTable.trim().equals("") || response.code() != 200){
                        System.out.println("No Allies in the battle");
                    }
                    else {
                        AgentsToTable agentsToTable = extractAgentsToTableFromJson(jsonAgentsToTable);
                        System.out.println("UBoats in the battle: " + jsonAgentsToTable);
                        agentsToTableConsumer.accept(agentsToTable);
                    }
                }
            }
        });
    }

    private AgentsToTable extractAgentsToTableFromJson(String jsonAgentsToTable) {
        JsonObject jsonObject = JsonParser.parseString(jsonAgentsToTable).getAsJsonObject();
        List<String> agentsNames = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("agents"), String[].class));
        List<Long> numberOfThreadsForEachAgent = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("numberOfThreadsForEachAgent"), Long[].class));
        List<Long> tasksSizesForEachAgent = Arrays.asList(GSON_INSTANCE.fromJson(jsonObject.get("numberOfTasksForEachAgent"), Long[].class));
        return new AgentsToTable(agentsNames, numberOfThreadsForEachAgent, tasksSizesForEachAgent);
    }
}
