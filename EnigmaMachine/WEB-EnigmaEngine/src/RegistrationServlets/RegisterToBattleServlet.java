package RegistrationServlets;

import DTO.OnLineContestsTable;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import Engine.BattleField;
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
import java.util.List;

import static Constants.ServletConstants.*;

@WebServlet(name = "RegisterToBattleServlet", value = "/allie/RegisterToBattle")
public class RegisterToBattleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        long taskSize = request.getParameter(TASK_SIZE) != null ? Long.parseLong(request.getParameter(TASK_SIZE)) : 0;
        int numberOfAgents = request.getParameter(NUMBER_OF_AGENTS) != null ? Integer.parseInt(request.getParameter(NUMBER_OF_AGENTS)) : 0;
        String allieNameFromSession = SessionUtils.getAllieName(request);
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie = alliesManager.getAllie(allieNameFromSession);
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoat currentBoat = uBoatManager.getUBoat(request.getParameter(USER_NAME));
        BattleField battleField = currentBoat.getBattleField();

        if (allieNameFromSession == null || allieNameFromSession.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            if(battleField.getNumberOfTeamsInBattleField() == battleField.getNumberOfAlliesToStartBattle()){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Battle is full");
            }
            else {
                battleField.addTeam(allie);
                //create the response json string
                allie.setBattleName(battleField.getBattleFieldName());
                Gson gson = new Gson();
                String teamsRegisteredAndNeeded = battleField.getNumberOfTeamsInBattleField() + "/" + battleField.getNumberOfAlliesToStartBattle();
                OnLineContestsTable onLineContestsTable = new OnLineContestsTable(battleField.getBattleFieldName(), currentBoat.getName(), currentBoat.getContestStatus(),
                        battleField.getLevel().name(),teamsRegisteredAndNeeded);
                //List<String> allieFields = allie.getAllieFields();
                String jsonResponse = gson.toJson(onLineContestsTable);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(jsonResponse);
                response.getWriter().flush();
            }
        }

    }

    private void registerToBattleByItsName(String battleName, Allie allie) {

    }
}
