import LoginPane.UBoatLoginPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class UBoatApp extends Application {
    private static final String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/MainScene/MainUBoatScenePane.fxml";
    private static final String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/LoginPane/UBoatLoginPane.fxml";
    private UBoatLoginPaneController uBoatLoginPaneController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("U-Boat");
        URL loginPage = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            uBoatLoginPaneController = fxmlLoader.getController();


            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
