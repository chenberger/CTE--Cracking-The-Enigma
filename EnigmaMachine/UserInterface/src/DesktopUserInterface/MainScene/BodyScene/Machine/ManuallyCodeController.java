package DesktopUserInterface.MainScene.BodyScene.Machine;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ManuallyCodeController {
    @FXML private Button setCodeButton;
    private CodeCalibrationController codeCalibrationController;

    @FXML void OnSetCodeButtonClicked(ActionEvent event) {
        //codeCalibrationController.codeConfigurationSettled();
    }

    public void show() throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("ManuallyCodeInitializer.fxml"));
        Scene scene = new Scene(load);
        Stage secondStage = new Stage();
        secondStage.setScene(scene); // set the scene
        secondStage.setTitle("Second Form");
        secondStage.show();
    }

    public void setCodeCalibrationController(CodeCalibrationController codeCalibrationController) {
        this.codeCalibrationController = codeCalibrationController;
    }
}
