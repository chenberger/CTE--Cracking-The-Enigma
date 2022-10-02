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

@WebServlet(name = "ProcessWordServlet", urlPatterns = {"/machine/ProcessWord"})
public class ProcessWordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String wordToProcess = request.getParameter("wordToProcess");
            EngineManager engineManager = ServletUtils.getUBoatsManager(getServletContext())
                    .getUBoat(SessionUtils.getUsername(request)).getEngineManager();
            String processedWord = engineManager.processInputsFromDictionary(wordToProcess);
            Gson gson = new Gson();
            String processedWordWithSpecialChars = getProcessedWordWithSpecialChars(processedWord);
            String json = gson.toJson(processedWordWithSpecialChars);
            out.println(json);
            out.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getProcessedWordWithSpecialChars(String processedWord) {
        StringBuilder processedWordWithSpecialChars = new StringBuilder();
        for (int i = 0; i < processedWord.length(); i++) {
            if (processedWord.charAt(i) == '\'') {
                processedWordWithSpecialChars.append("\'");
            } else {
                processedWordWithSpecialChars.append(processedWord.charAt(i));
            }
        }
        return processedWordWithSpecialChars.toString();
    }

}