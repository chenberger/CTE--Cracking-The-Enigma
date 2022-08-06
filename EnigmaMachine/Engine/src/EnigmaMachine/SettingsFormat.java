package EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class SettingsFormat {
    private List<Sector> settingsFormat;
    private Boolean isPluginBoardSet;
    public SettingsFormat() {
        this.settingsFormat = new ArrayList<Sector>();
    }
    public List<Sector> getSettingsFormat() {
        return settingsFormat;
    }


    public <T> void addSector(Sector<T> sector) {
        settingsFormat.add(sector);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        settingsFormat.forEach(sector -> result.append((sector.toString())));
        return result.toString();
    }

    public boolean isSettingsInitialized() {
        if(isPluginBoardSet) {
            return settingsFormat.size() == SectorType.values().length;
        }
        else {
            return settingsFormat.size() == SectorType.values().length - 1;
        }
    }

    public void clear() {
        settingsFormat.clear();
    }

    public void isPluginBoardSet(boolean pluginBoardSet) {
        isPluginBoardSet = pluginBoardSet;
    }
}
