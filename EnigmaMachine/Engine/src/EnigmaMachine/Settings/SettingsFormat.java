package EnigmaMachine.Settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsFormat implements Serializable {
    private List<Sector> settingsFormat;
    private boolean isPluginBoardSet;
    private int indexFormat;

    public SettingsFormat() {
        this.indexFormat = 1;
        this.settingsFormat = new ArrayList<Sector>();
        this.isPluginBoardSet = false;
    }
    public SettingsFormat(SettingsFormat settingsFormat) {
        this.indexFormat = settingsFormat.indexFormat;
        this.settingsFormat = new ArrayList<Sector>(settingsFormat.settingsFormat);
        this.isPluginBoardSet = settingsFormat.isPluginBoardSet;
    }
    public void advanceIndexFormat() {
        indexFormat++;
    }

    public int getIndexFormat() { return indexFormat; }

    public List<Sector> getSettingsFormat() {
        return settingsFormat;
    }


    public <T> void addSector(Sector<T> sector) {
        if(isSectorExists(sector)) {
            setSectorByType(sector);
        }
        else {
            settingsFormat.add(sector);
        }

        if(sector.getType() == SectorType.PLUGIN_BOARD) {
            isPluginBoardSet = true;
        }
    }

    private <T> boolean isSectorExists(Sector<T> sector) {
        for(Sector sectorToSearch: settingsFormat) {
            if(sectorToSearch.getType() == sector.getType()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        settingsFormat.forEach(sector -> result.append((sector.toString())));
        return result.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SettingsFormat clonedSettings =  new SettingsFormat();
        clonedSettings.settingsFormat = new ArrayList<>();
        settingsFormat.forEach(sector -> {
            try {
                clonedSettings.addSector((Sector) sector.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        clonedSettings.indexFormat = this.indexFormat;
        clonedSettings.isPluginBoardSet = this.isPluginBoardSet;

        return clonedSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingsFormat)) return false;
        SettingsFormat that = (SettingsFormat) o;
        boolean test = settingsFormat.equals(that.settingsFormat);
        return settingsFormat.equals(that.settingsFormat) && (isPluginBoardSet == that.isPluginBoardSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settingsFormat, isPluginBoardSet, indexFormat);
    }

    public boolean isSettingsInitialized() {
        if(isPluginBoardSet) {
            return settingsFormat.size() == SectorType.getSettingsFormatLength();
        }
        else {
            return settingsFormat.size() == SectorType.getSettingsFormatLength() - 1;
        }
    }

    public void clear() {
        indexFormat = 1;
        settingsFormat.clear();
        isPluginBoardSet = false;
    }

    public Sector getSectorByType(SectorType sectorType) {
        if(!isPluginBoardSet && sectorType == SectorType.PLUGIN_BOARD) {
            return null;
        }

        for(Sector sector : settingsFormat) {
            if(sector.getType() == sectorType) {
                return sector;
            }
        }

        throw new IllegalArgumentException("Error: The Sector type is not found in the settings format");
    }

    public void setSectorByType(Sector sector) {
        Sector sectorToUpdate = getSectorByType(sector.getType());
        sectorToUpdate.setElements(sector.getElements());
    }

    public void setIfPluginBoardSet(boolean pluginBoardSet) {
        isPluginBoardSet = pluginBoardSet;
    }
    public boolean isPluginBoardSet() {
        return isPluginBoardSet;
    }
}
