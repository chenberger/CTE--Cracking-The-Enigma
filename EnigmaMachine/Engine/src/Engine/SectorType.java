package Engine;

import java.util.Arrays;

public enum SectorType {
    ROTORS_ID("Settings") {
        @Override
        public Sector getRandomSector(RandomSettingsGenerator randomSettingsGenerator) {
            return randomSettingsGenerator.getRandomRotorsIdSector();
        }
    },
    START_POSITION_ROTORS("Settings") {
        @Override
        public Sector getRandomSector(RandomSettingsGenerator randomSettingsGenerator) {
            return randomSettingsGenerator.getRandomStartingPositionRotorsSector();
        }
    },
    REFLECTOR("Settings") {
        @Override
        public Sector getRandomSector(RandomSettingsGenerator randomSettingsGenerator) {
            return randomSettingsGenerator.getRandomReflectorSector();
        }
    },
    PLUGIN_BOARD("Settings") {
        @Override
        public Sector getRandomSector(RandomSettingsGenerator randomSettingsGenerator) {
            return randomSettingsGenerator.getRandomPluginBoardSector();
        }
    };

    private final String label;
    private static final String settingsLabel = "Settings";
    SectorType(String settings) {
        this.label = settings;
    }

    public static int getSettingsFormatLength() {
        return (int) Arrays.stream(SectorType.values()).filter(type -> type.label.equals(settingsLabel)).count();
    }

    public abstract Sector getRandomSector(RandomSettingsGenerator randomSettingsGenerator);
}
