package AllieMainScenePane.Body.ChatTabPane.chatroom;

import AllieMainScenePane.Body.ChatTabPane.chatarea.ChatAreaController;
import AllieMainScenePane.Body.ChatTabPane.users.UsersListController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.io.IOException;

public class ChatRoomMainController implements Closeable {

    @FXML private VBox usersListComponent;
    @FXML private UsersListController usersListComponentController;
    @FXML private VBox actionCommandsComponent;
    @FXML private GridPane chatAreaComponent;
    @FXML private ChatAreaController chatAreaComponentController;

    @FXML
    public void initialize() {
        setActive();
    }

    @Override
    public void close() throws IOException {
        usersListComponentController.close();
        chatAreaComponentController.close();
    }

    public void setActive() {
        usersListComponentController.startListRefresher();
        chatAreaComponentController.startListRefresher();
    }

    public void setInActive() {
        try {
            usersListComponentController.close();
            chatAreaComponentController.close();
        } catch (Exception ignored) {}
    }
}
