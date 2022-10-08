package MachineOpsServlet;

import DTO.SectorsCodeAsJson;
import Engine.EngineManager;
import Engine.UBoatManager.UBoat;
import EnigmaMachine.Settings.Sector;
import EnigmaMachineException.*;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SetMachineConfigServlet",urlPatterns = {"/machine/SetMachineConfig"})
public class SetMachineConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            if(request.getParameter("action").toString().equals("set_machine_config_automatically")) {
                setMachineConfigAutomatically(request, response);
            }
            else if(request.getParameter("action").toString().equals("reset_machine_config")) {
                resetMachineConfig(request, response);
            }
            else {
                setMachineConfigManually(request, response);
            }
        } catch (ReflectorSettingsException e) {
            throw new RuntimeException(e);
        } catch (RotorsInUseSettingsException e) {
            throw new RuntimeException(e);
        } catch (SettingsFormatException e) {
            throw new RuntimeException(e);
        } catch (SettingsNotInitializedException e) {
            throw new RuntimeException(e);
        } catch (MachineNotExistsException e) {
            throw new RuntimeException(e);
        } catch (StartingPositionsOfTheRotorException e) {
            throw new RuntimeException(e);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (PluginBoardSettingsException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void resetMachineConfig(HttpServletRequest request, HttpServletResponse response) throws ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, SettingsNotInitializedException, MachineNotExistsException, CloneNotSupportedException, StartingPositionsOfTheRotorException, PluginBoardSettingsException, ServletException, IOException {
        UBoat uBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
        uBoat.setCurrentProcessedMessage("");
        uBoat.getBattleField().setProcessedMessage("");
        EngineManager engine = uBoat.getEngineManager();
        engine.resetSettings();
        response.setStatus(HttpServletResponse.SC_OK);
        //request.getRequestDispatcher(GET_MACHINE_CONFIG_SERVLET).include(request, response);
    }

    private void setMachineConfigAutomatically(HttpServletRequest request, HttpServletResponse response) throws ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, SettingsNotInitializedException, StartingPositionsOfTheRotorException, PluginBoardSettingsException, CloneNotSupportedException, MachineNotExistsException, IOException {

        synchronized (this) {
            EngineManager engine = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request)).getEngineManager();
            engine.setSettingsAutomatically();
            Gson gson = new Gson();

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(gson.toJson(engine.displaySpecifications().getCurrentMachineSettings()));
            response.getWriter().flush();
        }
    }

    private void setMachineConfigManually(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectorSettingsException, RotorsInUseSettingsException,Exception, SettingsFormatException, SettingsNotInitializedException, MachineNotExistsException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException {
        try {
            //Type type = new TypeToken<List<Sector>>() {}.getType();
            //System.out.println("TYpe: " + type);
            Gson gson = new Gson();
            EngineManager engine = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request)).getEngineManager();
            SectorsCodeAsJson sectorsAsJson =  gson.fromJson(request.getParameter("sectors"), SectorsCodeAsJson.class);
            System.out.println("sectors: " + sectorsAsJson);

            List<Sector> sectors = sectorsAsJson.getSectors();
            EngineManager engineManager = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request)).getEngineManager();
            engineManager.initializeSettings(sectors);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(gson.toJson(engine.displaySpecifications().getCurrentMachineSettings()));
            response.getWriter().flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }

    //private List<Sector> settingsToSectors(Sector[] settings) {
    //    List<Sector> sectors = new ArrayList<>();
    //    for (Sector sector : settings) {
    //        sectors.add(sector);
    //    }
    //    return sectors;
    //}

    //TODO chen: generate the json into list of sectors
    private List<Sector> getListOfSectorsFromJson(String sectors) {
        List<Sector> sectorsList = new ArrayList<>();
        String[] sectorsArray = sectors.split(",");


        //gson.fromJson(request.getParameter("action"), List.class);
        return null;
    }
}
