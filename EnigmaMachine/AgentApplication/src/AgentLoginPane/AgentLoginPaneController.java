package AgentLoginPane;

import DesktopUserInterface.MainScene.ErrorDialog;
import Engine.EngineManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.HttpUrl;

import static AgentsServletsPaths.AgentServletsPaths.REGISTER_TO_ALLY_SERVLET;

public class AgentLoginPaneController {
    private String agentName;
    private String chosenTeam;
    private Integer numberOfThreads;
    private Long tasksPulledEachTime;
    private EngineManager engineManager;

    @FXML
    private TextField setAgentNameTextField;

    @FXML
    private Button setAgentNameButton;

    @FXML
    private TableView<String> teamNameTable;

    @FXML
    private TableColumn<?, ?> TeamsToChooseTable;

    @FXML
    private Button registerToTeamButton;

    @FXML
    private Label chosenTeamLabel;

    @FXML
    private Slider numberOfThreadsBar;

    @FXML
    private Button numberOfThreadsButton;

    @FXML
    private TextField tasksPulledTextField;

    @FXML
    private Button tasksPulledButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button quitButton;
    public AgentLoginPaneController() {
        this.agentName = "";
        this.chosenTeam = "";
        this.numberOfThreads = 0;
        this.tasksPulledEachTime = 0L;
    }

    @FXML
    void onLoginButtonClicked(ActionEvent event) {
        if(agentName.equals("") || chosenTeam.equals("") || numberOfThreads == 0 || tasksPulledEachTime == 0) {
            new ErrorDialog(new Exception("Please fill all the fields"), "Error");
        } else {
            registerToAlly();
        }
    }

    private void registerToAlly() {
        String finalUrl = HttpUrl.parse(REGISTER_TO_ALLY_SERVLET).
                newBuilder().
                addQueryParameter("action", "registerToAlly").
                addQueryParameter("agentName", agentName).
                addQueryParameter("chosenTeam", chosenTeam).
                addQueryParameter("numberOfThreads", numberOfThreads.toString()).
                addQueryParameter("tasksPulledEachTime", tasksPulledEachTime.toString()).
                build().
                toString();
    }

    @FXML
    void onNumberOfThreadsButtonClicked(ActionEvent event) {
        numberOfThreads = (int) numberOfThreadsBar.getValue();
    }

    @FXML
    void onQuitButtonClicked(ActionEvent event) {

    }

    @FXML
    void onRegisterToTeamButtonClicked(ActionEvent event) {
        try {
            chosenTeam = getChosenTeam();
        }
        catch (Exception e){
            new ErrorDialog(e, "Failed to get chosen team");
        }
    }

    private String getChosenTeam() throws Exception {
        ObservableList<String> chosenTeam = teamNameTable.getSelectionModel().getSelectedItems();
        if(chosenTeam.size() != 1) {
            throw new Exception("You must choose only one team!!");
        }
        else {
            return chosenTeam.get(0);
        }
    }

    @FXML
    void onSetAgentNameButtonClicked(ActionEvent event) {
        agentName = setAgentNameTextField.getText();
    }

    @FXML
    void onTasksPulledButtonClicked(ActionEvent event) {
        try {
            tasksPulledEachTime = Long.parseLong(tasksPulledTextField.getText());
            if(tasksPulledEachTime < 0) {
                tasksPulledEachTime = 0L;
                throw new Exception("Tasks pulled each time must be positive");
            }
        }
        catch (Exception e) {
            new ErrorDialog(e, "Error");
        }

    }
}
