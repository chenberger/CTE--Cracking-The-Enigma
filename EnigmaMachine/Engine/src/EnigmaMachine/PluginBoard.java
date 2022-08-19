package EnigmaMachine;


import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
public class PluginBoard implements Serializable {

    public static final Character PAIR_SEPARATOR = '|';
    private final Map<Character,Character> pluggedPairs;
    public PluginBoard(String inputtedPluggedPairs){
        pluggedPairs = new HashMap<>();
    }

    public PluginBoard(){
        pluggedPairs = new HashMap<>();
    }

    public Character getPluggedPair(Character inputtedChar){
        if(!pluggedPairs.containsKey(inputtedChar)){
            return inputtedChar;
        }
        return pluggedPairs.get(inputtedChar);
    }

    public void addPluginPair(Pair<Character, Character> pluginPair) {
        pluggedPairs.put(pluginPair.getKey(), pluginPair.getValue());
        pluggedPairs.put(pluginPair.getValue(), pluginPair.getKey());
    }

    public void clear() {
        pluggedPairs.clear();
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
