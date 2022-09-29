package ListsServlets;

import Engine.UBoatManager.UBoat;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UBoatListServlet", urlPatterns = {"/UBoatList"})
public class UBoatListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            if(request.getParameter("action").equals("getUBoatName") ){
                out.println(SessionUtils.getUsername(request));
            }else {
                Gson gson = new Gson();
                List<UBoat> UBoatsList = ServletUtils.getUBoatsManager(getServletContext()).getUBoatsAsList();
                String json = gson.toJson(UBoatsList);
                out.println(json);
                out.flush();
            }
        }
    }
}
