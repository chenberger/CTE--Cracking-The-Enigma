package CompetitionServlets;

import DTO.AgentCandidatesInformation;
import Engine.AgentsManager.Agent;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
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
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "BattleCandidatesServlet", urlPatterns = {"/BattleCandidates"})
public class BattleCandidatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            if (request.getParameter("action").equals("sendCandidates")) {
                sendCandidatesToBattle(request, response);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    synchronized private void sendCandidatesToBattle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Gson gson = new Gson();
        try {


            List<AgentCandidatesInformation> candidates = Arrays.asList(gson.fromJson(request.getReader(), AgentCandidatesInformation[].class));
            for (AgentCandidatesInformation candidate : candidates) {
                Agent agent = agentsManager.getAgent(candidate.getAgentName());
                Allie allie = alliesManager.getAllie(agent.getAllieName());
                String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
                UBoat uBoat = uBoatManager.getUBoat(uBoatName);
                uBoat.getBattleField().addAgentCandidateInformationToList(candidate);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
