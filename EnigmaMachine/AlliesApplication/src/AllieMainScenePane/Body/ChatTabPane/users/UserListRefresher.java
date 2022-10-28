package AllieMainScenePane.Body.ChatTabPane.users;

import Utils.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Utils.Constants.CHAT_USERS_LIST;
import static Utils.Constants.GSON_INSTANCE;

public class UserListRefresher extends TimerTask {

    private final Consumer<List<String>> usersListConsumer;
    private int requestNumber;

    public UserListRefresher(Consumer<List<String>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {
        final int finalRequestNumber = ++requestNumber;
        HttpClientUtil.runAsync(CHAT_USERS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    String jsonArrayOfUsersNames = response.body().string();
                    String[] usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                    List<String> temp = Arrays.asList(usersNames);
                    usersListConsumer.accept(Arrays.asList(usersNames));
                }
            }
        });
    }
}
