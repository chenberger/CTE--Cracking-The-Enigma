package UsersManagerServlets;

import UserManager.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getUserNameServlet", urlPatterns = {"/users/getUserName"})
public class getUserNameServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("action").equals("getBoatName")) {
            getUBoatName(request, response);
        }
        else if(request.getParameter("action").equals("getAllyName")) {
            getAllyName(request, response);
        }
        else {
            getAgentsName(request, response);
        }

    }

    private void getAgentsName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println(SessionUtils.getAgentName(request));
    }

    private void getAllyName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println(SessionUtils.getAllieName(request));
    }

    private void getUBoatName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println(SessionUtils.getUsername(request));
    }
}

