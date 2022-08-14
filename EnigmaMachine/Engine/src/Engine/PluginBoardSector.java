package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.PluginBoardSettingsException;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PluginBoardSector extends Sector<Pair<Character, Character>> implements Serializable {
    private final CharSequence delimiterElements = ",";
    private final CharSequence delimiterPair = "|";

    public PluginBoardSector(List<Pair<Character, Character>> elements) {
        super(elements, SectorType.PLUGIN_BOARD);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if(elements.size() > 0) {
            result.append(super.openSector);
            for (Pair<Character, Character> pluggedPair : super.elements) {
                if (super.elements.indexOf(pluggedPair) > 0) {
                    result.append(delimiterElements);
                }

                result.append(pluggedPair.getKey().toString() + delimiterPair + pluggedPair.getValue().toString());
            }

            result.append(super.closeSector);
        }

        return result.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new PluginBoardSector(new ArrayList<>(getElements()));
    }

    @Override
    public void validateSector(EnigmaMachine enigmaMachine) throws PluginBoardSettingsException {
        enigmaMachine.validatePluginBoardSettings(this);
    }

    @Override
    public void setSectorInTheMachine(EnigmaMachine enigmaMachine) throws CloneNotSupportedException {
        enigmaMachine.setPluginBoardSettings(this);
    }

    @Override
    public void addSectorToSettingsFormat(EnigmaMachine enigmaMachine) {
        enigmaMachine.getOriginalSettingsFormat().addSector(this);
    }
}
