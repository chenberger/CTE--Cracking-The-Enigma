package MainScene;

import CurrentCodeConfigurationPane.CurrentCodeConfigurationController;
import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.EngineManager;
import LoginPane.UBoatLoginPaneController;
import TopBorderPane.TopBorderPaneController;
import UBoatServletsPaths.UBoatsServletsPaths;
import Utils.Constants;
import Utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainUBoatScenePaneController {

    @FXML private GridPane UBoatLoginPane;
    @FXML private UBoatLoginPaneController UBoatLoginPaneController;
    @FXML private AnchorPane topBorderPane;
    @FXML private TopBorderPaneController topBorderPaneController;
    @FXML private AnchorPane UBoatCompetitionPane;
    @FXML private CompetitionPane.UBoatCompetitionPaneController UBoatCompetitionPaneController;
    private SimpleBooleanProperty isMachineExsists;
    private SimpleBooleanProperty isCodeConfigurationSet;
    @FXML private List<CurrentCodeConfigurationController> currentCodeConfigurationGridControllers;

    public static void setEnigmaEngine(EngineManager engineManager) {

    }
    public MainUBoatScenePaneController() {
        this.currentCodeConfigurationGridControllers = new ArrayList<>();
        this.isMachineExsists = new SimpleBooleanProperty(false);
        this.isCodeConfigurationSet = new SimpleBooleanProperty(false);
    }
    public void initialize() {
        if (UBoatLoginPane != null) {
            UBoatLoginPaneController.setMainUBoatScenePaneController(this);
        }
        if (UBoatCompetitionPane != null) {
            UBoatCompetitionPaneController.setMainUBoatScenePaneController(this);
        }
        if(topBorderPane != null) {
            topBorderPaneController.setMainUBoatScenePaneController(this);
        }
        UBoatCompetitionPane.disableProperty().bind(isMachineExsists.not()/*.or(isCodeConfigurationSet.not()*/);
    }


    public void switchToCompetitionRoom() {
        UBoatLoginPane.setVisible(false);
        UBoatCompetitionPane.setVisible(true);
    }
    public void addCurrentCodeConfigurationController(CurrentCodeConfigurationController currentCodeConfigurationController) {
        currentCodeConfigurationGridControllers.add(currentCodeConfigurationController);
    }
    private Window getStageWindow() {
        return UBoatCompetitionPane.getScene().getWindow();
    }
    public void loadMachine() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(getStageWindow());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", selectedFile.getAbsolutePath(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(selectedFile.getAbsolutePath())))
                .build();

        Request request = new Request.Builder()
                .url(UBoatsServletsPaths.FILE_UPLOADED_SERVLET)
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        try {

            if(response.code() == 200) {
                isMachineExsists.set(true);
                topBorderPaneController.setMachineExists(true);
                topBorderPaneController.setFileUploadedName(selectedFile.getAbsolutePath());
            }
            else{
                Platform.runLater(() -> {
                    try {
                        new ErrorDialog(new Exception(response.body().string()), "Error while uploading file");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            Platform.runLater(() -> new ErrorDialog(e, "Error while uploading file"));
        }

    }
        //updateHttpStatusLine("New request is launched for: " + finalUrl);

        //HttpClientUtil.runAsync(finalUrl, new Callback() {
//
        //    @Override
        //    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        //        Platform.runLater(() ->
        //                new ErrorDialog(e,"Unable to Upload File"));
        //    }
//
        //    @Override
        //    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        //        if (response.code() != 200) {
        //            String responseBody = response.body().string();
        //            Platform.runLater(() ->
        //                    new ErrorDialog(new Exception("Something went wrong. "), responseBody)
        //            );
        //        } else {
        //            Platform.runLater(() -> {
        //                isMachineExsists.set(true);
        //                topBorderPaneController.setMachineExists(true);
        //                topBorderPaneController.setFileUploadedName(selectedFile.getName());
        //            });
//
        //        }
        //    }
        //});



    public void setActive() {
        UBoatCompetitionPaneController.setActive();
    }

    public void setCompetitionPaneDisabled() {
        UBoatCompetitionPane.setDisable(true);
    }

}
