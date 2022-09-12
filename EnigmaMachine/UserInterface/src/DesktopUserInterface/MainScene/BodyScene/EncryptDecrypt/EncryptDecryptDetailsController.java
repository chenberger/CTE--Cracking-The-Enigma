package DesktopUserInterface.MainScene.BodyScene.EncryptDecrypt;

import DesktopUserInterface.MainScene.ErrorDialog;
import EnigmaMachineException.MachineNotExistsException;
import EnigmaMachineException.SettingsNotInitializedException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Set;


public class EncryptDecryptDetailsController {

    @FXML private TextField encryptDecryptTextBox;
    @FXML private Button encryptDecryptButton;
    @FXML private Button ResetMachineStateButton;
    @FXML private Label encryptDecryptTextLabel;
    @FXML private ScrollPane encryptDecryptWordsScrollPane;
    @FXML private RadioButton fullWordButton;
    @FXML private RadioButton letterByLetterButton;
    @FXML private Button clearButton;
    @FXML private TextField decodedWordsTextArea;
    @FXML private Label encryptDecryptTextLabel1;
    private EncryptDecryptGridController encryptDecryptGridController;

    private SimpleBooleanProperty fullWordDecodingProperty;
    private SimpleBooleanProperty fileLoadedProperty;

    public EncryptDecryptDetailsController() {
        fullWordDecodingProperty = new SimpleBooleanProperty(true);
        fileLoadedProperty = new SimpleBooleanProperty(false);
    }

    public void setEncryptDecryptGridController(EncryptDecryptGridController encryptDecryptGridController) {
        this.encryptDecryptGridController = encryptDecryptGridController;
    }

    @FXML public void initialize() {
        encryptDecryptTextBox.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!fullWordDecodingProperty.get() && newValue != null && newValue.length() > 0) {
                    encryptDecryptTextBox.setText(newValue);
                    encryptDecryptGridController.decodeWord(String.valueOf(encryptDecryptTextBox.getText().charAt(newValue.length() - 1)));
                }
            }
        });


        ToggleGroup toggleGroup = new ToggleGroup();
        fullWordButton.setToggleGroup(toggleGroup);
        letterByLetterButton.setSelected(true);
        letterByLetterButton.setToggleGroup(toggleGroup);

        fullWordButton.disableProperty().bind(fileLoadedProperty.not());
        letterByLetterButton.disableProperty().bind(fileLoadedProperty.not());
    }


    @FXML void onClearButtonClicked(ActionEvent event) {
        encryptDecryptTextBox.setText("");
        decodedWordsTextArea.setText("");
        encryptDecryptGridController.clearCurrentProccessedWord();
    }

    private void setScrollPaneColor(){
        encryptDecryptWordsScrollPane.setStyle("-fx-background-color: transparent;");
    }
    @FXML void onDecryptionButtonClicked(ActionEvent event) throws MachineNotExistsException, CloneNotSupportedException, SettingsNotInitializedException {
            try {
                if (fullWordDecodingProperty.get()) {
                    encryptDecryptGridController.decodeWord(encryptDecryptTextBox.getText());
                    encryptDecryptTextBox.clear();
                } else {
                    //TODO: chen - add statistics change only when pushes button.
                    decodedWordsTextArea.clear();
                }
                if(encryptDecryptTextBox.textProperty().get().length() > 0 || fullWordDecodingProperty.get()) {
                    encryptDecryptGridController.onFinishInput();
                }
            } catch (MachineNotExistsException | CloneNotSupportedException | IllegalArgumentException ignored) {
            }
            encryptDecryptTextBox.clear();
}
    @FXML void onLetterByLetterButtonPressed(ActionEvent event) {
        fullWordDecodingProperty.set(false);
        encryptDecryptButton.setText("Done");
        encryptDecryptTextBox.clear();
        decodedWordsTextArea.clear();
    }
    @FXML private void onResetMachineStateButtonClicked(ActionEvent event) {
        encryptDecryptGridController.resetMachineState();
        encryptDecryptGridController.clearCurrentProccessedWord();
    }
    @FXML private void onFullWordButtonPressed(ActionEvent event){
        fullWordDecodingProperty.set(true);
        encryptDecryptButton.setText("Process");
        encryptDecryptTextBox.clear();
        decodedWordsTextArea.clear();
    }


    public void setDecodedWord(String decodeWord) {
        if(fullWordDecodingProperty.get()) {
            decodedWordsTextArea.setText(decodeWord);
        }
        else{
            decodedWordsTextArea.setText(decodedWordsTextArea.getText() + decodeWord);
        }
    }

    public void enableToProcess(Object o, Set<Character> characters) {
        fileLoadedProperty.set(true);
        fullWordDecodingProperty.set(false);
    }

    public SimpleBooleanProperty getFullWordDecodingProperty(){
        return fullWordDecodingProperty;
    }

    public void clearDecodingTextArea() {
        encryptDecryptTextBox.clear();
    }
    public void clearDecodedTextArea() {
        decodedWordsTextArea.clear();
    }

    public void keyboardButtonClicked(Character keyboardValue) {
        encryptDecryptTextBox.setText(encryptDecryptTextBox.getText() + keyboardValue);
    }
}
