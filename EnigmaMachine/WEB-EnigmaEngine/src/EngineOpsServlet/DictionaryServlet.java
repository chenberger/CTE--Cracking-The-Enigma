package EngineOpsServlet;

import Engine.Dictionary;
import Engine.EngineManager;
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
import java.util.Set;

@WebServlet(name = "DictionaryServlet", value = "/Engine/GetDictionary")
public class DictionaryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            if(request.getParameter("action").equals("getDictionaryObject")) {
                getDictionaryObject(request, response);
            }
            else {
                getDictionaryAsSet(request, response);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getDictionaryObject(HttpServletRequest request, HttpServletResponse response) {
        try {
            UBoat uBoat = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
            EngineManager engineManager = uBoat.getEngineManager();
            Dictionary dictionary = engineManager.getDictionaryObject();
            Gson gson = new Gson();
            String json = gson.toJson(dictionary);
            response.getWriter().println(json);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getDictionaryAsSet(HttpServletRequest request, HttpServletResponse response) {
        try {
            UBoat uBoat = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
            EngineManager engineManager = uBoat.getEngineManager();
            Set<String> dictionary = engineManager.getDictionary();
            Gson gson = new Gson();
            String json = gson.toJson(dictionary);
            response.getWriter().println(json);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
