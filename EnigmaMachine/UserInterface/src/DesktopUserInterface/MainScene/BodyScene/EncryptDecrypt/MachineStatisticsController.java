package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class MachineStatisticsController {

    @FXML
    private ScrollPane statisticsGrid;

    @FXML
    private GridPane machineStatisticsGridPane;

    @FXML
    private TextArea statisticTextArea;

    @FXML
    private Label machineStatisticsHeader;
    private EncryptDecryptGridController encryptDecryptGridController;

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }

    public void setDecodeWord(String decodeWord) {
        statisticTextArea.setText(decodeWord);
    }
}
