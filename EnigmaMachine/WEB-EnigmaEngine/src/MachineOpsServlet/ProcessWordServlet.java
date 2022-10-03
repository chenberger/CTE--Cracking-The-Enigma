package MachineOpsServlet;

import Engine.EngineManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "ProcessWordServlet", urlPatterns = {"/machine/ProcessWord"})
public class ProcessWordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String wordToProcess = request.getParameter("wordToProcess");
            EngineManager engineManager = ServletUtils.getUBoatsManager(getServletContext())
                    .getUBoat(SessionUtils.getUsername(request)).getEngineManager();
            String processedWord = engineManager.processInputsFromDictionary(wordToProcess);

            //String processedWordWithSpecialChars = getProcessedWordWithSpecialChars(processedWord);
            String json = GSON_INSTANCE.toJson(processedWord);
            out.println(json);
            out.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}