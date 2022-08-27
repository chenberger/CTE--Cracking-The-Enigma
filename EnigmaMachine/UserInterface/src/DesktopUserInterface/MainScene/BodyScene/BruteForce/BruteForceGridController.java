package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import DesktopUserInterface.MainScene.MainController;
import Engine.EngineManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class BruteForceGridController {
    @FXML
    private GridPane BruteForceGrid;
    @FXML GridPane candidatesAndProgressGrid;
    @FXML
    CandidatesAndProgressGridController candidatesAndProgressController;
    @FXML GridPane dMOperationalGrid;
    @FXML
    DMOperationalGridController dmOperationalGridController;
    @FXML GridPane encryptDecryptActionsGrid;
    @FXML
    EncryptDecryptActionsGridController encryptDecryptActionsController;
    private MainController mainController;
    private EngineManager enigmaMachineEngine;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngineManager(EngineManager enigmaMachineEngine) {
        this.enigmaMachineEngine = enigmaMachineEngine;
    }
    public  void initialize(){
        if(candidatesAndProgressController != null){
            candidatesAndProgressController.setBruteForceGridController(this);
        }
        if(dmOperationalGridController != null){
            dmOperationalGridController.setBruteForceGridController(this);
        }
        if (encryptDecryptActionsController != null){
            encryptDecryptActionsController.setBruteForceGridController(this);
        }
    }
    private void registerToEvents(){
        //TODO:chen/erez - register here when needed
    }

}
