package Engine.UBoatManager;

import Engine.BattleField;
import EnigmaMachine.EnigmaMachine;
import Jaxb.Schema.Generated.CTEBattlefield;

import java.util.HashMap;
import java.util.Map;

public class UBoatManager {
    private final Map<String, UBoat> uBoats;
    //private Map<String,BattleField> uBoatsBattleFields;

    public UBoatManager() {
        this.uBoats = new HashMap<>();
        //this.uBoatsBattleFields = new HashMap<>();
    }
    public void setBattleField(String uBoatName, BattleField battleField) {
        //this.uBoatsBattleFields.put(uBoatName,battleField);
        uBoats.get(uBoatName).setBattleField(battleField);
    }

    public boolean isFileExists(String battleName) {
        //return uBoatsBattleFields.values().stream().anyMatch(battleField -> battleField.getBattleName().equals(battleName));
        return uBoats.values().stream().anyMatch(uBoat -> uBoat.getBattleField().getBattleName().equals(battleName));
    }

    public void addUBoat(String username, EnigmaMachine currentEnigmaMachine, CTEBattlefield battleField) throws CloneNotSupportedException {

        uBoats.put(username, new UBoat(username,battleField,
                new EnigmaMachine(currentEnigmaMachine.cloneRotors(),
                        currentEnigmaMachine.cloneReflectors(), currentEnigmaMachine.cloneKeyboard(),
                        currentEnigmaMachine.getNumOfActiveRotors())));
        //uBoatsBattleFields.put(username, new BattleField(battleField.getBattleName(),battleField.getLevel(),battleField.getAllies()));
    }
    public UBoat getUBoat(String username) {
        return  uBoats.get(username);
    }
    public String getUBoatByBattleName(String battleName) {
        return uBoats.values().stream().filter(uBoat -> uBoat.getBattleField().getBattleName().equals(battleName)).findFirst().get().getName();
    }
}
