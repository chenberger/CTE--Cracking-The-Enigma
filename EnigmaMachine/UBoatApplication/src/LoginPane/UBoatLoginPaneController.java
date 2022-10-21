package LoginPane;

import DesktopUserInterface.MainScene.ErrorDialog;
import MainScene.MainUBoatScenePaneController;
import UBoatServletsPaths.UBoatsServletsPaths;
import Utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.net.URL;

public class UBoatLoginPaneController {
    private static final String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/MainScene/MainUBoatScenePane.fxml";
    private MainUBoatScenePaneController mainUBoatScenePaneController;

    @FXML
    private GridPane UBoatLoginPane;

    @FXML private TextField registerTextField;
    @FXML private Button quitButton;

    @FXML private Button registerButton;
    public void setMainUBoatScenePaneController(MainUBoatScenePaneController mainUBoatScenePaneController) {
        this.mainUBoatScenePaneController = mainUBoatScenePaneController;
    }
   @FXML
   private void onQuitButtonClicked(ActionEvent event) {
       Platform.exit();
   }
    @FXML
    private void onRegisterButtonClicked(ActionEvent event) {
        String userName = registerTextField.getText();
        if (userName.isEmpty()) {
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.U_BOAT_LOGIN_SERVLET)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(finalUrl, new Callback() {

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Platform.runLater(() ->
                    new ErrorDialog(e,"Unable to Register")
            );
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if (response.code() != 200) {
                String responseBody = response.body().string();
                Platform.runLater(() ->
                       new ErrorDialog(new Exception(responseBody), "Unable to register, please try again with different name. ")
                );
            } else {
                Platform.runLater(() -> {
                    showMainUBoatScene();

                });
            }
            response.close();
        }
    });
    }

    private void showMainUBoatScene() {
        Scene scene = UBoatLoginPane.getScene();
        URL url = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            Parent root = fxmlLoader.load();
            mainUBoatScenePaneController = fxmlLoader.getController();
            scene.setRoot(root);
            mainUBoatScenePaneController.setActive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
