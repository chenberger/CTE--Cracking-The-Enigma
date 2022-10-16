package CompetitionServlets;

import DTO.AgentCandidatesInformation;
import DTO.AlliesTasksProgressToLabels;
import DTO.AllyCandidatesTable;
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
import java.util.Arrays;
import java.util.List;

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "BattleCandidatesServlet", urlPatterns = {"/BattleCandidates"})
public class BattleCandidatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            if (request.getParameter("action").equals("sendCandidates")) {
                sendCandidatesToBattle(request, response);
            }
            else if(request.getParameter("action").equals("updateAlliesProgress")){
                updateAlliesProgress(request, response);
            }
            else if(request.getParameter("action").equals("getAllieProgress")){
                getAllyProgress(request, response);
            }
            else if(request.getParameter("action").equals("updateCandidatesFound")){
                updateNumberOfCandidatesFoundByAgent(request, response);
            }
            else if(request.getParameter("action").equals("updateTasksPulled")){
                updateNumberOfTasksPulledByAgent(request, response);
            }
            else if(request.getParameter("action").equals("getAlliesCandidates")){
                getAlliesCandidates(request,response);
            }
            else if(request.getParameter("action").equals("UpdateTasksCompleted")){
                updateAgentTasksCompleted(request, response);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    private void updateAgentTasksCompleted(HttpServletRequest request, HttpServletResponse response) {
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        Agent agent = agentsManager.getAgent(SessionUtils.getAgentName(request));
        agent.updateTasksDone(GSON_INSTANCE.fromJson(request.getParameter("tasksCompleted"), Long.class));
    }

    private synchronized void getAlliesCandidates(HttpServletRequest request, HttpServletResponse response) {
        List<AllyCandidatesTable> allyCandidatesTables = new ArrayList<>();
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        try {
            UBoat uBoat = uBoatManager.getUBoat(SessionUtils.getUsername(request));

            if(uBoat == null){
                AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
                Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
                String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
                uBoat = uBoatManager.getUBoat(uBoatName);
            }

            BattleField battleField = uBoat.getBattleField();
            List<AgentCandidatesInformation> agentCandidatesInformation = battleField.getAgentsCandidatesInformation();
            for (AgentCandidatesInformation agentCandidate : agentCandidatesInformation) {
                allyCandidatesTables.add(new AllyCandidatesTable(agentCandidate.getCandidateString(), agentCandidate.getTeamName(), agentCandidate.getConfigurationOfTask()));
            }
            if(allyCandidatesTables.size() > 0){
                Gson gson = new Gson();
                String json = gson.toJson(allyCandidatesTables);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(json);
                response.getWriter().flush();
            }
            else{
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }

        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    private synchronized void updateNumberOfTasksPulledByAgent(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        Agent agent = agentsManager.getAgent(request.getParameter("agentName"));
        Long tasksPulled = gson.fromJson(request.getParameter("numberOfTasksPulled"), Long.class);
        agent.addNumberOfTasksPulled(tasksPulled);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private synchronized void updateNumberOfCandidatesFoundByAgent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        try {

            String agentName = request.getParameter("agentName");
            int numberOfCandidatesFound = Integer.parseInt(request.getParameter("numberOfCandidatesFound"));
            AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
            Agent agent = agentsManager.getAgent(agentName);
            agent.setTotalNumberOfCandidatesFound(numberOfCandidatesFound);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.getWriter().println(gson.toJson("Unable to update number of candidates found by agent"));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private synchronized void getAllyProgress(HttpServletRequest request, HttpServletResponse response) {
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
        try {
            Gson gson = new Gson();
            AlliesTasksProgressToLabels alliesTasksProgressToLabels = new AlliesTasksProgressToLabels(
                    allie.getTaskSize(), allie.getTasksCompleted(), allie.getTasksCompleted());
            String jsonAllieProgress = gson.toJson(alliesTasksProgressToLabels);
            response.getWriter().write(jsonAllieProgress);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private synchronized void updateAlliesProgress(HttpServletRequest request, HttpServletResponse response) {
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Agent agent = agentsManager.getAgent(SessionUtils.getUsername(request));
        try {
            Gson gson = new Gson();
            Allie allie = alliesManager.getAllie(agent.getAllieName());
            Long jsonTasksCompleted = gson.fromJson(request.getParameter("tasksCompleted"), Long.class);
            allie.increaseTasksCompleted(jsonTasksCompleted);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Allie progress updated successfully");
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

     private synchronized void sendCandidatesToBattle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Gson gson = new Gson();
        try {
            String agentCandidates = request.getParameter("candidatesList");
            AgentCandidatesInformation[] candidates = gson.fromJson(agentCandidates, AgentCandidatesInformation[].class);
            for (AgentCandidatesInformation candidate : candidates) {
                Agent agent = agentsManager.getAgent(candidate.getAgentName());
                Allie allie = alliesManager.getAllie(agent.getAllieName());
                String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
                UBoat uBoat = uBoatManager.getUBoat(uBoatName);
                candidate.setTeamName(allie.getTeamName());
                uBoat.getBattleField().addAgentCandidateInformationToList(candidate);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
