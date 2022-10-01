package CodeCalibrationPane;

import CompetitionPane.UBoatCompetitionPaneController;
import DTO.MachineDetails;
import DesktopUserInterface.MainScene.BodyScene.Machine.MachineGridController;
import DesktopUserInterface.MainScene.BodyScene.Machine.ManuallyCodeInitializerScene;
import DesktopUserInterface.MainScene.Common.SkinType;
import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.EngineManager;
import EnigmaMachine.Settings.Sector;
import EnigmaMachineException.*;
import UBoatMachinePane.UBoatMachinePaneController;
import UBoatServletsPaths.UBoatsServletsPaths;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.Constants.ACTION;

public class CodeCalibrationController {

    @FXML private GridPane CodeCalibrationGrid;
    @FXML private Button setRandomCodeButton;
    @FXML private Button setManuallyCodeButton;
    private MachineGridController machineGridController;
    private ManuallyCodeInitializerScene manuallyCodeInitializerScene;
    //private EngineManager engineManager;
    private Map<SkinType, String> skinPaths;
    private SkinType currentSkinType;
    private UBoatCompetitionPaneController uBoatCompetitionPaneController;
    private UBoatMachinePaneController UboatMachinePaneController;

    @FXML public void initialize() {
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "CodeCalibrationSkin" + skinIndex++ + ".css");
        }

        currentSkinType = SkinType.CLASSIC;
    }

    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    //public void setEngineManager(EngineManager engineManager) {
    //    this.engineManager = engineManager;
    //}

    @FXML void OnSetManuallyCodeButtonClicked(ActionEvent event) throws IOException {
        manuallyCodeInitializerScene= new ManuallyCodeInitializerScene();

        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter("action", "displaySpecifications")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        new ErrorDialog(e, "Unable to get specifications")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            new ErrorDialog(new Exception("Unable to get specifications"), responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            Gson gson = new Gson();
                            MachineDetails machineDetails = gson.fromJson(response.body().string(), MachineDetails.class);
                            manuallyCodeInitializerScene.show(machineDetails, CodeCalibrationController.this, currentSkinType);
                        } catch (IOException e) {
                            new ErrorDialog(e, "Unable to get specifications");
                        }
                    });
                }
            }
        });
    }

    @FXML void OnSetRandomCodeButtonClicked(ActionEvent event) {
        //machineGridController.setAutomaticCodeConfiguration();
        //uBoatCompetitionPaneController.setAutomaticCodeConfiguration();
        setSettingsAutomatically();
    }

    public void codeConfigurationSetted(List<Sector> codeConfigurationSectors) {
        initializeEngineSettings(codeConfigurationSectors);
    }

    private void initializeEngineSettings(List<Sector> codeConfigurationSectors) {
        Gson gson = new Gson();
        String json = gson.toJson(codeConfigurationSectors);
        String finalUrl = HttpUrl
                .parse(UBoatsServletsPaths.SET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, json)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        new ErrorDialog(e, "Unable to initialize engine settings")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            new ErrorDialog(new Exception("Unable to initialize engine settings"), responseBody)
                    );
                }
                else {
                    Gson gson = new Gson();
                    MachineDetails machineDetails = gson.fromJson(response.body().string(), MachineDetails.class);
                    Platform.runLater(() -> {
                            uBoatCompetitionPaneController.setNewConfiguration(machineDetails.getCurrentMachineSettings());
                    });
                }
            }
        });

    }
    public void setAutomaticCodeConfiguration() {
        setSettingsAutomatically();
    }

    private void setSettingsAutomatically() {
        String finalUrl = HttpUrl.parse(UBoatsServletsPaths.SET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter("action", "set_machine_config_automatically")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> new ErrorDialog(e, "Error while setting machine configuration"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {

                   Platform.runLater(() -> {
                       try {
                           Gson gson = new Gson();
                           String machineDetails = gson.fromJson(response.body().string(), String.class);
                           uBoatCompetitionPaneController.setNewConfiguration(machineDetails);
                           //new ErrorDialog(new Exception(response.body().string()), "Machine configuration setted successfully");
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }
                   });
                }
                else{
                    Platform.runLater(() -> {
                        try {
                            new ErrorDialog(new Exception(response.body().string()), "Error while setting machine configuration");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }


    public void codeConfigurationSetSuccessfully() {
        manuallyCodeInitializerScene.close();
    }

    public void setSkin(SkinType skinType) {
        currentSkinType = skinType;
        CodeCalibrationGrid.getStylesheets().clear();
        CodeCalibrationGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }

    public void setUBoatCompetitionPaneController(UBoatCompetitionPaneController uBoatCompetitionPaneController) {
        this.uBoatCompetitionPaneController = uBoatCompetitionPaneController;
    }

    public void setUBoatMachinePaneController(UBoatMachinePaneController uBoatMachinePaneController) {
        this.UboatMachinePaneController = uBoatMachinePaneController;
    }
}
