package DesktopUserInterface.MainScene.BodyScene.Machine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MachineDetailsController {

    @FXML private GridPane machineGrid;
    @FXML private GridPane machineDetailsGrid;
    @FXML private Label totalAmountRotorsLabel;
    @FXML private Label currentAmountRotorsLabel;
    @FXML private Label totalAmountReflectorsLabel;
    @FXML private Label messagesAmountLabel;
    @FXML private Label originalCodeLabel;
    @FXML private Label currentCodeLabel;

    private MachineGridController machineGridController;

    public void setMachineGridController(MachineGridController machineGridController) {
        this.machineGridController = machineGridController;
    }
}
