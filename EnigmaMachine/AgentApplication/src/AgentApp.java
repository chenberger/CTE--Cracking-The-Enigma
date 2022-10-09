import AgentLoginPane.AgentLoginPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AgentApp extends Application {
    private static final String APP_NAME = "Agent";
    private static final String MAIN_PAGE_FXML_RESOURCE_LOCATION = "AgentMainScenePane/AgentMainScenePane.fxml";
    private static final String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "AgentLoginPane/AgentLoginPane.fxml";
    private AgentLoginPaneController AgentLoginPaneController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(APP_NAME);
        URL loginPage = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            AgentLoginPaneController = fxmlLoader.getController();


            Scene scene = new Scene(root,1200,800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
