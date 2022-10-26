package CompetitionServlets;

import Engine.AgentsManager.Agent;
import Engine.AlliesManager.Allie;
import Engine.UBoatManager.UBoat;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "CompetitionServlet", value = "/CompetitionServlet/ReadyManager")
public class ReadyManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            if(request.getParameter("type").equals("uBoat")){
                handleUBoatReady(request, response);
            } else if (request.getParameter("type").equals("Allies")) {
                handleAlliesReady(request, response);
            }
            else{//type is Agent that wants to check the contest status
                checkContestStatus(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void checkContestStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Agent agent = ServletUtils.getAgentsManager(getServletContext()).getAgent(SessionUtils.getAgentName(request));
        Allie allie = ServletUtils.getAlliesManager(getServletContext()).getAllie(agent.getAllieName());
        String uBoatName = ServletUtils.getUBoatManager(getServletContext()).getUBoatByBattleName(allie.getBattleName());
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(uBoatName);

        if(uBoat.isContestOnline()){
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(GSON_INSTANCE.toJson(true));
            response.getWriter().flush();
        }
        else{
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(GSON_INSTANCE.toJson(false));
            response.getWriter().flush();
        }
    }

    synchronized private void handleAlliesReady(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            Allie allie = ServletUtils.getAlliesManager(getServletContext()).getAllie(SessionUtils.getAllieName(request));
            String uBoatName = ServletUtils.getUBoatsManager(getServletContext()).getUBoatByBattleName(allie.getBattleName());
            UBoat uBoat = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(uBoatName);
            uBoat.getBattleField().addTeamToReady();
            allie.setAgentsParticipatingInDecryption();
            if((uBoat.getBattleField().getNumberOfAlliesThatAreReady() == uBoat.getBattleField().getNumberOfAlliesToStartBattle()) && uBoat.isReady()){
                uBoat.getBattleField().clearCandidatesInformationList();
                uBoat.getBattleField().getAlliesInBattle().forEach(Allie::clearAgentsProcessData);
                uBoat.setContestStatusStarted();
                uBoat.startContest();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Battle Started");
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Battle Not Started");
        }

    }

    synchronized private void handleUBoatReady(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UBoat uBoat = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
            uBoat.setReady(true);
            if(uBoat.getBattleField().getNumberOfAlliesThatAreReady() == uBoat.getBattleField().getNumberOfAlliesToStartBattle()){
                uBoat.getBattleField().clearCandidatesInformationList();
                uBoat.getBattleField().getAlliesInBattle().forEach(Allie::clearAgentsProcessData);
                uBoat.setContestStatusStarted();
                uBoat.startContest();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Battle Started");
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Battle Not Started");
        }
    }
}
