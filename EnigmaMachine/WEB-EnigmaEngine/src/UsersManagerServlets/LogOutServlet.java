package UsersManagerServlets;

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
