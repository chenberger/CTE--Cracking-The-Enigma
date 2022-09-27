/*
package UserManger;

import java.util.*;

public class UsersManger {
    private List<String> allies;
    private List<String> agents;
    private List<String> uBoats;
    private List<String> battleFields;

    public UsersManger() {
        allies = new ArrayList<>();
        agents = new ArrayList<>();
        uBoats = new ArrayList<>();
        battleFields = new ArrayList<>();
    }
    //getters region
    public List<String> getAllies() {
        return allies;
    }
    public List<String> getAgents() {
        return agents;
    }
    public List<String> getUBoats() {
        return uBoats;
    }
    //end region

    public synchronized void addUBoat(String username) {
        uBoats.add(username);
    }
    public synchronized void addAgent(String username) {
        agents.add(username);
    }
    public synchronized void addAlly(String username) {
        allies.add(username);
    }
    public synchronized void addBattleField(String battleName) {
        battleFields.add(battleName);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
}
}
*/
