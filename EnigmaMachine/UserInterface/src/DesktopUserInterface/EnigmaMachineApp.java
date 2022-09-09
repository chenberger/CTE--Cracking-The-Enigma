package DesktopUserInterface;

import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EnigmaMachineApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Enigma Machine");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainScene/MainSceneBorderPane.fxml"));
        Parent load = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        EngineManager engineManager = new EngineManager();

        engineManager.setMainController(mainController);
        mainController.setEnigmaEngine(engineManager);
        Scene scene = new Scene(load);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

