package UsersManagerServlets;

import Constants.ServletConstants;
import Engine.AlliesManager.AlliesManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

import static Constants.ServletConstants.ALLIE_NAME;

@WebServlet(name = "AllieLoginServlet", urlPatterns = {"/allies/login"})
public class AllieLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String allieNameFromSession = SessionUtils.getAllieName(request);
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        if (allieNameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(ALLIE_NAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //response.sendRedirect(SIGN_UP_URL);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (alliesManager.isAllieExists(usernameFromParameter)) {
                        String errorMessage = "Allie Name " + usernameFromParameter + " already exists. Please enter a different username.";

                        request.setAttribute(ServletConstants.ALLIE_NAME_ERROR, errorMessage);
                        response.getWriter().println(errorMessage);

                        //getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    }
                    else {

                        alliesManager.addAllie(usernameFromParameter);
                        request.getSession(true).setAttribute(ALLIE_NAME, usernameFromParameter);

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
