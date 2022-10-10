package AgentLoginPane;

import DTO.TeamNameColumn;
import Utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_LIST_SERVLET;
import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_LOGIN_SERVLET;
import static Utils.Constants.ACTION;
import static Utils.Constants.GSON_INSTANCE;

public class OptionalTeamsRefresher extends TimerTask {
    private final Consumer<List<TeamNameColumn>> updateTableConsumer;
    public OptionalTeamsRefresher(Consumer<List<TeamNameColumn>> updateTableConsumer) {
        this.updateTableConsumer = updateTableConsumer;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl.parse(ALLIES_LIST_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getAlliesList")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String jsonListOfTeams = response.body().string();
                    List<String> listOfTeams = Arrays.asList(GSON_INSTANCE.fromJson(jsonListOfTeams, String[].class));
                    List<TeamNameColumn> listOfTeamsToTable = convertListOfTeamsToTable(listOfTeams);

                    updateTableConsumer.accept(listOfTeamsToTable);
                } else {
                    updateTableConsumer.accept(null);
                }
            }

        });
    }

    private List<TeamNameColumn> convertListOfTeamsToTable(List<String> listOfTeams) {
        List<TeamNameColumn> listOfTeamsToTable = new ArrayList<>();
        for (String team : listOfTeams) {
            listOfTeamsToTable.add(new TeamNameColumn(team));
        }
        return listOfTeamsToTable;
    }

}
