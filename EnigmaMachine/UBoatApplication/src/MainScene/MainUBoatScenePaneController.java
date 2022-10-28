package MainScene;


import DTO.ContestWinnerInformation;
import DesktopUserInterface.MainScene.ErrorDialog;
import LoginPane.UBoatLoginPaneController;
import MainScene.ChatTabPane.chatroom.ChatRoomMainController;
import MainScene.UBoatMachinePane.CurrentCodeConfigurationPane.CurrentCodeConfigurationController;
import MainScene.UBoatMachinePane.UBoatMachinePaneController;
import TopBorderPane.TopBorderPaneController;
import UBoatServletsPaths.UBoatsServletsPaths;
import Utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static Constants.ServletConstants.USERNAME;
import static UBoatServletsPaths.UBoatsServletsPaths.U_BOAT_LOGOUT_SERVLET;
import static Utils.Constants.ACTION;
import static Utils.Constants.TASKS_SERVLET;

public class MainUBoatScenePaneController {
    private static final String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/LoginPane/UBoatLoginPane.fxml";
    private boolean isWinnerFound;
    private String currentSessionId;
    @FXML private ScrollPane UBoatMainScenePane;

    private String currentBoatName;
    private UBoatMachinePaneController uBoatMachinePaneController;
    @FXML private GridPane UBoatLoginPane;
    @FXML private UBoatLoginPaneController UBoatLoginPaneController;
    @FXML private AnchorPane topBorderPane;
    @FXML private TopBorderPaneController topBorderPaneController;
    @FXML private AnchorPane UBoatCompetitionPane;
    @FXML private MainScene.CompetitionPane.UBoatCompetitionPaneController UBoatCompetitionPaneController;

    @FXML private GridPane machineGrid;
    @FXML private UBoatMachinePaneController machineGridController;
    private SimpleBooleanProperty isMachineExsists;
    private SimpleBooleanProperty isCodeConfigurationSet;
    @FXML private List<CurrentCodeConfigurationController> currentCodeConfigurationGridControllers;
    @FXML private Button logoutButton;
    @FXML private BorderPane chatRoomPane;
    private ChatRoomMainController chatRoomPaneController;
    private OkHttpClient httpClient;

    //TODO: make sure that log out is possible only between competitions//

    public MainUBoatScenePaneController() {
        currentSessionId = null;
        currentBoatName = null;
        this.currentCodeConfigurationGridControllers = new ArrayList<>();
        this.isMachineExsists = new SimpleBooleanProperty(false);
        this.isCodeConfigurationSet = new SimpleBooleanProperty(false);
        this.httpClient = new OkHttpClient().newBuilder().build();
        this.isWinnerFound = false;
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

        if(machineGridController != null) {
            machineGridController.setMainUBoatScenePaneController(this);
        }
        if(uBoatMachinePaneController != null) {
            uBoatMachinePaneController.setMainUBoatScenePaneController(this);
        }
        topBorderPaneController.setUBoatName();
        UBoatCompetitionPane.disableProperty().bind(isMachineExsists.not().or(isCodeConfigurationSet.not()));
    }
    public void machineDetailsChanged(){
        machineGridController.machineDetailsChanged();
    }

    @FXML public void onLogoutButtonClicked(ActionEvent actionEvent) {
        String finalUrl = HttpUrl.parse(U_BOAT_LOGOUT_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "uBoatLogout")
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(new Exception("Failed to log out from session"), "Failed to logout");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        try {
                            new ErrorDialog(new Exception("You have been logged out"), "Logged out");
                            loadLoginPage();
                            closeUBoatSession();
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                new ErrorDialog(new Exception("Failed to log out from session"), "Failed to logout");
                            });
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        new ErrorDialog(new Exception(responseStr), "Failed to logout");
                    });
                }
                response.close();
            }
        });
    }

    private void loadLoginPage() throws IOException {
        Scene scene = UBoatMainScenePane.getScene();
        URL url = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            Parent root = fxmlLoader.load();
            UBoatLoginPaneController = fxmlLoader.getController();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        getCurrentSessionId();
        getCurrentBoatName();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", selectedFile.getAbsolutePath(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(selectedFile.getAbsolutePath())))
                .build();
        String finalUrl = HttpUrl.parse(UBoatsServletsPaths.FILE_UPLOADED_SERVLET).newBuilder()
                .addQueryParameter(USERNAME, currentBoatName)
                .build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .method("POST", body)
                .addHeader("Cookie", "JSESSIONID=" + currentSessionId)
                .build();


        Response response = httpClient.newCall(request).execute();
        if (response.code() == 200) {
            Platform.runLater(() -> {
                try {
                    isMachineExsists.set(true);

                    //new ErrorDialog(new Exception(response.body().string()),"Machine loaded successfully");
                    topBorderPaneController.setFileUploadedName(selectedFile.getAbsolutePath());
                    UBoatCompetitionPaneController.setDictionary();
                    machineGridController.newFileLoaded();
                    topBorderPaneController.disableLoadMachineButton();
                } catch (Exception e) {
                    new ErrorDialog(e, "Error while loading machine");
                }
            });

        } else {
            Platform.runLater(() -> {
                try {
                    new ErrorDialog(new Exception(response.body().string()), "Error while loading machine");
                } catch (IOException e) {
                }
            });

        }
        response.close();

    }

    private void getCurrentBoatName() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        currentBoatName = topBorderPaneController.getAppHeaderLabelText();
        String finalUrl = HttpUrl.parse(UBoatsServletsPaths.GET_USER_NAME_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getBoatName")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> new ErrorDialog(e, "Error while getting boat Name"));
            }
