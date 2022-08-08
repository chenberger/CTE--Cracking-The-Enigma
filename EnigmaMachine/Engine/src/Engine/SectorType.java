package Engine;

import java.util.Arrays;

public enum SectorType {
    ROTORS_ID("Settings"), START_POSITION_ROTORS("Settings"), REFLECTOR("Settings"), PLUGIN_BOARD("Settings"),
    ORIGINAL_STRING("ProcessedStrings"), ENCRYPTED_STRING("ProcessedStrings");

    private final String label;
    private static final String settingsLabel = "Settings";
    SectorType(String settings) {
        this.label = settings;
    }

    public static int getSettingsFormatLength() {
        return (int) Arrays.stream(SectorType.values()).filter(type -> type.label.equals(settingsLabel)).count();


    }
}
