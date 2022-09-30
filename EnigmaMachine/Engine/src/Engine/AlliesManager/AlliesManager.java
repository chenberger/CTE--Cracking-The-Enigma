package Engine.AlliesManager;

import BruteForce.DecryptionManager;
import Engine.BattleField;

import java.util.HashMap;
import java.util.Map;

public class AlliesManager {
    private final Map<String, Allie> allies;

    public AlliesManager() {
        allies = new HashMap<>();
    }

    //region Getters and Setters
    public Map<String, Allie> getAllies() {
        return allies;
    }

    public boolean isAllieExists(String usernameFromParameter) {
        return allies.containsKey(usernameFromParameter);
    }

    public void addAllie(String usernameFromParameter) {
        allies.put(usernameFromParameter, new Allie(usernameFromParameter));
    }

    public Allie getAllie(String allieNameFromSession) {
        return allies.get(allieNameFromSession);
    }
    //TODO chen: maybe should add setters for battleName and level
    public void removeAllie(String allieName){
        allies.get(allieName).setBattleName(null);
        allies.get(allieName).setTaskSize(0);

    }
    //endregion

}
