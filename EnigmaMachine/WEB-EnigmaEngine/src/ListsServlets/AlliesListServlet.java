package ListsServlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "AlliesListServlet", urlPatterns = {"/AlliesList"})
public class AlliesListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            //TODO: get allies list relevant for specific boat cause we would like to update the allies in competition table
            Set<String> UBoatsList = ServletUtils.getUBoatsManager(getServletContext()).getUBoats();
            String json = gson.toJson(alliesList);
            out.println(json);
            out.flush();
        }
    }
}
