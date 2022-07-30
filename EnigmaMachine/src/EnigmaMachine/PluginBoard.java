package EnigmaMachine;

import java.util.HashMap;
import java.util.Map;

public class PluginBoard {
    private final Map<Character,Character> plugedPairs = new HashMap<>();
    PluginBoard(String inputedPlugedPairs){
        String[] pairs = inputedPlugedPairs.split(",");
        for(String pair : pairs){
            plugedPairs.put(pair.charAt(0),pair.charAt(1));
            plugedPairs.put(pair.charAt(1),pair.charAt(0));
        }
    }
    public Character getPlugedPair(Character inputedChar){
        if(!plugedPairs.containsKey(inputedChar)){
            return inputedChar;
        }
        return plugedPairs.get(inputedChar);
    }

}
