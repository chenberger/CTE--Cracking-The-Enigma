package Engine.UBoatManager;

import Engine.BattleField;
import EnigmaMachine.EnigmaMachine;
import Jaxb.Schema.Generated.CTEBattlefield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        uBoats.put(username, new UBoat(username,battleField, currentEnigmaMachine));
        //uBoatsBattleFields.put(username, new BattleField(battleField.getBattleName(),battleField.getLevel(),battleField.getAllies()));
    }
    public UBoat getUBoat(String username) {
        return  uBoats.get(username);
    }
    public String getUBoatByBattleName(String battleName) {
        return uBoats.values().stream().filter(uBoat -> uBoat.getBattleField().getBattleName().equals(battleName)).findFirst().get().getName();
    }
    public Map<String, UBoat> getUBoats() {
        return uBoats;
    }
    public List<UBoat> getUBoatsAsList() {
        return uBoats.values().stream().collect(Collectors.toList());
    }
}
