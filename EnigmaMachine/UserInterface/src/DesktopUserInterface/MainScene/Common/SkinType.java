package DesktopUserInterface.MainScene.Common;

public enum SkinType {
    CLASSIC("Classic") {

    },
    MATRIX("Matrix") {

    },
    HEAVEN("Heaven"){

    };

    private final String label;
    SkinType(String settings) {
        this.label = settings;
    }
}
