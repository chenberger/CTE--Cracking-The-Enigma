package AllieMainScenePane.Body.DashBoardTabPane;

import AllieMainScenePane.AllieMainScenePaneController;
import AllieMainScenePane.Body.DashBoardTabPane.ContestData.ContestDataPaneController;
import AllieMainScenePane.Body.DashBoardTabPane.ContestData.IllegibleContestAmountChosenException;
import AllieMainScenePane.Body.DashBoardTabPane.TeamAgentsData.TeamAgentsDataPaneController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class DashboardTabPaneController {

    private AllieMainScenePaneController allieMainScenePaneController;
    @FXML private AnchorPane teamAgentsDataPane;
    @FXML private TeamAgentsDataPaneController teamAgentsDataPaneController;

    @FXML private AnchorPane contestsDataPane;
    @FXML private ContestDataPaneController contestsDataPaneController;

    //TODO: Erez: get the name of the boat from the line the user marked(when he clicked the register button).
    public void initialize() {
        if(teamAgentsDataPaneController != null) {
            teamAgentsDataPaneController.setDashboardTabPaneController(this);
        }
        if(contestsDataPaneController != null) {
            contestsDataPaneController.setDashboardTabPaneController(this);
        }
        setActive();
    }

    public void setAllieMainScenePaneController(AllieMainScenePaneController allieMainScenePaneController) {
        this.allieMainScenePaneController = allieMainScenePaneController;
    }

    public void setActive() {
        teamAgentsDataPaneController.startListRefresher();
        contestsDataPaneController.startListRefresher();

    }


    public String getSelectedContest() throws IllegibleContestAmountChosenException {
        return contestsDataPaneController.getSelectedContest().getBoatName();
    }

    public void close() {
        teamAgentsDataPaneController.close();
        contestsDataPaneController.close();

    }

    public int getAmountOfAgents() {
        return teamAgentsDataPaneController.getAmountOfAgentInTheTeam();
    }

    public void moreThenOneAgentJoined() {
        allieMainScenePaneController.moreThenOneAgentJoined();
    }

    public void setContest(boolean isContestReady) {
        contestsDataPaneController.setContestProperty(isContestReady);
    }

    public SimpleBooleanProperty getContestSetProperty() {
        return contestsDataPaneController.getIsContestSetProperty();
    }

    public void onRegisterToBattleButtonClicked(ActionEvent actionEvent) {
        allieMainScenePaneController.onRegisterToBattleButtonClicked(actionEvent);
    }

    public void noAgentsJoined() {
        allieMainScenePaneController.noAgentsJoined();

    }
}
