package MachineOpsServlet;

import EnigmaMachine.Settings.Sector;
import EnigmaMachineException.*;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SetMachineConfigServlet",urlPatterns = {"/machine/SetMachineConfig"})
public class SetMachineConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest(request, response);
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
        }

    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectorSettingsException, RotorsInUseSettingsException, SettingsFormatException, SettingsNotInitializedException, MachineNotExistsException, StartingPositionsOfTheRotorException, CloneNotSupportedException, PluginBoardSettingsException {
        try {
            Gson gson = new Gson();
            List<Sector> sectors = gson.fromJson(request.getParameter("sectors"), List.class);
            ServletUtils.getUBoatManager(getServletContext()).getUBoat(request.getParameter("username")).getEngineManager().initializeSettings(sectors);
        } catch (SettingsNotInitializedException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}
