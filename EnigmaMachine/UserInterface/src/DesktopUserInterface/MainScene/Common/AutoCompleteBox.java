package DesktopUserInterface.MainScene.Common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

import java.util.Set;

public class AutoCompleteBox<T> implements EventHandler{
    private ComboBox comboBox;
    private Set<T> data;


    public AutoCompleteBox(final ComboBox comboBox) {
        this.comboBox = comboBox;
        this.doAutoCompleteBox();
    }

    private void doAutoCompleteBox() {
        this.comboBox.setEditable(true);

        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            comboBox.getEditor().setText(oldValue.toString() + newValue.toString());
            comboBox.show();
        });

        this.comboBox.setOnKeyPressed(t -> comboBox.hide());
        this.comboBox.setOnKeyReleased(AutoCompleteBox.this);
    }

    @Override
    public void handle(Event event) {
        setItems();
    }

    private void setItems() {
        ObservableList list = FXCollections.observableArrayList();
        String[] comboBoxText = this.comboBox.getEditor().getText().toUpperCase().split(" ");
        String textToSearchInDict = comboBoxText[comboBoxText.length-1];

        for (Object datum : this.data) {
            if (datum.toString().toUpperCase().contains(textToSearchInDict.toUpperCase())) {
                list.add(datum.toString());
            }
        }

        if(list.isEmpty()) this.comboBox.hide();

        this.comboBox.setItems(list);
        this.comboBox.show();
    }

    private void moveCaret(int textLength) {
        this.comboBox.getEditor().positionCaret(textLength);
    }

    public void setData(Set<T> data) {
        this.data = data;
        setItems();
    }
}