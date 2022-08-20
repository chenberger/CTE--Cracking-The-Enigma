package DesktopUserInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EnigmaMachineApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Enigma Machine");

        Parent load = FXMLLoader.load(getClass().getResource("RootSceneBorderPane/RootSceneBorderPane.fxml"));
        Scene scene = new Scene(load, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
