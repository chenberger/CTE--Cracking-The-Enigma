package DesktopUserInterface.MainScene;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class ErrorDialog {

    private final Alert alert;
    private final TextArea textArea;

    public ErrorDialog(Exception ex, String title) {
        this.textArea = new TextArea(ex.getMessage());
        this.textArea.setEditable(false);
        this.alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error : " + title);
        alert.getDialogPane().setContent(textArea);
        alert.setResizable(true);
        alert.showAndWait();
    }
}