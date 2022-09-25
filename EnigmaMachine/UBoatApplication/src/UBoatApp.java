import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import MainScene.MainUBoatScenePaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class UBoatApp extends Application {
    private static final String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/MainScene/MainUBoatScenePane.fxml";
    private MainUBoatScenePaneController mainUBoatScenePaneController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("U-Boat");
        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            mainUBoatScenePaneController = fxmlLoader.getController();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
