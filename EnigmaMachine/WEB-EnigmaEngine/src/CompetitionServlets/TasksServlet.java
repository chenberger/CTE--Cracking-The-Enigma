package CompetitionServlets;

import BruteForce.TasksProducer;
import DTO.TaskToAgent;
import Engine.AgentsManager.Agent;
import Engine.AgentsManager.AgentsManager;
import Engine.AlliesManager.Allie;
import Engine.AlliesManager.AlliesManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TasksServlet",urlPatterns = {"/Tasks"})
public class TasksServlet extends HttpServlet {
    @Override
    synchronized protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        if(request.getParameter("action").equals("getTasksInterval")) {
            getTasksFromTasksProducer(request, response);
        }

    }

    synchronized private void getTasksFromTasksProducer(HttpServletRequest request, HttpServletResponse response) {
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
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(gson.toJson(tasks));
            response.getWriter().flush();

        } catch ( IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    }
}
