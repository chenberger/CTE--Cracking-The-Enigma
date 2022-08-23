package DesktopUserInterface.MainScene.BodyScene.Machine;

import DTO.MachineDetails;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManuallyCodeInitializerScene {
    private ManuallyCodeController manuallyCodeController;
    public void show(MachineDetails machineDetails) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManuallyCodeInitializer.fxml"));
        Parent load = fxmlLoader.load();
        this.manuallyCodeController = (ManuallyCodeController) fxmlLoader.getController();
        manuallyCodeController.setMachineDetails(machineDetails);
        manuallyCodeController.initializeControls();

        Scene scene = new Scene(load);
        Stage secondStage = new Stage();
        secondStage.setScene(scene); // set the scene
        secondStage.setTitle("Manually Code Configuration");
        secondStage.show();
    }
}
