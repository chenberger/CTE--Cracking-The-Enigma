package MachineOpsServlet;

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
import java.util.List;

@WebServlet(name = "SetMachineConfigServlet",urlPatterns = {"/machine/SetMachineConfig"})
public class SetMachineConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            if(request.getParameter("action").toString().equals("set_machine_config_automatically")){
                setMachineConfigAutomatically(request,response);
            }else {
                processRequest(request, response);
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

    private void setMachineConfigAutomatically(HttpServletRequest request, HttpServletResponse response) throws ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, SettingsNotInitializedException, StartingPositionsOfTheRotorException, PluginBoardSettingsException, CloneNotSupportedException, MachineNotExistsException, IOException {


        EngineManager engine = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request)).getEngineManager();
        engine.setSettingsAutomatically();
        Gson gson = new Gson();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(gson.toJson(engine.displaySpecifications().getCurrentMachineSettings()));
        response.getWriter().flush();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectorSettingsException, RotorsInUseSettingsException,Exception, SettingsFormatException, SettingsNotInitializedException, MachineNotExistsException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException {
        try {
            Gson gson = new Gson();
            EngineManager engine = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request)).getEngineManager();
            List<Sector> sectors = gson.fromJson(request.getParameter("action"), List.class);
            EngineManager engineManager = ServletUtils.getUBoatsManager(getServletContext()).getUBoat(SessionUtils.getUsername(request)).getEngineManager();
            engineManager.initializeSettings(sectors);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(gson.toJson(engine.displaySpecifications().getCurrentMachineSettings()));
            response.getWriter().flush();

        } catch (SettingsNotInitializedException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}
