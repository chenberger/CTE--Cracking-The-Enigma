package UsersManagerServlets;

import Constants.ServletConstants;
import Engine.AgentsManager.AgentsManager;
import UserManager.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

import static Constants.ServletConstants.AGENT_NAME;

@WebServlet(name = "AgentLoginServlet", urlPatterns = "/agents/Login")
public class AgentLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String agentNameFromSession = SessionUtils.getAgentName(request);
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        UsersManager usersManager = ServletUtils.getUserManager(getServletContext());
        if (agentNameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(AGENT_NAME);
            //String allieTeamName = request.getParameter("AllieTeamName");
            //int numberOfWorkingThreads = Integer.parseInt(request.getParameter("NumberOfWorkingThreads"));
            //long numberOfClonedTasks = Long.parseLong(request.getParameter("NumberOfClonedTasks"));

            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //response.sendRedirect(SIGN_UP_URL);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (agentsManager.isAgentExists(usernameFromParameter) || usersManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username Name " + usernameFromParameter + " already exists. Please enter a different username.";

                        request.setAttribute(ServletConstants.AGENT_NAME_ERROR, errorMessage);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().println(errorMessage);

                        //getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    }
                    else {

                        //alliesManager.addAllie(usernameFromParameter);
                        agentsManager.addAgent(usernameFromParameter);
                        usersManager.addUser(usernameFromParameter);
                        request.getSession(true).setAttribute(AGENT_NAME, usernameFromParameter);

                        //System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            response.getWriter().println("Agent already exist");
            response.setStatus(HttpServletResponse.SC_OK);
            //user is already logged in
        }
    }

}
