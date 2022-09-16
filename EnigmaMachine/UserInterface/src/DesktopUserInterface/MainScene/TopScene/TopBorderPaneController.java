package DesktopUserInterface.MainScene.TopScene;

import DesktopUserInterface.MainScene.Common.SkinType;
import DesktopUserInterface.MainScene.ErrorDialog;
import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import EnigmaMachineException.GeneralEnigmaMachineException;
import EnigmaMachineException.IllegalAgentsAmountException;
import EnigmaMachineException.MachineNotExistsException;
import EnigmaMachineException.NotXmlFileException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TopBorderPaneController {
    @FXML
    private GridPane TopOfScene;
    @FXML
    private Button LoadMachineButton;
    @FXML
    private Label CurrentFilePathTextField;
    @FXML
    private Label EnigmaMachineHeadlineTextField;

    @FXML private ComboBox<String> skinComboBox;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;
    private Map<SkinType, String> skinPaths;

    @FXML public void initialize() {
        initializeSkins();
        skinComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(SkinType.values()).map(Enum::name).collect(Collectors.toList())));
        skinComboBox.setValue(SkinType.CLASSIC.toString());
        setSkin(SkinType.CLASSIC);
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "TopBorderPaneSkin" + skinIndex++ + ".css");
        }
    }

    @FXML
    void OnLoadMachineButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(mainController.getStageWindow());

        try {
            enigmaMachineEngine.setMachineDetailsFromXmlFile(selectedFile.getPath());
            CurrentFilePathTextField.setText(selectedFile.getPath());
            mainController.machineLoaded();

        }
        catch (NotXmlFileException | JAXBException | FileNotFoundException | GeneralEnigmaMachineException |
               IllegalAgentsAmountException | MachineNotExistsException | CloneNotSupportedException ex) {
            new ErrorDialog(ex, "Failed to load the enigma machine from xml file");
        }
    }

    @FXML
    void onSkinSelected(ActionEvent event) {
        SkinType skinType = SkinType.valueOf(skinComboBox.getValue());
        setSkin(skinType);
        mainController.setSkin(skinType);
    }

    private void setSkin(SkinType skinType) {
        TopOfScene.getStylesheets().clear();
        TopOfScene.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
}
