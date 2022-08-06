package EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class SettingsFormat {
    private List<Sector> settingsFormat;
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
        return settingsFormat.size() == SectorType.values().length;
    }

    public void clear() {
        settingsFormat.clear();
    }
}
