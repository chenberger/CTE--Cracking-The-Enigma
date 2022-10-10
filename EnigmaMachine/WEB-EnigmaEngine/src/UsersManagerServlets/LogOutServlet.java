package UsersManagerServlets;

import Engine.AlliesManager.AlliesManager;
import UserManager.UsersManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.application.Platform;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "LogOutServlet", urlPatterns = {"/users/logout"})
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter("action").equals("uBoatLogout")) {
            uBoatLogout(request, response);
        }
        else if(request.getParameter("action").equals("alliesLogout")) {
            alliesLogout(request, response);
        }


    }

    private void alliesLogout(HttpServletRequest request, HttpServletResponse response) {
        String allyNameFromSession = SessionUtils.getAllieName(request);
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        UsersManager usersManager = ServletUtils.getUserManager(getServletContext());
        if(allyNameFromSession != null) {
            alliesManager.removeAlly(allyNameFromSession);
            usersManager.removeUser(allyNameFromSession);
            SessionUtils.clearSession(request);
        }
    }

    private void uBoatLogout(HttpServletRequest request, HttpServletResponse response) {
        String usernameFromSession = SessionUtils.getUsername(request);
        UsersManager userManager = ServletUtils.getUserManager(request.getServletContext());

        if (usernameFromSession != null) {
            removeBoatFromBoatsManager(request, usernameFromSession);
            System.out.println("Clearing session for " + usernameFromSession);
            userManager.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);
            System.out.println("Users Registered:" + userManager.getUsers());

        }
    }

    private void removeBoatFromBoatsManager(HttpServletRequest request, String usernameFromSession) {
        try {
            ServletUtils.getUBoatManager(request.getServletContext()).removeUBoat(usernameFromSession);
        }
        catch (Exception e) {
            //System.out.println("No boat to remove");
        }
    }
}
