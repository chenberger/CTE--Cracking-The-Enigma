package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import EncryptDecryptActionsPane.EncryptDecryptActionsGridController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

public class DictionarySearchScene {
    private DictionarySearchWindowController dictionarySearchWindowController;
    private Stage dictionarySearchStage;
    private EncryptDecryptActionsGridController encryptDecryptActionsGridController;

    public void show(Set<String> dictionary, EncryptDecryptActionsGridController encryptDecryptActionsGridController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DictionarySearchWindow.fxml"));
        Parent load = fxmlLoader.load();

        dictionarySearchWindowController = (DictionarySearchWindowController) fxmlLoader.getController();
        dictionarySearchWindowController.setDictionary(dictionary);
        dictionarySearchWindowController.setEncryptDecryptActionsGridController(encryptDecryptActionsGridController);

        Scene scene = new Scene(load);
        dictionarySearchStage = new Stage();
        dictionarySearchStage.initModality(Modality.APPLICATION_MODAL);
        dictionarySearchStage.setScene(scene); // set the scene
        dictionarySearchStage.setTitle("Search from dictionary");
        dictionarySearchStage.show();
    }

    public void close() {
        dictionarySearchStage.close();
    }
}
