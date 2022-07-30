package EnigmaMachine;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PluginBoard {
    public static final Character PAIR_SEPARATOR = '|';
    private final Map<Character,Character> pluggedPairs = new HashMap<>();
    PluginBoard(String inputtedPluggedPairs){
        String[] pairs = inputtedPluggedPairs.split(",");
        for(String pair : pairs){
            pluggedPairs.put(pair.charAt(0),pair.charAt(1));
            pluggedPairs.put(pair.charAt(1),pair.charAt(0));
        }
    }
    public Character getPlugedPair(Character inputedChar){
        if(!pluggedPairs.containsKey(inputedChar)){
            return inputedChar;
        }
        return pluggedPairs.get(inputedChar);
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
