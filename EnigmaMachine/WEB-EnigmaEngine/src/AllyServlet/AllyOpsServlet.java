package AllyServlet;

import DTO.AgentProgressDataToTable;
import DTO.AgentsProgressAndDataTable;
import DTO.AlliesTasksProgressToLabels;
import DTO.OnLineContestsTable;
import Engine.AgentsManager.Agent;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import Engine.BattleField;
import Engine.UBoatManager.UBoat;
import Engine.UBoatManager.UBoatManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "AllyOpsServlet",urlPatterns = {"/AllyOps"})
public class AllyOpsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        if(request.getParameter("action").equals("setTaskSize")) {
            setAllyTaskSize(request, response);
        }
        else if(request.getParameter("action").equals("getCurrentContestData")){
            getCurrentContestData(request, response);
        }
        else if(request.getParameter("action").equals("getAgentsProgressDataToTable")){
            getAgentsProgressDataToTable(request, response);
        }
        else if(request.getParameter("action").equals("getAlliesTasksProgressToLabels")){
            getAlliesTasksProgressToLabels(request, response);
        }
        else if(request.getParameter("action").equals("quitFromBattle")) {
            quitFromBattle(request, response);
        }
        else{
            checkIfAgentParticipateInContest(request, response);
        }
    }

    private void quitFromBattle(HttpServletRequest request, HttpServletResponse response) {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
        if(allie != null) {
            allie.setBattleName("");
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private synchronized void getAlliesTasksProgressToLabels(HttpServletRequest request, HttpServletResponse response) {
        try {
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
            long totalTasksCompletedByAlly = allie.getTotalTasksCompleted();
            AlliesTasksProgressToLabels alliesTasksProgressToLabels = new AlliesTasksProgressToLabels(allie.getTotalNumberOfTasks(),
                    totalTasksCompletedByAlly, allie.getTasksProduced());
            Gson gson = new Gson();
            String json = gson.toJson(alliesTasksProgressToLabels);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
            response.getWriter().flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private synchronized void getAgentsProgressDataToTable(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<AgentsProgressAndDataTable> agentsProgressDataToTable = new ArrayList<>();
            AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
            List<Agent> agents = allie.getAgents();

            for(Agent agent : agents){
                agentsProgressDataToTable.add(new AgentsProgressAndDataTable(agent.getAgentName(), getTasksPulledAndDone(agent),agent.getTotalNumberOfCandidatesFound()));
            }

            if(agentsProgressDataToTable.size() > 0){
                String json = GSON_INSTANCE.toJson(agentsProgressDataToTable);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
                response.getWriter().flush();
            }
            else{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("No agents in this allie");
            }

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private synchronized String getTasksPulledAndDone(Agent agent) {
        return  agent.getNumberOfTasksDone() + "/" + agent.getNumberOfTasksPulled();
    }

    private synchronized void checkIfAgentParticipateInContest(HttpServletRequest request, HttpServletResponse response) {
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Agent agent = agentsManager.getAgent(request.getParameter("agentName"));
        Allie allie = alliesManager.getAllie(agent.getAllieName());
        if(allie.getAgentsParticipatingInDecryption().contains(agent)){
            try {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(GSON_INSTANCE.toJson(true));
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else{
            try {
                response.getWriter().println(GSON_INSTANCE.toJson(false));
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private synchronized void getCurrentContestData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
        if(uBoatName != null) {
            UBoat uBoat = uBoatManager.getUBoat(uBoatName);
            BattleField battleField = uBoat.getBattleField();
            String teamsRegisteredAndNeeded = battleField.getNumberOfTeamsInBattleField() + "/" + battleField.getNumberOfAlliesToStartBattle();
            OnLineContestsTable onLineContestsTable = new OnLineContestsTable(battleField.getBattleFieldName(), uBoat.getName(), uBoat.getContestStatus(),
                    battleField.getLevel().name(),teamsRegisteredAndNeeded);
            String json = GSON_INSTANCE.toJson(onLineContestsTable);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
            response.getWriter().flush();
        }
        else{
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        UBoat uBoat = uBoatManager.getUBoat(uBoatName);
    }

     private synchronized void setAllyTaskSize(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long taskSize = Long.parseLong(request.getParameter("taskSize"));
        AlliesManager  alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
        response.setStatus(HttpServletResponse.SC_OK);
        allie.setTaskSize(taskSize);
    }

    //create the response json string
}
