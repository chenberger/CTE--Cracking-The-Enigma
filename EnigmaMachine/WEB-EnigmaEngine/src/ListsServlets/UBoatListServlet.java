package ListsServlets;

import DTO.AlliesToTable;
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
import java.util.List;

@WebServlet(name = "UBoatListServlet", urlPatterns = {"/UBoatsList"})
public class UBoatListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        response.setContentType("application/json");
        try {
            if(request.getParameter("action")!= null && request.getParameter("action").equals("getUBoatName") ){
                response.getWriter().println(gson.toJson(SessionUtils.getUsername(request)));
            }else {
                if(ServletUtils.getUBoatsManager(getServletContext()).getUBoats().size() == 0) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                else{
                UBoat currentBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
                AlliesToTable alliesToTable = setAlliesToTable(currentBoat);
                String json = gson.toJson(alliesToTable);
                response.getWriter().println(json);
                response.getWriter();
                response.setStatus(HttpServletResponse.SC_OK);
                }
            }
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
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
