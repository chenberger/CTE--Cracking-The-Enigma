package UsersManagerServlets;

import Engine.AgentsManager.Agent;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import Engine.UBoatManager.UBoat;
import Engine.UBoatManager.UBoatManager;
import UserManager.UsersManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "LogOutServlet", urlPatterns = {"/users/logout"})
public class LogOutServlet extends HttpServlet {

    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        if(request.getParameter("action").equals("uBoatLogout")) {
            uBoatLogout(request, response);
        }
        else if(request.getParameter("action").equals("alliesLogout")) {
            alliesLogout(request, response);
        }
        else if(request.getParameter("action").equals("agentLogout")) {
            agentLogout(request, response);
        }
        else if(request.getParameter("action").equals("checkIfAgentTeamLoggedOut")) {
            checkIfAgentTeamLoggedOut(request, response);
        }


    }

    private synchronized void checkIfAgentTeamLoggedOut(HttpServletRequest request, HttpServletResponse response) {
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        Agent agent = agentsManager.getAgent(SessionUtils.getAgentName(request));

        if(agent.getAgentRegistrationStatus() == true){
            try {
                if(agent.getAllieName() == null || agent.getAllieName().equals("")){
                    agent.setAgentUnRegisteredToTeam();
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private synchronized void agentLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        UsersManager usersManager = ServletUtils.getUserManager(getServletContext());

        Agent agent = agentsManager.getAgent(SessionUtils.getAgentName(request));
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie;
        try {
            allie = alliesManager.getAllie(agent.getAllieName());
        }catch (Exception e){
            allie = null;
        }

        if(allie != null) {
            allie.removeAgent(agent);
            agent.quitFromAllie();
        }

        response.setStatus(HttpServletResponse.SC_OK);
        String logoutMessage = "Agent " + agent.getAgentName() + " has logged out";
        String json = GSON_INSTANCE.toJson(logoutMessage);
        if(usersManager.isUserExists(agent.getAgentName())) {
            usersManager.removeUser(agent.getAgentName());
        }
        if(agentsManager.isAgentExists(agent.getAgentName())) {
            agentsManager.removeAgent(agent.getAgentName());
        }
        response.getWriter().println(json);
        response.getWriter().flush();
    }

    private synchronized void alliesLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String allyNameFromSession = SessionUtils.getAllieName(request);
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        UsersManager usersManager = ServletUtils.getUserManager(getServletContext());
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());

        if (allyNameFromSession != null) {

            Allie allie = alliesManager.getAllie(allyNameFromSession);
            if (allie.getBattleName() != null && !allie.getBattleName().equals("")) {
                String battleName = allie.getBattleName();
                String uBoatName = uBoatManager.getUBoatByBattleName(battleName);
                UBoat uBoat = uBoatManager.getUBoat(uBoatName);
                if (uBoat.isContestOnline()) {
                    allie.stopContest();
                }
                allie.setBattleName("");
                uBoat.getBattleField().removeAllyFromBattle(allie);
            }

            //allie.removeAllAgents();
            Gson gson = new Gson();
            String json = gson.toJson("Ally " + allie.getTeamName() + " has logged out");
            alliesManager.removeAlly(allyNameFromSession);
            usersManager.removeUser(allyNameFromSession);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
            response.getWriter().flush();
                //SessionUtils.clearSession(request);
        }
    }

    private synchronized void uBoatLogout(HttpServletRequest request, HttpServletResponse response) {
        String usernameFromSession = SessionUtils.getUsername(request);
        UsersManager userManager = ServletUtils.getUserManager(request.getServletContext());
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(request.getServletContext());
        if (usernameFromSession != null) {
            UBoat uBoat = uBoatManager.getUBoat(usernameFromSession);
            if(uBoat != null && uBoat.isContestOnline()) {
                uBoat.getBattleField().setWinner("Battle Host Left");
                uBoat.getBattleField().stopContest();
                uBoat.setContestOver();
            }

            if(uBoat != null) {
                removeBoatFromBoatsManager(request, usernameFromSession);
            }

            userManager.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);
        }
    }

    private void removeBoatFromBoatsManager(HttpServletRequest request, String usernameFromSession) {
        try {
            ServletUtils.getUBoatManager(request.getServletContext()).removeUBoat(usernameFromSession);
        }
        catch (Exception e) {
            //System.out.println("No boat to remove");
        }
    }
}
