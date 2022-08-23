package DesktopUserInterface.MainScene.BodyScene.Machine;

import DTO.MachineDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MachineDetailsController {

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

    public void machineDetailsChanged(MachineDetails machineDetails) {
        totalAmountRotorsLabel.setText(String.valueOf(machineDetails.getAmountOfTotalRotors()));
        currentAmountRotorsLabel.setText(String.valueOf(machineDetails.getAmountCurrentRotorsInUse()));
        totalAmountReflectorsLabel.setText(String.valueOf(machineDetails.getAmountOfTotalReflectors()));
        messagesAmountLabel.setText(String.valueOf(machineDetails.getMessagesCounter()));
        originalCodeLabel.setText(String.valueOf(machineDetails.getOriginalMachineSettings()));
        currentCodeLabel.setText(String.valueOf(machineDetails.getCurrentMachineSettings()));
    }
}
