package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class MachineStatisticsGridPaneController {

    @FXML
    private ScrollPane statisticsGrid;
    @FXML
    private TextArea statisticTextArea;

    @FXML
    private Label machineStatisticsHeader;

    @FXML
    private GridPane machineStatisticsGridPane;

    private EncryptDecryptGridController encryptDecryptGridController;

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }

    public void showCurrentStatistics(String currentStatistics) {
        statisticTextArea.setText(currentStatistics);
    }
}
