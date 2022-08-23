package DesktopUserInterface.MainScene.TopScene;

import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import EnigmaMachineException.GeneralEnigmaMachineException;
import EnigmaMachineException.NotXmlFileException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

public class TopBorderPaneController {
    @FXML
    private Button LoadMachineButton;
    @FXML
    private Label CurrentFilePathTextField;
    @FXML
    private Label EnigmaMachineHeadlineTextField;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;

    @FXML
    void OnLoadMachineButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(mainController.getStageWindow());

        try {
            enigmaMachineEngine.setMachineDetailsFromXmlFile(selectedFile.getPath());
            mainController.machineLoaded();
            CurrentFilePathTextField.setText(selectedFile.getPath());
        }
        catch (NotXmlFileException | JAXBException | FileNotFoundException | GeneralEnigmaMachineException ex) {
            new ErrorDialog(ex, "Failed to load the enigma machine from xml file");
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
}
