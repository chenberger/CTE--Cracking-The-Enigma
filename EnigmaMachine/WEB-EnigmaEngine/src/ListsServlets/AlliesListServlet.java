package ListsServlets;

import DTO.AgentsToTable;
import DTO.AlliesToTable;
import Engine.AgentsManager.Agent;
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

@WebServlet(name = "AlliesListServlet", urlPatterns = {"/AlliesList"})
public class AlliesListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        response.setContentType("application/json");
        try {
            if(request.getParameter("action")!= null && request.getParameter("action").equals("getAllyName") ){
                response.getWriter().println(gson.toJson(SessionUtils.getUsername(request)));
            }else {
                if(ServletUtils.getAlliesManager(getServletContext()).getAllies().size() == 0) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                else{
                    Allie currentAllie = ServletUtils.getAlliesManager(getServletContext()).getAllie(SessionUtils.getAllieName(request));
                    AgentsToTable agentsToTable = setAgentsToTable(currentAllie);
                    String json = gson.toJson(agentsToTable);
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

    private AgentsToTable setAgentsToTable(Allie currentAllie) {
        List<String> agentsNames = new ArrayList<>();
        for(String agentName : currentAllie.getAgentsNames()){
            agentsNames.add(agentName);
        }

        List<Long> numberOfThreadsForEachAgent = new ArrayList<>();
        for(Agent agent : currentAllie.getAgents()){
            numberOfThreadsForEachAgent.add(agent.getNumberOfWorkingThreads());
        }

        List<Long> tasksSizesForEachAgent = new ArrayList<>();
        for(Agent agent : currentAllie.getAgents()){
            tasksSizesForEachAgent.add(agent.getTasksPullingInterval());
        }
        return new AgentsToTable(agentsNames, numberOfThreadsForEachAgent, tasksSizesForEachAgent);
    }
}

