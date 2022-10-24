package ListsServlets;

import BruteForce.DifficultyLevel;
import DTO.AlliesToTable;
import DTO.OnLineContestsDataToTable;
import Engine.AlliesManager.Allie;
import Engine.UBoatManager.UBoat;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "UBoatListServlet", urlPatterns = {"/UBoatsList"})
public class UBoatListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        response.setContentType("application/json");
        try {
            if(request.getParameter("action")!= null && request.getParameter("action").equals("getCurrentOnlineContests") ) {
                getCurrentOnlineContests(request, response);
            }
            else if(request.getParameter("action")!= null && request.getParameter("action").equals("getUBoatName") ){
                response.getWriter().println(gson.toJson(SessionUtils.getUsername(request)));
            }else if(request.getParameter("action")!= null && request.getParameter("action").equals("getTeamsInBattle") ){
                getAllAlliesInBattle(request, response);
            }
            else {
                getCurrentRegisteredAllies(request, response);
            }

        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getAllAlliesInBattle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Allie allie = ServletUtils.getAlliesManager(getServletContext()).getAllies().get(SessionUtils.getAllieName(request));
        String currentUBoatName = ServletUtils.getUBoatsManager(getServletContext()).getUBoatByBattleName(allie.getBattleName());
        UBoat uBoat = ServletUtils.getUBoatsManager(getServletContext()).getUBoats().get(currentUBoatName);
        if(uBoat != null){
            AlliesToTable alliesToTable = setAlliesToTable(uBoat);
            try {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(GSON_INSTANCE.toJson(alliesToTable));
                response.getWriter().flush();
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Error: " + e.getMessage());
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    private void getCurrentRegisteredAllies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(ServletUtils.getUBoatsManager(getServletContext()).getUBoats().size() == 0) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        else{
            UBoat currentBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
            AlliesToTable alliesToTable = setAlliesToTable(currentBoat);
            String json = GSON_INSTANCE.toJson(alliesToTable);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
            response.getWriter();

        }
    }

    private void getCurrentOnlineContests(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Allie currentAllie = ServletUtils.getAlliesManager(getServletContext()).getAllie(SessionUtils.getAllieName(request));
        OnLineContestsDataToTable onLineContestsDataToTable = setOnLineContestsDataToTable();
        String json = GSON_INSTANCE.toJson(onLineContestsDataToTable);
        if(ServletUtils.getUBoatsManager(getServletContext()).getUBoats().size() == 0) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        else{
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
            response.getWriter();
        }

    }

    private OnLineContestsDataToTable setOnLineContestsDataToTable() {
        List<String> uBoatsNames = new ArrayList<>();
        List<String> battlesNames = new ArrayList<>();
        List<DifficultyLevel> difficultyLevels = new ArrayList<>();
        Map<String, String> contestsStatus = new HashMap<>();
        Map<String, Integer> numberOfTeamsRegisteredToEachContest = new HashMap<>();
        List<Integer> numberOfTeamsNeededToEachContest = new ArrayList<>();

        List<UBoat> uBoats = ServletUtils.getUBoatsManager(getServletContext()).getUBoatsAsList();
        for (UBoat uBoat : uBoats) {
            uBoatsNames.add(uBoat.getName());
            battlesNames.add(uBoat.getBattleField().getBattleFieldName());
            difficultyLevels.add(uBoat.getBattleField().getLevel());
            contestsStatus.put(uBoat.getName(), uBoat.getContestStatus());
            numberOfTeamsRegisteredToEachContest.put(uBoat.getBattleField().getBattleFieldName(), uBoat.getBattleField().getNumberOfTeamsInBattleField());
            numberOfTeamsNeededToEachContest.add(uBoat.getBattleField().getNumberOfAlliesToStartBattle());
        }

        return new OnLineContestsDataToTable(battlesNames, uBoatsNames, difficultyLevels, contestsStatus, numberOfTeamsRegisteredToEachContest, numberOfTeamsNeededToEachContest);
    }

    private AlliesToTable setAlliesToTable(UBoat currentBoat) {
        List<Integer> numberOfAgentsForEachAllies = new ArrayList<>();
        for(Allie allie : currentBoat.getBattleField().getAlliesInBattle()){
            numberOfAgentsForEachAllies.add(allie.getAgents().size());
        }
        List<Long> tasksSizesForEachAllies = new ArrayList<>();
        for(Allie allie : currentBoat.getBattleField().getAlliesInBattle()){
            tasksSizesForEachAllies.add(allie.getTaskSize());
        }
        return new AlliesToTable(currentBoat.getTeams(),
                numberOfAgentsForEachAllies, tasksSizesForEachAllies, currentBoat.getName());
    }
}
