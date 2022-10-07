package AllyServlet;

import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "AllyOpsServlet",urlPatterns = {"/AllyOps"})
public class AllyOpsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        if(request.getParameter("action").equals("setTaskSize")) {
            setAllyTaskSize(request, response);
        }
    }

    synchronized private void setAllyTaskSize(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long taskSize = Long.parseLong(request.getParameter("taskSize"));
        AlliesManager  alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
        response.setStatus(HttpServletResponse.SC_OK);
        allie.setTaskSize(taskSize);
    }

    //create the response json string
}
