package DesktopUserInterface.MainScene.BodyScene.Machine;

import DTO.MachineDetails;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ManuallyCodeInitializerScene {
    private ManuallyCodeController manuallyCodeController;
    private Stage manuallyCodeStage;
    private CodeCalibrationController codeCalibrationController;
    public void show(MachineDetails machineDetails, CodeCalibrationController codeCalibrationController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManuallyCodeInitializer.fxml"));
        Parent load = fxmlLoader.load();
        this.manuallyCodeController = (ManuallyCodeController) fxmlLoader.getController();
        manuallyCodeController.setMachineDetails(machineDetails);
        manuallyCodeController.setCodeCalibrationController(codeCalibrationController);
        manuallyCodeController.initializeControls();

        Scene scene = new Scene(load);
        manuallyCodeStage = new Stage();
        manuallyCodeStage.initModality(Modality.APPLICATION_MODAL);
        manuallyCodeStage.setScene(scene); // set the scene
        manuallyCodeStage.setTitle("Manually Code Configuration");
        manuallyCodeStage.show();
    }

    public void close() {
        manuallyCodeStage.close();
    }
}
