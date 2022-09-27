package LoginPane;

import DesktopUserInterface.MainScene.ErrorDialog;
import MainScene.MainUBoatScenePaneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UBoatLoginPaneController {
    private MainUBoatScenePaneController mainUBoatScenePaneController;
    @FXML
    private GridPane UBoatLoginPane;

    @FXML
    private HBox registerHBox;
    @FXML
    private TextField registerTextField;

    @FXML
    private Button registerButton;
    public void setMainUBoatScenePaneController(MainUBoatScenePaneController mainUBoatScenePaneController) {
        this.mainUBoatScenePaneController = mainUBoatScenePaneController;
    }
    @FXML
    private void onRegisterButtonClicked(ActionEvent event) {
        String BASE_URL = "http://localhost:8080/users/Login";
        String userName = registerTextField.getText();
        if (userName.isEmpty()) {
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(BASE_URL)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        //HttpClientUtil.runAsync(finalUrl, new Callback() {
//
        //    @Override
        //    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        //        Platform.runLater(() ->
        //                errorMessageProperty.set("Something went wrong: " + e.getMessage())
        //        );
        //    }
//
        //    @Override
        //    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        //        if (response.code() != 200) {
        //            String responseBody = response.body().string();
        //            Platform.runLater(() ->
        //                    errorMessageProperty.set("Something went wrong: " + responseBody)
        //            );
        //        } else {
        //            Platform.runLater(() -> {
        //                chatAppMainController.updateUserName(userName);
        //                chatAppMainController.switchToChatRoom();
        //            });
        //        }
        //    }
        //});
    }
}
