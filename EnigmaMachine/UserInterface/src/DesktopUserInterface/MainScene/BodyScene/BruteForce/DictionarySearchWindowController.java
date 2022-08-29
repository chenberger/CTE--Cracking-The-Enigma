package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DictionarySearchWindowController {
    @FXML private TextField searchBar;

    @FXML private ListView<String> listView;
    private Set<String> dictionary;
    private EncryptDecryptActionsGridController encryptDecryptActionsGridController;
    private StringProperty searchFieldProperty;


    @FXML public void initialize() {
        searchBar.textProperty().addListener((obs, oldText, newText) -> {
            changeListView();
        });
    }
    @FXML void selectButtonClicked(ActionEvent event) {
        encryptDecryptActionsGridController.wordSelectedFromDictionary(listView.getSelectionModel().getSelectedItem());
    }

    @FXML void searchBarChanged(ActionEvent event) {
        changeListView();
    }

    private void changeListView() {
        listView.getItems().clear();

        if(searchBar.getText().length() == 0) {
            listView.getItems().addAll(dictionary);
        }
        else {
            listView.getItems().addAll(searchList(searchBar.getText()));
        }
    }

    @FXML void searchBarTextChanged(InputMethodEvent event) {
        changeListView();
    }

    private List<String> searchList(String searchWords) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return dictionary.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
        listView.getItems().addAll(dictionary);
    }

    public void setEncryptDecryptActionsGridController(EncryptDecryptActionsGridController encryptDecryptActionsGridController) {
        this.encryptDecryptActionsGridController = encryptDecryptActionsGridController;
    }
}
