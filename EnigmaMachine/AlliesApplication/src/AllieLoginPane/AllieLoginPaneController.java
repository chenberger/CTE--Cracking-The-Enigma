package AllieLoginPane;

import AllieMainScenePane.AllieMainScenePaneController;
import DesktopUserInterface.MainScene.ErrorDialog;

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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

import static AlliesServletsPaths.AlliesServletsPaths.ALLIES_LOGIN_SERVLET;

public class AllieLoginPaneController {
    private static final String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/AllieMainScenePane/AllieMainScenePane.fxml";
    private AllieMainScenePaneController allieMainScenePaneController;
    @FXML private GridPane AllieLoginPane;
    @FXML private TextField registerTextField;
    @FXML private javafx.scene.control.Button quitButton;

    @FXML private Button registerButton;
    public void setAllieMainScenePaneController(AllieMainScenePaneController allieMainScenePaneController) {
        this.allieMainScenePaneController = allieMainScenePaneController;
    }
    @FXML
    private void onQuitButtonClicked(ActionEvent event) {
        Platform.exit();
    }
    @FXML
    private void onRegisterButtonClicked(ActionEvent event) {
        String allyName = registerTextField.getText();
        if (allyName.isEmpty()) {
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(ALLIES_LOGIN_SERVLET)
                .newBuilder()
                .addQueryParameter("allieName", allyName)
                .build()
                .toString();


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
                        showMainAllyScene();

                    });
                }

            }
        });
    }

    private void showMainAllyScene() {
        Scene scene = AllieLoginPane.getScene();
        URL url = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            Parent root = fxmlLoader.load();
            allieMainScenePaneController = fxmlLoader.getController();
            scene.setRoot(root);
            allieMainScenePaneController.setActive();
        } catch (IOException e) {
            new ErrorDialog(e, "Unable to load main ally scene");
        }
    }

}
