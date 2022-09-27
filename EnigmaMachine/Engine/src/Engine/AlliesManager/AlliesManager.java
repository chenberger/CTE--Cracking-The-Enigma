package Engine.AlliesManager;

import BruteForce.DecryptionManager;
import Engine.BattleField;

import java.util.HashMap;
import java.util.Map;

public class AlliesManager {
    private final Map<String, DecryptionManager> allies;
    private BattleField battleField;

    public AlliesManager() {
        allies = new HashMap<>();
        this.battleField = new BattleField();
    }
    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }
    //region Getters and Setters
    public Map<String, DecryptionManager> getAllies() {
        return allies;
    }

    public boolean isAllieExists(String usernameFromParameter) {
        return allies.containsKey(usernameFromParameter);
    }

    public void addAllie(String usernameFromParameter) {
        allies.put(usernameFromParameter, new DecryptionManager());
    }

   //public Allie getAllieByName(String allieName) {

   //}
    //endregion

}
