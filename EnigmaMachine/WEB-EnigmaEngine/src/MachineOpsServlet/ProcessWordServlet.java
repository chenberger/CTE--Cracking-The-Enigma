package MachineOpsServlet;

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

import static Utils.Constants.GSON_INSTANCE;

@WebServlet(name = "ProcessWordServlet", urlPatterns = {"/machine/ProcessWord"})
public class ProcessWordServlet extends HttpServlet {
    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        if(request.getParameter("action")!= null && request.getParameter("action").equals("getProcessedWord") ){
            getProcessedWord(request, response);
        }
        else{
            processMessage(request, response);
        }

    }

    private synchronized void processMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try  {
            synchronized (this){
                String wordToProcess = request.getParameter("wordToProcess");
                UBoat currentBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(SessionUtils.getUsername(request));
                EngineManager engineManager = ServletUtils.getUBoatsManager(getServletContext())
                        .getUBoat(SessionUtils.getUsername(request)).getEngineManager();
                String processedWord = engineManager.processInputsFromDictionary(wordToProcess);
                engineManager.displaySpecifications().addMessageToCounter();
                response.setStatus(HttpServletResponse.SC_OK);
                currentBoat.setCurrentProcessedMessage(processedWord);
                currentBoat.getBattleField().setProcessedMessage(processedWord);
                currentBoat.getBattleField().setOriginalMessage(wordToProcess);
                //String processedWordWithSpecialChars = getProcessedWordWithSpecialChars(processedWord);
                String json = GSON_INSTANCE.toJson(processedWord);
                response.getWriter().println(json);
                response.getWriter().flush();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }

    private synchronized void getProcessedWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try  {
            synchronized (this){
                UBoat currentBoat = ServletUtils.getUBoatManager(getServletContext()).getUBoat(request.getParameter("uBoatName"));
                String processedWord = currentBoat.getBattleField().getProcessedMessage();
                if(processedWord == null || processedWord.equals("")){
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_OK);
                    String json = GSON_INSTANCE.toJson(processedWord);
                    response.getWriter().println(json);
                    response.getWriter().flush();
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }


}