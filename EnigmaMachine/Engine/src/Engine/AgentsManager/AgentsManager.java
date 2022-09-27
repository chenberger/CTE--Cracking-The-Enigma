package Engine.AgentsManager;

import BruteForce.Agent;

import java.util.HashMap;
import java.util.Map;

public class AgentsManager {
    private final Map<String, Agent> agents;

    public AgentsManager() {
        agents = new HashMap<>();

    }

    public boolean isAgentExists(String usernameFromParameter) {
        return agents.containsKey(usernameFromParameter);
    }

    public void addAgent(String usernameFromParameter, String allieTeam, int numberOfThreads, long tasksClonedFromServerEachTime) {
        //agents.put(usernameFromParameter, new Agent());
    }
}
