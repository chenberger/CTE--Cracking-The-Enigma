package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.Common.SkinType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class MachineStatisticsGridPaneController {

    @FXML private ScrollPane statisticsGrid;
    @FXML private TextArea statisticTextArea;

    @FXML private Label machineStatisticsHeader;

    @FXML private GridPane machineStatisticsGridPane;
    private Map<SkinType, String> skinPaths;
    private EncryptDecryptGridController encryptDecryptGridController;

    @FXML public void initialize() {
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "MachineStatisticsSkin" + skinIndex++ + ".css");
        }
    }

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }

    public void staticsAndHistoryChanged(Object o, String currentStatistics) {
        statisticTextArea.setText(currentStatistics);
    }

    public void setCodeConfiguration(String currentMachineSettings) {
        statisticTextArea.setText(currentMachineSettings);
    }

    public void setSkin(SkinType skinType) {
        machineStatisticsGridPane.getStylesheets().clear();
        machineStatisticsGridPane.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }
}
