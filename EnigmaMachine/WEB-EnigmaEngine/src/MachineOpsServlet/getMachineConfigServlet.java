package MachineOpsServlet;

import DTO.*;
import Engine.AgentsManager.Agent;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import Engine.EngineManager;
import Engine.UBoatManager.UBoat;
import Engine.UBoatManager.UBoatManager;
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

@WebServlet(name = "GetMachineConfigServlet",urlPatterns = {"/machine/GetMachineConfig"})
public class getMachineConfigServlet extends HttpServlet {
    private final Object lock = new Object();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            if(request.getParameter("action")!= null && request.getParameter("action").equals("getMachineDataForInitialize") ) {
                getMachineDataForInit(request, response);
            }
            else if(request.getParameter("action").toString().equals("displaySpecifications")) {
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

    private void getMachineDataForInit(HttpServletRequest request, HttpServletResponse response) throws MachineNotExistsException, CloneNotSupportedException, IOException {
        synchronized (lock) {
            Gson gson = new Gson();
            UBoatManager uBoatManager = ServletUtils.getUBoatsManager(getServletContext());
            AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
            Agent agent = ServletUtils.getAgentsManager(getServletContext()).getAgent(request.getParameter("agentName"));
            String allieName = agent.getAllieName();
            Allie allie = alliesManager.getAllie(allieName);

            String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
            UBoat uBoat = uBoatManager.getUBoat(uBoatName);
            try {

                EngineManager engineManager = uBoat.getEngineManager();
                EnigmaMachine enigmaMachine = engineManager.getCurrentEnigmaMachine();
                DataToInitializeMachine dataToInitializeMachine = new DataToInitializeMachine(enigmaMachine.cloneRotors(), enigmaMachine.getCurrentRotorsInUse(), enigmaMachine.cloneReflectors(), enigmaMachine.getCurrentReflectorInUse(), enigmaMachine.cloneKeyboard());
                String json = gson.toJson(dataToInitializeMachine);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(json);
                response.getWriter().flush();

            } catch (IOException | RuntimeException | MachineNotExistsException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
            }
        }

    }


    synchronized private void getRawMachineDetails(HttpServletRequest request, HttpServletResponse response) throws MachineNotExistsException, CloneNotSupportedException {
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
        EngineManager engine = uBoat.getEngineManager();
        MachineDetails machineDetails = engine.displaySpecifications();
        DetailsToManualCodeInitializer detailsToManualCodeInitializer = new DetailsToManualCodeInitializer(machineDetails.getAllRotorsId(),machineDetails.getAllReflectorsId(),machineDetails.getKeyboardCharacters(),machineDetails.getAmountCurrentRotorsInUse());

        response.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(detailsToManualCodeInitializer);
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    synchronized private void getMachineSpecifications(HttpServletRequest request, HttpServletResponse response) throws MachineNotExistsException, CloneNotSupportedException, IOException {
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

    synchronized private void getCurrentMachineConfig(HttpServletRequest request, HttpServletResponse response) throws IOException, MachineNotExistsException, CloneNotSupportedException {
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
    synchronized private void getOriginalMachineConfig(HttpServletRequest request, HttpServletResponse response) throws MachineNotExistsException, CloneNotSupportedException {
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
