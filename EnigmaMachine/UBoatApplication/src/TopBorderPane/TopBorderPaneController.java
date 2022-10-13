package TopBorderPane;

import DesktopUserInterface.MainScene.ErrorDialog;
import MainScene.MainUBoatScenePaneController;
import Utils.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static UBoatServletsPaths.UBoatsServletsPaths.GET_USER_NAME_SERVLET;
import static Utils.Constants.ACTION;


public class TopBorderPaneController {
    private MainUBoatScenePaneController mainUBoatScenePaneController;
    @FXML private GridPane topBorderPane;
    @FXML private Label appHeaderLabel;
    @FXML
     private Button loadMachineButton;
    @FXML private Label CurrentFilePathTextLabel;

    public void setMainUBoatScenePaneController(MainUBoatScenePaneController mainUBoatScenePaneController) {
        this.mainUBoatScenePaneController = mainUBoatScenePaneController;
    }
    @FXML private void onLoadMachineButtonClicked(ActionEvent event) throws IOException {
        mainUBoatScenePaneController.loadMachine();
    }

    public void setMachineExists(boolean isMachineExists) {
        loadMachineButton.setDisable(isMachineExists);
    }

    public void setFileUploadedName(String name) {
        CurrentFilePathTextLabel.setText(name);
    }

    public void disableLoadMachineButton() {
        loadMachineButton.setDisable(true);
    }

    public void setUBoatName() {
        String finalUrl = HttpUrl.parse(GET_USER_NAME_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getBoatName")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(new Exception("Failed to get user name"), "Failed to get user name");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String userName = response.body().string();
                appHeaderLabel.setText("UBoat - " + userName);
            }
        });
    }
    public String getAppHeaderLabelText() {
        return appHeaderLabel.getText();
    }
}
