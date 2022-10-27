package RegistrationServlets;

import Engine.AgentsManager.Agent;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.Allie;
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

@WebServlet(name = "RegisterToAllieServlet", urlPatterns = {"/agent/RegisterToAllie"})
public class RegisterToAllieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        processRequest(request, response);
    }

    private synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //int numberOfThreads = request.getParameter(NUMBER_OF_THREADS) != null ? Integer.parseInt(request.getParameter(NUMBER_OF_THREADS)) : 0;
        //long numberOfPulledTasks = request.getParameter(NUMBER_OF_PULLED_TASKS) != null ? Long.parseLong(request.getParameter(NUMBER_OF_PULLED_TASKS)) : 0;
        String agentName = SessionUtils.getAgentName(request);

        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        Agent agent = agentsManager.getAgent(agentName);
        agent.setNumberOfWorkingThreads(request.getParameter(NUMBER_OF_THREADS) != null ? Integer.parseInt(request.getParameter(NUMBER_OF_THREADS)) : 0);
        agent.setTasksPullingInterval(request.getParameter("tasksPulledEachTime") != null ? Long.parseLong(request.getParameter("tasksPulledEachTime")) : 0);
        agent.setAllieName(request.getParameter(ALLIE_NAME));
        Allie currentAllie = ServletUtils.getAlliesManager(getServletContext()).getAllie(request.getParameter("chosenTeam"));

        if(agentName == null || agentName.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            currentAllie.addAgent(agent);
            //create the response json string
            agent.setAllieName(currentAllie.getTeamName());
            agent.setIsAgentRegisteredToTeam();
            Gson gson = new Gson();
            List<String> agentFields = agent.getAgentFields();
            String jsonResponse = gson.toJson(agentFields);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(jsonResponse);
            response.getWriter().flush();

        }
    }
}
