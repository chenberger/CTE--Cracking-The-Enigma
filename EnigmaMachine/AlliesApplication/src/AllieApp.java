import AllieLoginPane.AllieLoginPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AllieApp extends Application {
    private static final String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/AllieLoginPane/AllieLoginPane.fxml";
    private AllieLoginPaneController allieLoginPaneController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Allie");
        URL loginPage = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            allieLoginPaneController = fxmlLoader.getController();


            Scene scene = new Scene(root,1200,800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
