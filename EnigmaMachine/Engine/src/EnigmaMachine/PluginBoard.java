package EnigmaMachine;


import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PluginBoard {

    public static final Character PAIR_SEPARATOR = '|';
    private final Map<Character,Character> pluggedPairs;
    PluginBoard(String inputtedPluggedPairs){
        pluggedPairs = new HashMap<>();
    }

    public Character getPluggedPair(Character inputtedChar){
        if(!pluggedPairs.containsKey(inputtedChar)){
            return inputtedChar;
        }
        return pluggedPairs.get(inputtedChar);
    }

    public void addPlugginPair(Pair<Character, Character> plugginPair) {
        pluggedPairs.put(plugginPair.getKey(), plugginPair.getValue());
    }

    //region Getters
    public int size() {
        return pluggedPairs.size();
    }

    public Map<Character,Character> getAllPluggedPairs() {
        return pluggedPairs;
    }
    //endregion

}
