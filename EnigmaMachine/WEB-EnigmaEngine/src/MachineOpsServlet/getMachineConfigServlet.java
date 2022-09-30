package MachineOpsServlet;

import DTO.MachineDetails;
import Engine.EngineManager;
import Engine.UBoatManager.UBoat;
import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.MachineNotExistsException;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SetMachineConfigServlet",urlPatterns = {"/machine/GetMachineConfig"})
public class getMachineConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            processRequest(request, response);
        } catch (MachineNotExistsException e) {
            throw new RuntimeException(e);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, MachineNotExistsException, CloneNotSupportedException {
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
        EngineManager engine = uBoat.getEngineManager();
        if(request.getParameter("action").equals("displaySpecifications")){
            MachineDetails machineDetails = engine.displaySpecifications();
            response.setStatus(HttpServletResponse.SC_OK);
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(machineDetails);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        }
    }
}
