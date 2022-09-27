package UsersManagerServlets;

import Constants.ServletConstants;
import Engine.AlliesManager.AlliesManager;
import Engine.UBoatManager.UBoatManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

import static Constants.ServletConstants.ALLIE_NAME;
import static Constants.ServletConstants.U_BOAT_NAME;

@WebServlet(name = "UBoatLoginServlet", urlPatterns = "/u-boat/login")
public class UBoatLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String UBoatNameFromSession = SessionUtils.getUsername(request);
        //UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        if (UBoatNameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(U_BOAT_NAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //response.sendRedirect(SIGN_UP_URL);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (ServletUtils.getUserManager(getServletContext()).isUserExists(usernameFromParameter)) {
                        String errorMessage = "The U Boat with the Name " + usernameFromParameter + " already exists. Please enter a different username.";

                        request.setAttribute(ServletConstants.U_BOAT_NAME_ERROR, errorMessage);
                        response.getWriter().println(errorMessage);

                        //getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    }
                    else {

                        //uBoatManager.addUBoat(usernameFromParameter);

                        request.getSession(true).setAttribute(U_BOAT_NAME, usernameFromParameter);
                        RequestDispatcher rd = request.getRequestDispatcher("/fileUploaded");
                        rd.include(request, response);
                        //System.out.println("On login, request URI is: " + request.getRequestURI());
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
