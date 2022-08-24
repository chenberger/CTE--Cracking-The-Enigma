package DesktopUserInterface.MainScene.BodyScene.Machine;

public class PluggedPair {
    private Character firstCharacter;
    private Character secondCharacter;

    public PluggedPair(Character firstCharacter, Character secondCharacter) {
        this.firstCharacter = firstCharacter;
        this.secondCharacter = secondCharacter;
    }

    public Character getFirstCharacter() {
        return firstCharacter;
    }

    public Character getSecondCharacter() {
        return secondCharacter;
    }

    public void setFirstCharacter(Character firstCharacter) {
        this.firstCharacter = firstCharacter;
    }

    public void setSecondCharacter(Character secondCharacter) {
        this.secondCharacter = secondCharacter;
    }
}
