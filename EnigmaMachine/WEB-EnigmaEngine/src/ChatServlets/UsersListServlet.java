package ChatServlets;

import UserManager.UsersManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "ChatUsersListServlet", value = "/Chat/UsersListServlet")
public class UsersListServlet extends HttpServlet {

    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UsersManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<String> usersList = userManager.getUsers();
            //List<String> userListAsStringArray = new ArrayList<>(usersList);
            String json = gson.toJson(usersList);
            out.println(json);
            out.flush();

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
