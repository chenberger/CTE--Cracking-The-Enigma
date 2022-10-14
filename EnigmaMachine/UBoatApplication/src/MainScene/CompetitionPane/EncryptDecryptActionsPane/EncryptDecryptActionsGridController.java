package MainScene.CompetitionPane.EncryptDecryptActionsPane;

import DesktopUserInterface.MainScene.BodyScene.BruteForce.BruteForceGridController;
import DesktopUserInterface.MainScene.Common.AutoCompleteBox;
import DesktopUserInterface.MainScene.Common.SkinType;
import DesktopUserInterface.MainScene.ErrorDialog;
import MainScene.CompetitionPane.UBoatCompetitionPaneController;
import UBoatServletsPaths.UBoatsServletsPaths;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static Utils.Constants.ACTION;
import static Utils.Constants.GSON_INSTANCE;

public class EncryptDecryptActionsGridController {
    private BruteForceGridController bruteForceGridController;
    private UBoatCompetitionPaneController uBoatCompetitionPaneController;
    @FXML private Button addWordFromDictButton;
    @FXML private TextField encryptedDecryptedWordText;
    @FXML private TextField processedWordText;
    @FXML private Button encryptDecryptButton;
    @FXML private Button ResetMachineStateButton;
    @FXML private Label encryptDecryptTextLabel;
    @FXML private ComboBox<String> dictionarySearchComboBox;
    @FXML private GridPane encryptDecryptActionGrid;
    private AutoCompleteBox<String> autoCompleteDictionaryBox;
    private Map<SkinType, String> skinPaths;


    @FXML public void initialize() {
        autoCompleteDictionaryBox = new AutoCompleteBox<String>(dictionarySearchComboBox);
        //setDictionarySearchComboBox();
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "EncryptDecryptActionsGridSkin" + skinIndex++ + ".css");
        }
    }

    //public void setBruteForceGridController(BruteForceGridController bruteForceGridController) {
    //    this.bruteForceGridController = bruteForceGridController;
    //}
    public void setUBoatCompetitionPaneController(UBoatCompetitionPaneController uBoatCompetitionPaneController) {
        this.uBoatCompetitionPaneController = uBoatCompetitionPaneController;
    }

    @FXML private void onDecryptionButtonClicked(ActionEvent event) {
        //bruteForceGridController.decodeFromDictionary(encryptedDecryptedWordText.getText());
        String wordToProcess = encryptedDecryptedWordText.getText();
        if (wordToProcess.isEmpty()) {
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.PROCESS_WORD_SERVLET)
                .newBuilder()
                .addQueryParameter("wordToProcess", wordToProcess)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                       new ErrorDialog(new Exception("Error while trying to decrypt word: " + wordToProcess) , "Error"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() ->
                    {
                        try {
                            new ErrorDialog(new Exception(response.body().string()) , "Error while trying to decrypt word: " + wordToProcess);
                        } catch (IOException e) {
                            //throw new RuntimeException(e);
                        }
                    });
                }
                else{
                    String decryptedWord = GSON_INSTANCE.fromJson(response.body().string(), String.class);
                    Platform.runLater(() -> {
                        processedWordText.setText(decryptedWord);
                        uBoatCompetitionPaneController.getCurrentMachineConfiguration();
                        uBoatCompetitionPaneController.setProcessedWordExist(true);
                        uBoatCompetitionPaneController.machineDetailsChanged();

                        //uBoatCompetitionPaneController.setIsWordProcessed();
                        //uBoatCompetitionPaneController.setIsReadyOrNot();
                        //uBoatCompetitionPaneController.addDecryptedWord(decryptedWord);
                    });
                }
            }
        });
    }
    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) {
            //bruteForceGridController.resetMachineState();
            encryptedDecryptedWordText.setText("");
            processedWordText.setText("");
            resetMachineState();
            uBoatCompetitionPaneController.getCurrentMachineConfiguration();
            uBoatCompetitionPaneController.setProcessedWordExist(false);
            uBoatCompetitionPaneController.machineDetailsChanged();

            //uBoatCompetitionPaneController.setNoWordProcessed();
    }

    public void setProcessedString(String processedString) {
        processedWordText.setText(processedString);
    }

    @FXML private void onAddWordFromDictButtonClicked(ActionEvent event) {
        //String wordToAdd = encryptedDecryptedWordText.getLength() > 0 ? " " + dictionarySearchComboBox.getEditor().getText() : dictionarySearchComboBox.getEditor().getText();
        encryptedDecryptedWordText.setText(encryptedDecryptedWordText.getText() + dictionarySearchComboBox.getEditor().getText());
        dictionarySearchComboBox.getEditor().setText("");
    }
    public void setDictionarySearchComboBox() {
        setAutoCompleteBoxDictionary();
        dictionarySearchComboBox.setDisable(false);
        addWordFromDictButton.setDisable(false);
    }

    private void setAutoCompleteBoxDictionary() {

        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.DICTIONARY_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getDictionary")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        new ErrorDialog(new Exception("Error, Error while trying to get dictionary") , "Error"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    new ErrorDialog(new Exception("Error, Error while trying to get dictionary") , "Error");
                }
                else{
                    String dict = response.body().string();
                    Platform.runLater(() -> {
                        Gson gson = new Gson();
                        System.out.println("dictionary got " );
                        autoCompleteDictionaryBox.setData(gson.fromJson(dict, Set.class));
                    });
                }
            }
        });
    }
    private void resetMachineState(){

        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.SET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "reset_machine_config")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            new ErrorDialog(new Exception("Error, Error while trying to reset machine state" + e) , "Error"));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        new ErrorDialog(new Exception("Error, Error while trying to reset machine state") , "Error");
                    }
                    else{

                        Platform.runLater(() -> {
                            Gson gson = new Gson();
                            System.out.println("success in reset machine state " );
                            try {
                                String originalConfiguration = response.body().string();
                                uBoatCompetitionPaneController.setNewConfiguration(gson.fromJson(originalConfiguration, String.class));
                            } catch (IOException e) {
                                new ErrorDialog(new Exception("Error, Error while trying to reset machine state, " + e) , "Error");
                            }
                        });
                    }
                }
            });
        }
    //public void setDictionary(Object source, Dictionary dictionary) {
    //    this.autoCompleteDictionaryBox.setData(dictionary.getDictionary());
    //    dictionarySearchComboBox.setDisable(false);
    //    addWordFromDictButton.setDisable(false);
    //}

    public void setSkin(SkinType skinType) {
        encryptDecryptActionGrid.getStylesheets().clear();
        encryptDecryptActionGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }

    public void clearTexts() {
        encryptedDecryptedWordText.clear();
        processedWordText.clear();
        dictionarySearchComboBox.getEditor().setText("");
    }

    public String getProccesesedString() {
        return processedWordText.getText().equals("") ? null : processedWordText.getText();
    }

}
