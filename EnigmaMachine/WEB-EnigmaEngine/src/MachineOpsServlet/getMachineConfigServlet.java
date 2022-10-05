package MachineOpsServlet;

import DTO.MachineConfigurationToShow;
import DTO.MachineDetails;
import Engine.EngineManager;
import Engine.UBoatManager.UBoat;
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

@WebServlet(name = "GetMachineConfigServlet",urlPatterns = {"/machine/GetMachineConfig"})
public class getMachineConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            if(request.getParameter("action").toString().equals("displaySpecifications")) {
                getMachineSpecifications(request, response);
            }
            else if(request.getParameter("action").toString().equals("displayRawMachineDetails")) {
                getRawMachineDetails(request, response);
            }
            else if(request.getParameter("action").toString().equals("getCurrentMachineConfig")) {
                getCurrentMachineConfig(request, response);
            }
            else{
                getOriginalMachineConfig(request, response);
            }

        } catch (MachineNotExistsException e) {
            throw new RuntimeException(e);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }



    private void getRawMachineDetails(HttpServletRequest request, HttpServletResponse response) throws MachineNotExistsException, CloneNotSupportedException {
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
        EngineManager engine = uBoat.getEngineManager();
        MachineDetails machineDetails = engine.displaySpecifications();
        //machineDetails = new MachineDetails(engine.getEnigmaMachine(), 0, machineDetails.getOriginalSettingsFormat(),machineDetails.getOriginalSettingsFormat());
        response.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(machineDetails);
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getMachineSpecifications(HttpServletRequest request, HttpServletResponse response) throws MachineNotExistsException, CloneNotSupportedException, IOException {
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
        EngineManager engine = uBoat.getEngineManager();
        MachineDetails machineDetails = engine.displaySpecifications();
        MachineConfigurationToShow machineConfigurationToShow = new MachineConfigurationToShow(machineDetails.getAmountOfTotalRotors(), machineDetails.getAmountOfTotalReflectors(), machineDetails.getAmountCurrentRotorsInUse(), machineDetails.getMessagesCounter(), machineDetails.getCurrentMachineSettings(), machineDetails.getOriginalMachineSettings());
        response.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(machineConfigurationToShow);
        try {
            response.getWriter().print(jsonResponse);
            response.getWriter().flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void getCurrentMachineConfig(HttpServletRequest request, HttpServletResponse response) throws IOException, MachineNotExistsException, CloneNotSupportedException {
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
        EngineManager engine = uBoat.getEngineManager();
        MachineDetails machineDetails = engine.displaySpecifications();
        response.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        //TODO: return it to get machine config
        String jsonResponse = gson.toJson(machineDetails.getCurrentMachineSettings());
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void getOriginalMachineConfig(HttpServletRequest request, HttpServletResponse response) throws MachineNotExistsException, CloneNotSupportedException {
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
        EngineManager engine = uBoat.getEngineManager();
        MachineDetails machineDetails = engine.displaySpecifications();
        response.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        String originalMachineConfig = machineDetails.getOriginalMachineSettings();
        String jsonResponse = gson.toJson(originalMachineConfig);
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
