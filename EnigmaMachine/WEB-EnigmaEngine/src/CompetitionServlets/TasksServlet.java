package CompetitionServlets;

import BruteForce.TasksProducer;
import DTO.TaskToAgent;
import Engine.AgentsManager.Agent;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import Engine.BattleField;
import Engine.UBoatManager.UBoat;
import Engine.UBoatManager.UBoatManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.Request;
import okhttp3.Response;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TasksServlet",urlPatterns = {"/Tasks"})
public class TasksServlet extends HttpServlet {
    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        if(request.getParameter("action").equals("getTasksInterval")) {
            getTasksFromTasksProducer(request, response);
        }
        else if(request.getParameter("action").equals("stopContest")){
            stopCurrentContest(request, response);
        } else if (request.getParameter("action").equals("checkIfContestIsOver")) {
            checkIfContestIsOver(request, response);
        }


    }

    private synchronized void stopCurrentContest(HttpServletRequest request, HttpServletResponse response) {
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        UBoat uBoat = uBoatManager.getUBoat(SessionUtils.getUsername(request));
        uBoat.getBattleField().setWinner(request.getParameter("winner"));
        stopContest(uBoat);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    private synchronized void checkIfContestIsOver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        Allie allie = alliesManager.getAllie(SessionUtils.getAllieName(request));
        String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
        UBoat uBoat = uBoatManager.getUBoat(uBoatName);
        try {
            if (uBoat.getBattleField().getWinner().equals("")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else if (uBoat.getBattleField().getWinner().equals(allie.getTeamName())) {
                String message = "You Won!!! the encrypted message was: " + uBoat.getBattleField().getOriginalMessage();
                Gson gson = new Gson();
                String json = gson.toJson(message);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
                response.getWriter().flush();
            } else if (uBoat.getBattleField().getWinner().equals("Battle Host Left")) {
                String message = "Sorry, but the host has left so the battle is over!! ";
                allie.setBattleName("");
                Gson gson = new Gson();
                String json = gson.toJson(message);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
                response.getWriter().flush();
            } else {
                String message = "You Lost! The winner is: " + uBoat.getBattleField().getWinner() +
                        "!!! The encrypted message was: " + uBoat.getBattleField().getOriginalMessage();
                Gson gson = new Gson();
                String json = gson.toJson(message);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
                response.getWriter().flush();

            }
        } catch (Exception e) {
            String message = "Sorry, but the host has left so the battle is over!! ";
            allie.setBattleName("");
            Gson gson = new Gson();
            String json = gson.toJson(message);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);
            response.getWriter().flush();
        }
    }
    private synchronized void getTasksFromTasksProducer(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        boolean wait = true;
        AlliesManager alliesManager = ServletUtils.getAlliesManager(getServletContext());
        AgentsManager agentsManager = ServletUtils.getAgentsManager(getServletContext());
        Agent agent = agentsManager.getAgent(request.getParameter("agentName"));
        Allie allie = alliesManager.getAllie(agent.getAllieName());
        try {
            TasksProducer tasksProducer = allie.getDecryptionManager().getTasksProducer();
            while(wait){
                if(tasksProducer.getTasksQueue().size() >= agent.getTasksPullingInterval()){
                    wait = false;
                } else if (tasksProducer.isNoMoreTasks()) {
                    wait = false;
                }
            }
            List<TaskToAgent> tasks = new ArrayList<>();
            for(int i = 0; i < agent.getTasksPullingInterval(); i++){
                if(tasksProducer.getTasksQueue().size() > 0){
                    tasks.add(tasksProducer.getTasksQueue().poll());
                }
            }
            if(tasksProducer.isNoMoreTasks()){
                UBoatManager uBoatManager = ServletUtils.getUBoatManager(getServletContext());
                String uBoatName = uBoatManager.getUBoatByBattleName(allie.getBattleName());
                UBoat uBoat = uBoatManager.getUBoat(uBoatName);
                //stopContest(uBoat);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(gson.toJson(tasks));
            response.getWriter().flush();

        } catch ( IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    }

    private void stopContest(UBoat uBoat) {
        uBoat.getBattleField().stopContest();
        uBoat.setContestOver();
    }
}
