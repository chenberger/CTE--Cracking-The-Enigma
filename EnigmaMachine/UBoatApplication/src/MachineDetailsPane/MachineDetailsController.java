package MachineDetailsPane;

import DTO.MachineDetails;
import DesktopUserInterface.MainScene.BodyScene.Machine.MachineGridController;
import DesktopUserInterface.MainScene.Common.SkinType;
import UBoatMachinePane.UBoatMachinePaneController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class MachineDetailsController {

    @FXML private GridPane machineDetailsGrid;
    @FXML private Label totalAmountRotorsLabel;
    @FXML private Label currentAmountRotorsLabel;
    @FXML private Label totalAmountReflectorsLabel;
    @FXML private Label messagesAmountLabel;
    @FXML private TextArea originalCodeLabel;
    @FXML private TextArea currentCodeLabel;
    private Map<SkinType, String> skinPaths;
    private MachineGridController machineGridController;
    private UBoatMachinePaneController UBoatMachinePaneController;

    @FXML public void initialize() {
        initializeSkins();
    }

    private void initializeSkins() {
        int skinIndex = 1;
        skinPaths = new HashMap<>();
        for(SkinType skin : SkinType.values()) {
            skinPaths.put(skin, "machinedetailsSkin" + skinIndex++ + ".css");
        }
    }

    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }

    public void machineDetailsChanged(Object object, MachineDetails machineDetails) {
        totalAmountRotorsLabel.setText("The total amount of rotors that can be use in the machine is: " + machineDetails.getAmountOfTotalRotors());
        currentAmountRotorsLabel.setText("The current amount of rotors in use in the machine is: " + machineDetails.getAmountCurrentRotorsInUse());
        totalAmountReflectorsLabel.setText("The total amount of reflectors that can be use in the machine is: " + machineDetails.getAmountOfTotalReflectors());
        messagesAmountLabel.setText("The total amount of messages have been processed by the machine so far is: " + machineDetails.getMessagesCounter());
        originalCodeLabel.setText(String.valueOf(machineDetails.getOriginalMachineSettings()));
        currentCodeLabel.setText(String.valueOf(machineDetails.getCurrentMachineSettings()));
    }

    public void currentCodeChanged(Object source, String currentCode) {
        currentCodeLabel.setText(currentCode);
    }

    public void setSkin(SkinType skinType) {
        machineDetailsGrid.getStylesheets().clear();
        machineDetailsGrid.getStylesheets().add(String.valueOf(getClass().getResource(skinPaths.get(skinType))));
    }

    public void setUBoatMachinePaneController(UBoatMachinePaneController uBoatMachinePaneController) {
        this.UBoatMachinePaneController = uBoatMachinePaneController;
    }
}
