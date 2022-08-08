package Engine;

import javafx.util.Pair;

import java.util.List;

public class PluginBoardSector extends Sector<Pair<Character, Character>>{
    private final CharSequence delimiterElements = ",";
    private final CharSequence delimiterPair = "|";

    public PluginBoardSector(List<Pair<Character, Character>> elements) {
        super(elements, SectorType.PLUGIN_BOARD);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(super.openSector);
        for (Pair<Character, Character> pluggedPair : super.elements) {
            if(super.elements.indexOf(pluggedPair) > 0) {
                result.append(delimiterElements);
            }
            result.append(pluggedPair.getKey().toString() + delimiterPair + pluggedPair.getValue().toString());
        }

        result.append(super.closeSector);
        return result.toString();
    }
}
