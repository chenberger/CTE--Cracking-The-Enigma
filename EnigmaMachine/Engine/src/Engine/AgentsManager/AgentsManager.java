package Engine.AgentsManager;

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

    public void addAgent(String usernameFromParameter) {
        agents.put(usernameFromParameter, new Agent(usernameFromParameter));
    }

    public Agent getAgent(String parameter) {
        return agents.get(parameter);
    }
}