//
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    currentBoatName = response.body().string().trim();
                    countDownLatch.countDown();
                }
                else{
                    Platform.runLater(() -> {
                        try {
                            new ErrorDialog(new Exception(response.body().string()), "Error while getting session id");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                response.close();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized private void getCurrentSessionId() throws IOException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String finalUrl = HttpUrl.parse(UBoatsServletsPaths.U_BOAT_LOGIN_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "get_session_id")
                .build()
                .toString();

       // Request request = new Request.Builder()
       //         .url(finalUrl)
       //         .build();
       // Response response = new OkHttpClient().newCall(request).execute();
       //if(response.code() == 200) {
       //      currentSessionId = response.body().string().trim();
       //}
       //else{
       //     new ErrorDialog(new Exception(response.body().string()), "Error while getting session id");
       //}
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> new ErrorDialog(e, "Error while getting session id"));
            }
//
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    currentSessionId = response.body().string().trim();
                    countDownLatch.countDown();
                }
                else{
                    Platform.runLater(() -> {
                        try {
                            new ErrorDialog(new Exception(response.body().string()), "Error while getting session id");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                response.close();
            }
        });
        try{
            countDownLatch.await();
        } catch (InterruptedException e) {

        }

    }


    public void setActive() {
        UBoatCompetitionPaneController.setActive();
        httpClient = new OkHttpClient();
    }

    public void setCompetitionPaneDisabled() {
        UBoatCompetitionPane.setDisable(true);
    }


    public void setCodeConfiguration(String currentMachineConfiguration) {
        machineGridController.setCodeConfiguration(currentMachineConfiguration);
    }

    public void newCodeConfigurationSetted(String currentCodeConfiguration) {
        UBoatCompetitionPaneController.setNewConfiguration(currentCodeConfiguration);
        isCodeConfigurationSet.set(true);
    }

    public void close() {
        //HttpClientUtil.shutdown();
        //Platform.exit();
    }
    private void closeUBoatSession(){
        UBoatCompetitionPaneController.close();
        try {
            chatRoomPaneController.close();
        } catch (IOException ignored) {

        }
        HttpClientUtil.removeCookiesOf("localhost");
    }

    public void notifyIfWordIsFound(ContestWinnerInformation contestWinner) {
        if(!isWinnerFound) {
            isWinnerFound = true;
            stopContest(contestWinner.getWinnerName());
            if(!contestWinner.getWinnerName().equals("")) {
                Platform.runLater(() -> {
                    new ErrorDialog(new Exception(contestWinner.getWinnerName() + " found the word: " + contestWinner.getOriginalWord()), "Winner found");
                });
            }
            else{
                Platform.runLater(() -> {
                    new ErrorDialog(new Exception("All of the competitors left the contest!!"), "Contest is Over");
                });
            }
        }
    }

    private void stopContest(String contestWinner) {
        UBoatCompetitionPaneController.stopContest();
        String finalUrl = HttpUrl.parse(TASKS_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "stopContest")
                .addQueryParameter("winner", contestWinner)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> new ErrorDialog(e, "Error while stopping contest"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != 200) {
                    Platform.runLater(() -> {
                        try {
                            new ErrorDialog(new Exception(response.body().string()), "Error while stopping contest");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                response.close();
            }
        });

    }

    public void incrementMessagesCounter() {
        machineGridController.incrementMessagesCounter();
    }
    public void setNoneWinnerFound() {
        isWinnerFound = false;
    }

    public void setPaneSize() {
        UBoatMainScenePane.prefWidth(1200);
        UBoatMainScenePane.prefHeight(920);
    }
}
