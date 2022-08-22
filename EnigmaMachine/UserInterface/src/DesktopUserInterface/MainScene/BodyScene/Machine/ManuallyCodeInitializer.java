package DesktopUserInterface.MainScene.BodyScene.Machine;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManuallyCodeInitializer {
    public void show() throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("ManuallyCodeInitializer.fxml"));
        Scene scene = new Scene(load);
        Stage secondStage = new Stage();
        secondStage.setScene(scene); // set the scene
        secondStage.setTitle("Second Form");
        secondStage.show();
    }
}
