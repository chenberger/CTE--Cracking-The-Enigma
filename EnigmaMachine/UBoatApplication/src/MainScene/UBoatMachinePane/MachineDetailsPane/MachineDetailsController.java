package MainScene.UBoatMachinePane.MachineDetailsPane;

import DTO.MachineConfigurationToShow;
import DesktopUserInterface.MainScene.BodyScene.Machine.MachineGridController;
import DesktopUserInterface.MainScene.Common.SkinType;
import DesktopUserInterface.MainScene.ErrorDialog;
import MainScene.UBoatMachinePane.UBoatMachinePaneController;
import Utils.HttpClientUtil;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static UBoatServletsPaths.UBoatsServletsPaths.GET_MACHINE_CONFIG_SERVLET;
import static Utils.Constants.*;

public class MachineDetailsController {

    @FXML private GridPane machineDetailsGrid;
    @FXML private Label totalAmountRotorsLabel;
    @FXML private Label currentAmountRotorsLabel;
    @FXML private Label totalAmountReflectorsLabel;
    @FXML private Label messagesAmountLabel;
    @FXML private TextArea originalCodeLabel;
    @FXML private TextArea currentCodeLabel;
    private Map<SkinType, String> skinPaths;
    private MachineGridController machineGridController;
    private MainScene.UBoatMachinePane.UBoatMachinePaneController UBoatMachinePaneController;

    @FXML public void initialize() {
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "machinedetailsSkin" + skinIndex++ + ".css");
        }
    }

    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    public void setNewMachineDetails(MachineConfigurationToShow machineDetailsToShow) {
        Platform.runLater(() -> {
            totalAmountRotorsLabel.setText("The total amount of rotors that can be use in the machine is: " + machineDetailsToShow.getNumOfPossibleRotors());
            currentAmountRotorsLabel.setText("The current amount of rotors in use in the machine is: " + machineDetailsToShow.getNumOfRotorsInUse());
            totalAmountReflectorsLabel.setText("The total amount of reflectors that can be use in the machine is: " + machineDetailsToShow.getNumOfPossibleRotors());
            messagesAmountLabel.setText("The total amount of messages have been processed by the machine so far is: " + machineDetailsToShow.getNumOfProcessedMessages());
            currentCodeLabel.setText(machineDetailsToShow.getCurrentCodeConfiguration());
            originalCodeLabel.setText(machineDetailsToShow.getOriginalCodeConfiguration());
        });

        //originalCodeLabel.setText(String.valueOf(machineDetails.getOriginalMachineSettings()));
        //currentCodeLabel.setText(String.valueOf(machineDetails.getCurrentMachineSettings()));
    }

    private void setOriginalAndCurrentCodeLabels() {
        setOriginalCodeLabel();
        setCurrentCodeLabel();
    }

    private void setCurrentCodeLabel() {
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getCurrentMachineConfig")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(new Exception("Failed to get current machine config"), "Failed to get current machine config");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {

                    try {
                        String currentMachineConfig = GSON_INSTANCE.fromJson(response.body().string(), String.class);
                        currentCodeLabel.setText(currentMachineConfig);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
            }
        });
    }

    private void setOriginalCodeLabel() {
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET)
                .newBuilder()
                .addQueryParameter(ACTION, "getOriginalMachineConfig")
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(new Exception("Failed to get original machine config"), "Failed to get original machine config");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        String originalMachineConfig = GSON_INSTANCE.fromJson(response.body().string(), String.class);
                        originalCodeLabel.setText(originalMachineConfig);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });

            }
        });
    }

    public void currentCodeChanged(Object source, String currentCode) {
        currentCodeLabel.setText(currentCode);
    }

    public void setSkin(SkinType skinType) {
        machineDetailsGrid.getStylesheets().clear();
        machineDetailsGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }

    public void setUBoatMachinePaneController(UBoatMachinePaneController uBoatMachinePaneController) {
        this.UBoatMachinePaneController = uBoatMachinePaneController;
    }

    public void machineDetailsChanged() {
        String finalUrl = HttpUrl.parse(GET_MACHINE_CONFIG_SERVLET).newBuilder()
                .addQueryParameter(ACTION, DISPLAY_SPECIFICATIONS)
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new ErrorDialog(e, "Unable to get specifications");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    new ErrorDialog(new Exception("Unable to get specifications"), responseBody);

                } else {
                    Platform.runLater(() -> {
                        try {
                            Gson gson = new Gson();
                            MachineConfigurationToShow machineConfigurationToShow = gson.fromJson(response.body().string(), MachineConfigurationToShow.class);
                            setNewMachineDetails(machineConfigurationToShow);
                        } catch (IOException e) {
                            new ErrorDialog(e, "Unable to get specifications");
                        }
                    });
                }
            }
        });
    }
}
