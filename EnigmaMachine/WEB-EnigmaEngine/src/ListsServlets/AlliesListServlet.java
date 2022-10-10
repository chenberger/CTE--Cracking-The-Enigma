package ListsServlets;

import DTO.AgentsToTable;
import DTO.AlliesToTable;
import DTO.TeamNameColumn;
import Engine.AgentsManager.Agent;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
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

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "AlliesListServlet", urlPatterns = {"/AlliesList"})
public class AlliesListServlet extends HttpServlet {
    private final Object alliesListLock = new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        synchronized (alliesListLock){
            Gson gson = new Gson();
            response.setContentType("application/json");
            try {
                if(request.getParameter("action")!= null && request.getParameter("action").equals("getAllyName") ){
                    response.getWriter().println(gson.toJson(SessionUtils.getUsername(request)));
                }else if(request.getParameter("action") != null && request.getParameter("action").equals("getAlliesList")){
                    getAlliesList(request, response);
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
    }
    private void getAlliesList(HttpServletRequest request, HttpServletResponse response){
        synchronized (alliesListLock) {
            try {
                response.setContentType("application/json");
                AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
                List<String> allAllies = new ArrayList<>(alliesManager.getAllies().keySet());
                if (allAllies.size() > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(GSON_INSTANCE.toJson(allAllies));
                    response.getWriter().flush();

                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }

    private List<TeamNameColumn> getTeamNameColumns(AlliesManager alliesManager) {
        List<TeamNameColumn> allAllies = new ArrayList<>();
        for (String allyName : alliesManager.getAllies().keySet()) {
            allAllies.add(new TeamNameColumn(allyName));
        }
        return allAllies;
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

