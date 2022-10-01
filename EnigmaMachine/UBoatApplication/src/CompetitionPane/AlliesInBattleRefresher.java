package CompetitionPane;

import DTO.AlliesToTable;
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

import static UBoatServletsPaths.UBoatsServletsPaths.ALLIES_LIST_SERVLET;
import static UBoatServletsPaths.UBoatsServletsPaths.U_BOATS_LIST_SERVLET;
import static Utils.Constants.GSON_INSTANCE;

public class AlliesInBattleRefresher extends TimerTask {

    //private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<UBoat>> uBoatListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public AlliesInBattleRefresher(BooleanProperty shouldUpdate/*, Consumer<String> httpRequestLoggerConsumer*/, Consumer<List<UBoat>> uBoatsListConsumer) {
        this.shouldUpdate = shouldUpdate;
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.uBoatListConsumer = uBoatsListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        //if (!shouldUpdate.get()) {
        //    return;
        //}

        final int finalRequestNumber = ++requestNumber;
        //httpRequestLoggerConsumer.accept("About to invoke: " + U_BOATS_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        System.out.println("About to invoke: " + U_BOATS_LIST_SERVLET + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(U_BOATS_LIST_SERVLET, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }
            //TODO chen: make sure that this is the syntax to transform u boat list from the server to the client
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUBoats = response.body().string();
                System.out.println("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUBoats);
                if(jsonArrayOfUBoats.trim().equals("[]")){
                    System.out.println("No UBoats in the battle");
                }
                else {
                    UBoat[] uBoats = GSON_INSTANCE.fromJson(jsonArrayOfUBoats, UBoat[].class);
                    System.out.println("UBoats in the battle: " + jsonArrayOfUBoats);
                    uBoatListConsumer.accept(Arrays.asList(uBoats));
                }
            }
        });
    }
}
