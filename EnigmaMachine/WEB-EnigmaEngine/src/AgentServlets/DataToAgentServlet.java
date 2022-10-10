package AgentServlets;

import DTO.AgentContestAndTeamData;
import Engine.AgentsManager.Agent;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import Engine.UBoatManager.UBoat;
import Engine.UBoatManager.UBoatManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "DataToAgentServlet", urlPatterns = {"/agent/DataToAgent"})
public class DataToAgentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        if(request.getParameter("action").equals("getContestAndTeamData")){
            getContestAndTeamData(request, response);
        }
        else if(request.getParameter("action").equals("getAgentProgressAndStatus")){
            getAgentProgressAndStatus(request, response);
        }
        else{//get the agent's candidates
            getAgentCandidates(request, response);

        }
    }

    synchronized private void getAgentCandidates(HttpServletRequest request, HttpServletResponse response) {

    }

    synchronized private void getAgentProgressAndStatus(HttpServletRequest request, HttpServletResponse response) {
       //try{
       //    AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
       //    String agentNameFromSession = SessionUtils.getAgentName(request);
       //
       //}
    }

    synchronized private void getContestAndTeamData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String contestStatus;
            String teamName;
            String battleName;
            Gson gson = new Gson();

            AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
            Agent agent = agentsManager.getAgent(SessionUtils.getAgentName(request));

            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            Allie allie = alliesManager.getAllie(agent.getAllieName());

            teamName = allie.getTeamName();
            if(allie.getBattleName().equals("")){
                battleName = "N/A";
                contestStatus = "N/A";
            }
            else{
                UBoatManager uBoatManager = ServletUtils.getUBoatsManager(getServletContext());
                String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
                UBoat uBoat = uBoatManager.getUBoat(uBoatName);

                contestStatus = uBoat.getContestStatus();
                battleName = allie.getBattleName();
            }
            String jsonStringOfData = gson.toJson(new AgentContestAndTeamData(teamName, battleName, contestStatus));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(jsonStringOfData);
            response.getWriter().flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}
