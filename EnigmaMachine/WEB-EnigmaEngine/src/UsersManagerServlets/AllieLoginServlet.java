package UsersManagerServlets;

import Constants.ServletConstants;
import DTO.TeamNameColumn;
import Engine.AlliesManager.AlliesManager;
import UserManager.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Constants.ServletConstants.ALLIE_NAME;
import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "AllieLoginServlet", urlPatterns = {"/allies/login"})
public class AllieLoginServlet extends HttpServlet {
    private static final Object alliesListLock = new Object();
    private static final Object alliesManagerLock = new Object();
    private static final Object processRequestLock = new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        synchronized (alliesManagerLock) {
            processRequest(request, response);
        }
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        synchronized (processRequestLock) {
            response.setContentType("text/html;charset=UTF-8");
            String allieNameFromSession = SessionUtils.getAllieName(request);
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            UsersManager usersManager = ServletUtils.getUserManager(getServletContext());
            if (allieNameFromSession == null) {
                //user is not logged in yet
                String usernameFromParameter = request.getParameter(ALLIE_NAME);
                if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    //normalize the username value
                    usernameFromParameter = usernameFromParameter.trim();

                    synchronized (this) {
                        if (alliesManager.isAllieExists(usernameFromParameter) || usersManager.isUserExists(usernameFromParameter)) {
                            String errorMessage = "Username Name " + usernameFromParameter + " already exists. Please enter a different username.";

                            request.setAttribute(ServletConstants.ALLIE_NAME_ERROR, errorMessage);
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().println(errorMessage);


                        } else {

                            alliesManager.addAllie(usernameFromParameter);
                            usersManager.addUser(usernameFromParameter);
                            request.getSession(true).setAttribute(ALLIE_NAME, usernameFromParameter);

                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                    }
                }
            } else {
                //user is already logged in
                response.getWriter().println("Allie already exist");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
