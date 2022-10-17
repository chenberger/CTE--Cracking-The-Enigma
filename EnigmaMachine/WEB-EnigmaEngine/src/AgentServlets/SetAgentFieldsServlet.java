package AgentServlets;

import Engine.AgentsManager.Agent;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static Utils.Constants.GSON_INSTANCE;
import static java.lang.System.out;

@WebServlet(name = "SetAgentFieldsServlet", urlPatterns = {"/agent/SetAgentFields"})
public class SetAgentFieldsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        processRequest(request, response);
    }
    private synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            Agent agent = ServletUtils.getAgentsManager(getServletContext()).getAgent(SessionUtils.getAgentName(request));
            agent.setTasksPullingInterval(Long.parseLong(request.getParameter("tasksPullingInterval")));
            agent.setNumberOfWorkingThreads(Long.parseLong(request.getParameter("numberOfWorkingThreads")));
            response.setStatus(HttpServletResponse.SC_OK);

            String jsonResponse = GSON_INSTANCE.toJson(agent.getAgentFields());
            out.println(jsonResponse);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
        }
    }
}

