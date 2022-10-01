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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UBoatListServlet", urlPatterns = {"/UBoatsList"})
public class UBoatListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        response.setContentType("application/json");
        try {
            if(request.getParameter("action")!= null && request.getParameter("action").equals("getUBoatName") ){
                response.getWriter().println(gson.toJson(SessionUtils.getUsername(request)));
            }else {

                List<UBoat> UBoatsList = ServletUtils.getUBoatsManager(getServletContext()).getUBoatsAsList();
                String json = gson.toJson(UBoatsList);
                response.getWriter().println(json);
                response.getWriter();
            }
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
