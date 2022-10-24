package AllieMainScenePane.Body.ContestTabPane.CurrentContest;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import DTO.OnLineContestsTable;
import DesktopUserInterface.MainScene.ErrorDialog;
import Utils.HttpClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;

import static Utils.Constants.*;
import static Utils.Constants.REFRESH_RATE;


public class CurrentContestDataPaneController implements Closeable {
    private CurrentContestDataRefresher currentContestDataRefresher;
    private Timer timer;
    @FXML private ContestTabPaneController contestTabPaneController;
    //@FXML private AnchorPane currentContestDataPane;
    @FXML private Label battlefieldNameLabel;
    @FXML private Label uBoatNameLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label contestStatusLabel;
    @FXML private Label teamsRegisteredAndNeededLabel;

    @FXML private Label processedWordLabel;
    private SimpleBooleanProperty shouldRefresh = new SimpleBooleanProperty();
    public CurrentContestDataPaneController(){
        this.shouldRefresh.set(false);
    }
    public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
        this.contestTabPaneController = contestTabPaneController;
    }
    //setters for the labels
    public void setBattlefieldNameLabel(String battlefieldName) {
        battlefieldNameLabel.setText(battlefieldName);
    }
    public void setUBoatNameLabel(String uBoatName) {
        uBoatNameLabel.setText(uBoatName);
    }
    public void setDifficultyLabel(String difficulty) {
        difficultyLabel.setText(difficulty);
    }
    public void setContestStatusLabel(String contestStatus) {
        contestStatusLabel.setText(contestStatus);
    }
    public void setTeamsRegisteredAndNeededLabel(String teamsRegisteredAndNeeded) {
        teamsRegisteredAndNeededLabel.setText(teamsRegisteredAndNeeded);
    }
    public void setProcessedWordLabel(String processedWord) {
        processedWordLabel.setText(processedWord);
    }

    public void setChosenContest(OnLineContestsTable chosenContest, String uBoatName) {
        shouldRefresh.set(true);
        //setActive();
    }
    public void setActive(){
        startListRefresher();
    }

    public void startListRefresher() {
        currentContestDataRefresher = new CurrentContestDataRefresher(shouldRefresh, this::updateCurrentContestData);
        timer = new Timer();
        timer.schedule(currentContestDataRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @Override
    public void close(){

        if (currentContestDataRefresher != null && timer != null) {
            currentContestDataRefresher.cancel();
            timer.cancel();
        }
    }

    private void updateCurrentContestData(OnLineContestsTable onLineContestsTable) {
        Platform.runLater(() -> {
            setBattlefieldNameLabel(onLineContestsTable.getBattleName());
            setUBoatNameLabel(onLineContestsTable.getBoatName());
            setDifficultyLabel(onLineContestsTable.getDifficulty());
            setContestStatusLabel(onLineContestsTable.getContestStatus());
            setTeamsRegisteredAndNeededLabel(onLineContestsTable.getTeamsRegisteredAndNeeded());
            if(onLineContestsTable.getBoatName().equals("N/A")){
                setProcessedWordLabel("N/A");
            }else {
                handleProcessedWordFromBattle(onLineContestsTable.getBoatName());
            }
        });
    }


    private void handleProcessedWordFromBattle(String uBoatName) {
        String finalUrl = HttpUrl.parse(PROCESS_WORD_SERVLET).
                newBuilder().
                addQueryParameter(ACTION, "getProcessedWord").
                addQueryParameter("uBoatName", uBoatName).
                build().
                toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(new Exception("Failed to get processed word"), "Failed to get processed word");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != 200){
                    if(response.code() == HttpServletResponse.SC_NO_CONTENT) {
                        Platform.runLater(() -> setProcessedWordLabel("N/A"));
                    }
                    else {
                        new ErrorDialog(new Exception("Failed to get processed word"), "Failed to get processed word");
                    }
                }
                else{
                    String jsonProcessedWord = response.body().string();
                    String processedWord = GSON_INSTANCE.fromJson(jsonProcessedWord, String.class);
                    Platform.runLater(() -> setProcessedWordLabel(processedWord.trim()));
                }
            }
        });
    }
}
