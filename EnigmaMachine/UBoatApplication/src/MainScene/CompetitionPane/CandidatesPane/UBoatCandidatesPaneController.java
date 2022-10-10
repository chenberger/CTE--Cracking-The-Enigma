package MainScene.CompetitionPane.CandidatesPane;

import AllieMainScenePane.Body.ContestTabPane.ContestTabPaneController;
import MainScene.CompetitionPane.UBoatCompetitionPaneController;

import java.io.Closeable;

public class UBoatCandidatesPaneController implements Closeable {
    private UBoatCompetitionPaneController uBoatCompetitionPaneController;
    //TODO : Make sure that the dependency that i added(this client on the allies client) does not create circular dependency.
    //private ContestTabPaneController contestTabPaneController;

    public void setUBoatCompetitionPaneController(UBoatCompetitionPaneController uBoatCompetitionPaneController) {
        this.uBoatCompetitionPaneController = uBoatCompetitionPaneController;
    }
    @Override
    public void close()  {
        //TODO : Close the timer task that refreshes the candidates table.
    }
   // public void setContestTabPaneController(ContestTabPaneController contestTabPaneController) {
    //    this.contestTabPaneController = contestTabPaneController;
   // }
}
